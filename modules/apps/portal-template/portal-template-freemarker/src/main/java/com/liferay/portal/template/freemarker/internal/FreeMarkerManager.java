/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.template.freemarker.internal;

import com.liferay.petra.concurrent.ConcurrentReferenceKeyHashMap;
import com.liferay.petra.concurrent.NoticeableExecutorService;
import com.liferay.petra.concurrent.NoticeableFuture;
import com.liferay.petra.concurrent.ThreadPoolHandlerAdapter;
import com.liferay.petra.executor.PortalExecutorConfig;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.petra.memory.FinalizeManager;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.SingleVMPool;
import com.liferay.portal.kernel.cache.thread.local.Lifecycle;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCacheManager;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.JSPSupportServlet;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoader;
import com.liferay.portal.kernel.util.NamedThreadFactory;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.template.BaseTemplateManager;
import com.liferay.portal.template.TemplateContextHelper;
import com.liferay.portal.template.freemarker.configuration.FreeMarkerEngineConfiguration;
import com.liferay.portal.template.freemarker.internal.helper.FreeMarkerTemplateContextHelper;
import com.liferay.taglib.TagSupport;

import freemarker.cache.TemplateCache;

import freemarker.core.TemplateClassResolver;

import freemarker.debug.impl.DebuggerService;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.jsp.internal.WriterFactoryUtil;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.net.URL;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * @author Mika Koivisto
 * @author Tina Tina
 * @author Raymond Augé
 */
@Component(
	configurationPid = "com.liferay.portal.template.freemarker.configuration.FreeMarkerEngineConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = "language.type=" + TemplateConstants.LANG_TYPE_FTL,
	service = TemplateManager.class
)
public class FreeMarkerManager extends BaseTemplateManager {

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static BeansWrapper getBeansWrapper() {
		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		BeansWrapper beansWrapper = _beansWrappers.get(classLoader);

		if (beansWrapper == null) {
			BeansWrapperBuilder beansWrapperBuilder = new BeansWrapperBuilder(
				Configuration.getVersion());

			beansWrapper = beansWrapperBuilder.build();

			_beansWrappers.put(classLoader, beansWrapper);
		}

		return beansWrapper;
	}

	@Override
	public void destroy() {
		if (_configuration == null) {
			return;
		}

		_configuration.clearEncodingMap();
		_configuration.clearSharedVariables();
		_configuration.clearTemplateCache();

		_configuration = null;

		templateContextHelper.removeAllHelperUtilities();

		_templateModels.clear();

		if (_isEnableDebuggerService()) {
			//DebuggerService.shutdown();
		}
	}

	@Override
	public void destroy(ClassLoader classLoader) {
		templateContextHelper.removeHelperUtilities(classLoader);
	}

	@Override
	public String getName() {
		return TemplateConstants.LANG_TYPE_FTL;
	}

	@Override
	public String[] getRestrictedVariables() {
		return _freeMarkerEngineConfiguration.restrictedVariables();
	}

	@Override
	public void init() throws TemplateException {
		if (_configuration != null) {
			return;
		}

		_configuration = new Configuration(Configuration.getVersion());

		try {
			Field field = ReflectionUtil.getDeclaredField(
				Configuration.class, "cache");

			PortalCache<TemplateResource, TemplateCache.MaybeMissingTemplate>
				portalCache =
					_freeMarkerTemplateResourceCache.
						getSecondLevelPortalCache();

			TemplateCache templateCache = new LiferayTemplateCache(
				_configuration, templateResourceLoader, portalCache);

			field.set(_configuration, templateCache);

			_configuration.setSharedVariable(
				"loop-count-threshold",
				new SimpleNumber(
					_freeMarkerEngineConfiguration.loopCountThreshold()));
		}
		catch (Exception exception) {
			throw new TemplateException(
				"Unable to Initialize FreeMarker manager", exception);
		}

		_configuration.setDefaultEncoding(StringPool.UTF8);
		_configuration.setLocalizedLookup(
			_freeMarkerEngineConfiguration.localizedLookup());
		_configuration.setNewBuiltinClassResolver(_templateClassResolver);

		try {
			_configuration.setSetting("auto_import", _getMacroLibrary());
			_configuration.setSetting(
				"template_exception_handler",
				_freeMarkerEngineConfiguration.templateExceptionHandler());
		}
		catch (Exception exception) {
			throw new TemplateException(
				"Unable to init FreeMarker manager", exception);
		}

		_defaultBeansWrapper = new LiferayObjectWrapper();
		_restrictedBeansWrapper = new RestrictedLiferayObjectWrapper(
			_freeMarkerEngineConfiguration.allowedClasses(),
			_freeMarkerEngineConfiguration.restrictedClasses(),
			_freeMarkerEngineConfiguration.restrictedMethods());

		if (_isEnableDebuggerService()) {
			DebuggerService.getBreakpoints("*");
		}

		FreeMarkerTemplateContextHelper freeMarkerTemplateContextHelper =
			(FreeMarkerTemplateContextHelper)templateContextHelper;

		freeMarkerTemplateContextHelper.setDefaultBeansWrapper(
			_defaultBeansWrapper);
		freeMarkerTemplateContextHelper.setRestrictedBeansWrapper(
			_restrictedBeansWrapper);
	}

	@Reference(unbind = "-")
	public void setTemplateClassResolver(
		TemplateClassResolver templateClassResolver) {

		_templateClassResolver = templateClassResolver;
	}

	@Override
	@Reference(service = FreeMarkerTemplateContextHelper.class, unbind = "-")
	public void setTemplateContextHelper(
		TemplateContextHelper templateContextHelper) {

		super.setTemplateContextHelper(templateContextHelper);
	}

	@Override
	@Reference(service = FreeMarkerTemplateResourceLoader.class, unbind = "-")
	public void setTemplateResourceLoader(
		TemplateResourceLoader templateResourceLoader) {

		super.setTemplateResourceLoader(templateResourceLoader);
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		_freeMarkerEngineConfiguration = ConfigurableUtil.createConfigurable(
			FreeMarkerEngineConfiguration.class,
			componentContext.getProperties());

		BundleContext bundleContext = componentContext.getBundleContext();

		_bundle = bundleContext.getBundle();

		_freeMarkerBundleClassloader = new FreeMarkerBundleClassloader(
			_bundle, FrameworkUtil.getBundle(TagSupport.class));

		int stateMask = ~Bundle.INSTALLED & ~Bundle.UNINSTALLED;

		_bundleTracker = new BundleTracker<>(
			bundleContext, stateMask, new TaglibBundleTrackerCustomizer());

		_bundleTracker.open();

		WriterFactoryUtil.setWriterFactory(new UnsyncStringWriterFactory());

		_initAsyncRender(bundleContext);
	}

	protected void addTaglibSupport(
		Map<String, Object> contextObjects,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, ObjectWrapper objectWrapper) {

		ServletContext servletContext = httpServletRequest.getServletContext();

		contextObjects.put(
			"Application",
			_getServletContextHashModel(servletContext, objectWrapper));

		contextObjects.put(
			"Request",
			new HttpRequestHashModel(
				httpServletRequest, httpServletResponse, objectWrapper));

		// Legacy

		TaglibFactoryWrapper taglibFactoryWrapper = new TaglibFactoryWrapper(
			servletContext, objectWrapper);

		contextObjects.put("PortalJspTagLibs", taglibFactoryWrapper);
		contextObjects.put("PortletJspTagLibs", taglibFactoryWrapper);
		contextObjects.put("taglibLiferayHash", taglibFactoryWrapper);

		// Contributed

		for (Map.Entry<String, String> entry : _taglibMappings.entrySet()) {
			try {
				contextObjects.put(
					entry.getKey(), taglibFactoryWrapper.get(entry.getValue()));
			}
			catch (TemplateModelException templateModelException) {
				_log.error(
					"Unable to add taglib " + entry.getKey() + " to context",
					templateModelException);
			}
		}
	}

	@Deactivate
	protected void deactivate() {
		_bundleTracker.close();

		if (_freeMarkerEngineConfiguration.asyncRenderTimeout() > 0) {
			_noticeableExecutorService.shutdownNow();

			_timeoutTemplateCounters.clear();

			_serviceRegistration.unregister();
		}
	}

	@Override
	protected Template doGetTemplate(
		TemplateResource templateResource, boolean restricted,
		Map<String, Object> helperUtilities) {

		BeansWrapper beansWrapper = _defaultBeansWrapper;

		if (restricted) {
			beansWrapper = _restrictedBeansWrapper;
		}

		return new FreeMarkerTemplate(
			templateResource, helperUtilities, _configuration,
			templateContextHelper, _freeMarkerTemplateResourceCache, restricted,
			beansWrapper, this);
	}

	@Modified
	protected void modified(ComponentContext componentContext) {
		if (_freeMarkerEngineConfiguration.asyncRenderTimeout() > 0) {
			_noticeableExecutorService.shutdownNow();

			_noticeableExecutorService = null;

			_serviceRegistration.unregister();

			_serviceRegistration = null;

			_timeoutTemplateCounters.clear();

			_timeoutTemplateCounters = null;
		}

		_freeMarkerEngineConfiguration = ConfigurableUtil.createConfigurable(
			FreeMarkerEngineConfiguration.class,
			componentContext.getProperties());

		_initAsyncRender(componentContext.getBundleContext());
	}

	protected void render(
			String templateId, Writer writer, boolean restricted,
			Callable<Void> callable)
		throws Exception {

		long timeout = _freeMarkerEngineConfiguration.asyncRenderTimeout();

		if ((timeout <= 0) || !restricted) {
			callable.call();

			return;
		}

		AtomicInteger timeoutCounter = _timeoutTemplateCounters.computeIfAbsent(
			templateId, key -> new AtomicInteger(0));

		if (timeoutCounter.get() >=
				_freeMarkerEngineConfiguration.asyncRenderTimeoutThreshold()) {

			throw new IllegalStateException(
				StringBundler.concat(
					"Skip processing FreeMarker template ", templateId,
					" since it has timed out ",
					_freeMarkerEngineConfiguration.
						asyncRenderTimeoutThreshold(),
					" times"));
		}

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		Object threadLocals = ThreadLocalUtil._cloneThreadLocals(currentThread);

		NoticeableFuture<?> noticeableFuture =
			_noticeableExecutorService.submit(
				(Callable<Void>)() -> {
					Thread thread = Thread.currentThread();

					thread.setContextClassLoader(contextClassLoader);

					try {
						ThreadLocalUtil._setThreadLocals(thread, threadLocals);

						callable.call();
					}
					finally {
						ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);

						ThreadLocalUtil._setThreadLocals(thread, null);
					}

					return null;
				});

		try {
			noticeableFuture.get(timeout, TimeUnit.MILLISECONDS);
		}
		catch (ExecutionException executionException) {
			Throwable throwable = executionException.getCause();

			if (throwable instanceof Exception) {
				throw (Exception)throwable;
			}

			throw new Exception(throwable);
		}
		catch (TimeoutException timeoutException) {
			timeoutCounter.incrementAndGet();

			String errorMessage = StringBundler.concat(
				"FreeMarker template ", templateId, " processing timeout");

			writer.write(errorMessage);

			_log.error(errorMessage, timeoutException);

			ThreadLocalUtil._clearThreadLocals(threadLocals);
		}
	}

	@Reference(unbind = "-")
	protected void setSingleVMPool(SingleVMPool singleVMPool) {
		_singleVMPool = singleVMPool;
	}

	private String _getMacroLibrary() {
		Set<String> macroLibraries = SetUtil.fromArray(
			_freeMarkerEngineConfiguration.macroLibrary());

		macroLibraries.add(_LIFERAY_MACRO_LIBRARY);

		Class<?> clazz = getClass();

		String contextName = ClassLoaderPool.getContextName(
			clazz.getClassLoader());

		contextName = contextName.concat(
			TemplateConstants.CLASS_LOADER_SEPARATOR);

		StringBundler sb = new StringBundler(3 * macroLibraries.size());

		for (String library : macroLibraries) {
			if (_hasLibrary(library)) {
				sb.append(contextName);
				sb.append(library);
				sb.append(StringPool.COMMA);
			}
			else if (_log.isWarnEnabled()) {
				_log.warn("Unable to find library: " + library);
			}
		}

		if (sb.index() > 0) {
			sb.setIndex(sb.index() - 1);
		}

		return sb.toString();
	}

	private ServletContextHashModel _getServletContextHashModel(
		ServletContext servletContext, ObjectWrapper objectWrapper) {

		GenericServlet genericServlet = new JSPSupportServlet(servletContext);

		return new ServletContextHashModel(genericServlet, objectWrapper);
	}

	private ServletContext _getServletContextWrapper(
		ServletContext servletContext,
		FreeMarkerBundleClassloader freeMarkerBundleClassloader) {

		return _servletContextProxyProviderFunction.apply(
			new ServletContextInvocationHandler(
				servletContext, freeMarkerBundleClassloader));
	}

	private boolean _hasLibrary(String library) {
		int index = library.indexOf(CharPool.SPACE);

		if (index != -1) {
			library = library.substring(0, index);
		}

		if (_bundle.getResource(library) == null) {
			return false;
		}

		return true;
	}

	private void _initAsyncRender(BundleContext bundleContext) {
		if (_freeMarkerEngineConfiguration.asyncRenderTimeout() <= 0) {
			return;
		}

		_noticeableExecutorService = _portalExecutorManager.getPortalExecutor(
			FreeMarkerManager.class.getName());
		_serviceRegistration = bundleContext.registerService(
			PortalExecutorConfig.class,
			new PortalExecutorConfig(
				FreeMarkerManager.class.getName(), 1,
				_freeMarkerEngineConfiguration.asyncRenderThreadPoolMaxSize(),
				60, TimeUnit.SECONDS,
				_freeMarkerEngineConfiguration.
					asyncRenderThreadPoolMaxQueueSize(),
				new NamedThreadFactory(
					FreeMarkerManager.class.getName(), Thread.NORM_PRIORITY,
					null),
				new ThreadPoolExecutor.AbortPolicy(),
				new ThreadPoolHandlerAdapter()),
			null);
		_timeoutTemplateCounters = new ConcurrentHashMap<>();
	}

	private boolean _isEnableDebuggerService() {
		if ((System.getProperty("freemarker.debug.password") != null) &&
			(System.getProperty("freemarker.debug.port") != null)) {

			return true;
		}

		return false;
	}

	private static final String _LIFERAY_MACRO_LIBRARY =
		"FTL_liferay.ftl as liferay";

	private static final Log _log = LogFactoryUtil.getLog(
		FreeMarkerManager.class);

	private static final Map<ClassLoader, BeansWrapper> _beansWrappers =
		new ConcurrentReferenceKeyHashMap<>(
			FinalizeManager.WEAK_REFERENCE_FACTORY);
	private static final Function<InvocationHandler, ServletContext>
		_servletContextProxyProviderFunction =
			ProxyUtil.getProxyProviderFunction(ServletContext.class);

	private Bundle _bundle;
	private BundleTracker<Set<String>> _bundleTracker;
	private volatile Configuration _configuration;
	private volatile BeansWrapper _defaultBeansWrapper;
	private FreeMarkerBundleClassloader _freeMarkerBundleClassloader;
	private volatile FreeMarkerEngineConfiguration
		_freeMarkerEngineConfiguration;

	@Reference
	private FreeMarkerTemplateResourceCache _freeMarkerTemplateResourceCache;

	private volatile NoticeableExecutorService _noticeableExecutorService;

	@Reference
	private PortalExecutorManager _portalExecutorManager;

	private volatile BeansWrapper _restrictedBeansWrapper;
	private volatile ServiceRegistration<PortalExecutorConfig>
		_serviceRegistration;
	private SingleVMPool _singleVMPool;
	private final Map<String, String> _taglibMappings =
		new ConcurrentHashMap<>();
	private TemplateClassResolver _templateClassResolver;
	private final Map<String, TemplateModel> _templateModels =
		new ConcurrentHashMap<>();
	private volatile Map<String, AtomicInteger> _timeoutTemplateCounters;

	private static class ThreadLocalUtil {

		private static void _clearThreadLocals(Object threadLocals)
			throws Exception {

			_sizeField.set(threadLocals, 0);
			_tableField.set(threadLocals, Array.newInstance(_ENTRY_CLASS, 0));
			_thresholdField.set(threadLocals, 0);
		}

		private static Object _cloneThreadLocals(Thread thread)
			throws Exception {

			Object threadLocals = _threadLocalsField.get(thread);

			Object table = _tableField.get(threadLocals);

			int length = Array.getLength(table);

			try {
				_tableField.set(
					threadLocals, Array.newInstance(_ENTRY_CLASS, length));

				Object clonedThreadLocals = _createInheritedMapMethod.invoke(
					null, threadLocals);

				System.arraycopy(
					table, 0, _tableField.get(clonedThreadLocals), 0, length);

				_sizeField.set(
					clonedThreadLocals, _sizeField.get(threadLocals));

				return clonedThreadLocals;
			}
			finally {
				_tableField.set(threadLocals, table);
			}
		}

		private static void _setThreadLocals(Thread thread, Object threadLocals)
			throws Exception {

			_threadLocalsField.set(thread, threadLocals);
		}

		private static final Class<?> _ENTRY_CLASS;

		private static final Method _createInheritedMapMethod;
		private static final Field _sizeField;
		private static final Field _tableField;
		private static final Field _threadLocalsField;
		private static final Field _thresholdField;

		static {
			try {
				_threadLocalsField = ReflectionUtil.getDeclaredField(
					Thread.class, "threadLocals");

				Class<?> threadLocalMapClass = _threadLocalsField.getType();

				_createInheritedMapMethod = ReflectionUtil.getDeclaredMethod(
					ThreadLocal.class, "createInheritedMap",
					threadLocalMapClass);

				_sizeField = ReflectionUtil.getDeclaredField(
					threadLocalMapClass, "size");
				_tableField = ReflectionUtil.getDeclaredField(
					threadLocalMapClass, "table");
				_thresholdField = ReflectionUtil.getDeclaredField(
					threadLocalMapClass, "threshold");

				Class<?> tableFieldType = _tableField.getType();

				_ENTRY_CLASS = tableFieldType.getComponentType();
			}
			catch (Exception exception) {
				throw new ExceptionInInitializerError(exception);
			}
		}

	}

	private class ServletContextInvocationHandler implements InvocationHandler {

		public ServletContextInvocationHandler(
			ServletContext servletContext,
			FreeMarkerBundleClassloader freeMarkerBundleClassloader) {

			_servletContext = servletContext;
			_freeMarkerBundleClassloader = freeMarkerBundleClassloader;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			String methodName = method.getName();

			if (methodName.equals("getClassLoader")) {
				return _freeMarkerBundleClassloader;
			}
			else if (methodName.equals("getResource")) {
				return _getResource((String)args[0]);
			}
			else if (methodName.equals("getResourceAsStream")) {
				return _getResourceAsInputStream((String)args[0]);
			}
			else if (methodName.equals("getResourcePaths")) {
				return _getResourcePaths((String)args[0]);
			}

			return method.invoke(_servletContext, args);
		}

		private URL _getExtension(String path) {
			Enumeration<URL> enumeration = _bundle.findEntries(
				"META-INF/resources", path.substring(1), false);

			if (enumeration == null) {
				return null;
			}

			List<URL> urls = Collections.list(enumeration);

			return urls.get(urls.size() - 1);
		}

		private URL _getResource(String path) {
			if (path.charAt(0) != '/') {
				path = '/' + path;
			}

			URL url = _getExtension(path);

			if (url != null) {
				return url;
			}

			url = _freeMarkerBundleClassloader.getResource(path);

			if (url != null) {
				return url;
			}

			if (path.startsWith("/WEB-INF/tld/")) {
				String adaptedPath =
					"/META-INF/" + path.substring("/WEB-INF/tld/".length());

				url = _getExtension(adaptedPath);

				if (url == null) {
					url = _bundle.getResource(adaptedPath);
				}
			}

			if (url != null) {
				return url;
			}

			if (!path.startsWith("/META-INF/") &&
				!path.startsWith("/WEB-INF/")) {

				url = _bundle.getResource("/META-INF/resources" + path);
			}

			return url;
		}

		private InputStream _getResourceAsInputStream(String path) {
			URL url = _getResource(path);

			if (url == null) {
				return null;
			}

			try {
				return url.openStream();
			}
			catch (IOException ioException) {
				if (_log.isDebugEnabled()) {
					_log.debug(ioException);
				}

				return null;
			}
		}

		private Set<String> _getResourcePaths(String path) {
			Enumeration<URL> enumeration = _bundle.findEntries(
				path, null, true);

			if (enumeration == null) {
				return null;
			}

			Set<String> resourcePaths = new HashSet<>();

			while (enumeration.hasMoreElements()) {
				URL url = enumeration.nextElement();

				resourcePaths.add(url.getPath());
			}

			return resourcePaths;
		}

		private final FreeMarkerBundleClassloader _freeMarkerBundleClassloader;
		private final ServletContext _servletContext;

	}

	private class TaglibBundleTrackerCustomizer
		implements BundleTrackerCustomizer<Set<String>> {

		@Override
		public Set<String> addingBundle(
			Bundle bundle, BundleEvent bundleEvent) {

			URL url = bundle.getEntry("/META-INF/taglib-mappings.properties");

			if (url == null) {
				BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

				List<BundleCapability> bundleCapabilities =
					bundleWiring.getCapabilities("osgi.extender");

				for (BundleCapability bundleCapability : bundleCapabilities) {
					Map<String, Object> attributes =
						bundleCapability.getAttributes();

					Object value = attributes.get("osgi.extender");

					if (value.equals("jsp.taglib")) {
						return Collections.emptySet();
					}
				}
			}
			else {
				try (InputStream inputStream = url.openStream()) {
					Properties properties = PropertiesUtil.load(
						inputStream, StringPool.UTF8);

					@SuppressWarnings("unchecked")
					Map<String, String> map = PropertiesUtil.toMap(properties);

					if (!map.isEmpty()) {
						_freeMarkerBundleClassloader.addBundle(bundle);

						_taglibMappings.putAll(map);
					}

					return map.keySet();
				}
				catch (Exception exception) {
					_log.error(exception);
				}
			}

			return null;
		}

		@Override
		public void modifiedBundle(
			Bundle bundle, BundleEvent bundleEvent, Set<String> trackedKeys) {
		}

		@Override
		public void removedBundle(
			Bundle bundle, BundleEvent bundleEvent, Set<String> trackedKeys) {

			for (String key : trackedKeys) {
				_taglibMappings.remove(key);
			}

			_templateModels.clear();

			_freeMarkerBundleClassloader.removeBundle(bundle);
		}

	}

	private class TaglibFactoryWrapper implements TemplateHashModel {

		public TaglibFactoryWrapper(
			ServletContext servletContext, ObjectWrapper objectWrapper) {

			_taglibFactory = new TaglibFactory(
				_getServletContextWrapper(
					servletContext, _freeMarkerBundleClassloader));

			_taglibFactory.setObjectWrapper(objectWrapper);
		}

		@Override
		public TemplateModel get(String uri) throws TemplateModelException {
			TemplateModel templateModel = _templateModels.get(uri);

			if (templateModel == null) {
				Thread currentThread = Thread.currentThread();

				ClassLoader contextClassLoader =
					currentThread.getContextClassLoader();

				try {
					currentThread.setContextClassLoader(
						_freeMarkerBundleClassloader);

					templateModel = _taglibFactory.get(uri);
				}
				finally {
					currentThread.setContextClassLoader(contextClassLoader);
				}

				_templateModels.put(uri, templateModel);
			}

			return templateModel;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		private final TaglibFactory _taglibFactory;

	}

}