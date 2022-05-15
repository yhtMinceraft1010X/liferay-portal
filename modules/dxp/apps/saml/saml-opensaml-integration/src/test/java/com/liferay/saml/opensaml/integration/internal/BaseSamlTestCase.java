/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.opensaml.integration.internal;

import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.configuration.ConfigurationFactory;
import com.liferay.portal.kernel.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.saml.constants.SamlProviderConfigurationKeys;
import com.liferay.saml.opensaml.integration.internal.binding.HttpPostBinding;
import com.liferay.saml.opensaml.integration.internal.binding.HttpRedirectBinding;
import com.liferay.saml.opensaml.integration.internal.binding.HttpSoap11Binding;
import com.liferay.saml.opensaml.integration.internal.binding.SamlBinding;
import com.liferay.saml.opensaml.integration.internal.bootstrap.OpenSamlBootstrap;
import com.liferay.saml.opensaml.integration.internal.credential.FileSystemKeyStoreManagerImpl;
import com.liferay.saml.opensaml.integration.internal.credential.KeyStoreCredentialResolver;
import com.liferay.saml.opensaml.integration.internal.identifier.SamlIdentifierGeneratorStrategyFactory;
import com.liferay.saml.opensaml.integration.internal.metadata.MetadataGeneratorUtil;
import com.liferay.saml.opensaml.integration.internal.metadata.MetadataManagerImpl;
import com.liferay.saml.opensaml.integration.internal.servlet.profile.IdentifierGenerationStrategyFactory;
import com.liferay.saml.opensaml.integration.internal.velocity.VelocityEngineFactory;
import com.liferay.saml.persistence.model.SamlPeerBinding;
import com.liferay.saml.persistence.model.impl.SamlPeerBindingImpl;
import com.liferay.saml.persistence.service.SamlPeerBindingLocalService;
import com.liferay.saml.persistence.service.SamlPeerBindingLocalServiceUtil;
import com.liferay.saml.runtime.configuration.SamlProviderConfiguration;
import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper;
import com.liferay.saml.runtime.metadata.LocalEntityManager;
import com.liferay.saml.util.PortletPropsKeys;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;
import net.shibboleth.utilities.java.support.security.IdentifierGenerationStrategy;
import net.shibboleth.utilities.java.support.xml.ParserPool;

import org.apache.http.client.HttpClient;
import org.apache.velocity.app.VelocityEngine;

import org.junit.After;
import org.junit.Before;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.metadata.resolver.impl.AbstractMetadataResolver;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.SingleLogoutService;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.security.credential.Credential;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Mika Koivisto
 */
public abstract class BaseSamlTestCase {

	@Before
	public void setUp() throws Exception {
		_setupProps();

		OpenSamlBootstrap.bootstrap();

		_setupConfiguration();
		_setupIdentifiers();
		_setupParserPool();

		_setupPortal();

		_setupMetadata();

		_setupSamlBindings();

		_setupSamlPeerBindingsLocalService();
	}

	@After
	public void tearDown() {
		identifiers.clear();

		for (Class<?> serviceUtilClass : serviceUtilClasses) {
			ReflectionTestUtil.setFieldValue(
				serviceUtilClass, "_service", null);
		}

		ClassLoaderPool.unregister("saml-portlet");
	}

	protected Credential getCredential(String entityId) throws Exception {
		EntityIdCriterion entityIdCriterion = new EntityIdCriterion(entityId);

		CriteriaSet criteriaSet = new CriteriaSet();

		criteriaSet.add(entityIdCriterion);

		if (entityId.equals(samlProviderConfiguration.entityId())) {
			return credentialResolver.resolveSingle(criteriaSet);
		}

		KeyStoreCredentialResolver keyStoreCredentialResolver =
			_getKeyStoreCredentialResolver(entityId);

		return keyStoreCredentialResolver.resolveSingle(criteriaSet);
	}

	protected MockHttpServletRequest getMockHttpServletRequest(String url) {
		String protocol = url.substring(0, url.indexOf(":"));
		String queryString = StringPool.BLANK;
		String requestURI = StringPool.BLANK;
		String serverName = StringPool.BLANK;
		int serverPort = 80;

		if (url.indexOf(StringPool.COLON, protocol.length() + 3) > 0) {
			serverName = url.substring(
				protocol.length() + 3,
				url.indexOf(StringPool.COLON, protocol.length() + 3));
			serverPort = GetterUtil.getInteger(
				url.substring(
					url.indexOf(StringPool.COLON, protocol.length() + 3) + 1,
					url.indexOf(StringPool.SLASH, protocol.length() + 3)));
		}
		else {
			serverName = url.substring(
				protocol.length() + 3,
				url.indexOf(StringPool.SLASH, protocol.length() + 3));
		}

		if (url.indexOf(StringPool.QUESTION) > 0) {
			queryString = url.substring(url.indexOf(StringPool.QUESTION) + 1);
			requestURI = url.substring(
				url.indexOf(StringPool.SLASH, protocol.length() + 3),
				url.indexOf(StringPool.QUESTION));
		}
		else {
			requestURI = url.substring(
				url.indexOf(StringPool.SLASH, protocol.length() + 3));
		}

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest("GET", requestURI);

		mockHttpServletRequest.setQueryString(queryString);
		mockHttpServletRequest.setContextPath(StringPool.SLASH);
		mockHttpServletRequest.setSecure(protocol.equals("https"));
		mockHttpServletRequest.setServerPort(serverPort);
		mockHttpServletRequest.setServerName(serverName);

		if (Validator.isNull(queryString)) {
			return mockHttpServletRequest;
		}

		String[] parameters = StringUtil.split(
			queryString, StringPool.AMPERSAND);

		for (String parameter : parameters) {
			String[] kvp = StringUtil.split(parameter, StringPool.EQUAL);

			try {
				String value = URLDecoder.decode(kvp[1], StringPool.UTF8);

				mockHttpServletRequest.setParameter(kvp[0], value);
			}
			catch (UnsupportedEncodingException unsupportedEncodingException) {
			}
		}

		return mockHttpServletRequest;
	}

	protected <T> T getMockPortalService(
		Class<?> serviceUtilClass, Class<T> serviceClass) {

		return _getMockService(serviceUtilClass, serviceClass);
	}

	protected <T> T getMockPortletService(
		Class<?> serviceUtilClass, Class<T> serviceClass) {

		return _getMockService(serviceUtilClass, serviceClass);
	}

	protected void prepareIdentityProvider(String entityId) {
		Mockito.when(
			samlProviderConfiguration.entityId()
		).thenReturn(
			entityId
		);

		Mockito.when(
			samlProviderConfiguration.role()
		).thenReturn(
			SamlProviderConfigurationKeys.SAML_ROLE_IDP
		);

		Mockito.when(
			samlProviderConfigurationHelper.isRoleIdp()
		).thenReturn(
			true
		);
	}

	protected long prepareSamlPeerBinding(
		String peerEntityId, String samlNameIdFormat,
		String samlNameIdNameQualifier, String samlNameIdValue) {

		SamlPeerBinding samlPeerBinding = new SamlPeerBindingImpl();

		samlPeerBinding.setSamlPeerBindingId(_samlPeerBindings.size() + 1);
		samlPeerBinding.setCompanyId(COMPANY_ID);
		samlPeerBinding.setDeleted(false);
		samlPeerBinding.setSamlNameIdFormat(samlNameIdFormat);
		samlPeerBinding.setSamlNameIdNameQualifier(samlNameIdNameQualifier);
		samlPeerBinding.setSamlNameIdValue(samlNameIdValue);
		samlPeerBinding.setSamlPeerEntityId(peerEntityId);

		_samlPeerBindings.put(
			samlPeerBinding.getSamlPeerBindingId(), samlPeerBinding);

		return samlPeerBinding.getSamlPeerBindingId();
	}

	protected void prepareServiceProvider(String entityId) {
		Mockito.when(
			samlProviderConfiguration.entityId()
		).thenReturn(
			entityId
		);

		Mockito.when(
			samlProviderConfiguration.role()
		).thenReturn(
			SamlProviderConfigurationKeys.SAML_ROLE_SP
		);

		Mockito.when(
			samlProviderConfigurationHelper.isRoleSp()
		).thenReturn(
			true
		);
	}

	protected static final String ACS_URL =
		"http://localhost:8080/c/portal/saml/acs";

	protected static final long COMPANY_ID = 1;

	protected static final String IDP_ENTITY_ID = "testidp";

	protected static final String LOGIN_URL =
		"http://localhost:8080/c/portal/login";

	protected static final String LOGOUT_URL =
		"http://localhost:8080/c/portal/logout";

	protected static final String METADATA_URL =
		"http://localhost:8080/c/portal/saml/metadata";

	protected static final String PORTAL_URL = "http://localhost:8080";

	protected static final String RELAY_STATE =
		"http://localhost:8080/relaystate";

	protected static final long SESSION_ID = 2;

	protected static final String SLO_LOGOUT_URL =
		"http://localhost:8080/c/portal/saml/slo_logout";

	protected static final String SP_ENTITY_ID = "testsp";

	protected static final String SSO_URL =
		"http://localhost:8080/c/portal/saml/sso";

	protected static final String UNKNOWN_ENTITY_ID = "testunknown";

	protected KeyStoreCredentialResolver credentialResolver;
	protected FileSystemKeyStoreManagerImpl fileSystemKeyStoreManagerImpl;
	protected GroupLocalService groupLocalService;
	protected HttpClient httpClient;
	protected IdentifierGenerationStrategyFactory
		identifierGenerationStrategyFactory;
	protected List<String> identifiers = new ArrayList<>();
	protected LocalEntityManager localEntityManager;
	protected MetadataManagerImpl metadataManagerImpl;
	protected ParserPool parserPool;
	protected Portal portal;
	protected List<SamlBinding> samlBindings;
	protected IdentifierGenerationStrategy samlIdentifierGenerator;
	protected SamlPeerBindingLocalService samlPeerBindingLocalService;
	protected SamlProviderConfiguration samlProviderConfiguration;
	protected SamlProviderConfigurationHelper samlProviderConfigurationHelper;
	protected List<Class<?>> serviceUtilClasses = new ArrayList<>();
	protected UserLocalService userLocalService;

	protected class MockMetadataResolver extends AbstractMetadataResolver {

		public MockMetadataResolver() {
		}

		public MockMetadataResolver(boolean idpNeedsSignature) {
			_idpNeedsSignature = idpNeedsSignature;
		}

		@Override
		public Iterable<EntityDescriptor> resolve(CriteriaSet criteriaSet)
			throws ResolverException {

			try {
				return Collections.singleton(doResolve(criteriaSet));
			}
			catch (Exception exception) {
				throw new ResolverException(exception);
			}
		}

		protected EntityDescriptor doResolve(CriteriaSet criteriaSet)
			throws Exception {

			EntityIdCriterion entityIdCriterion = criteriaSet.get(
				EntityIdCriterion.class);

			if (entityIdCriterion == null) {
				throw new ResolverException("Entity ID criterion is null");
			}

			String entityId = entityIdCriterion.getEntityId();

			KeyStoreCredentialResolver keyStoreCredentialResolver =
				_getKeyStoreCredentialResolver(entityId);

			Credential credential = keyStoreCredentialResolver.resolveSingle(
				criteriaSet);

			if (entityId.equals(IDP_ENTITY_ID)) {
				EntityDescriptor entityDescriptor =
					MetadataGeneratorUtil.buildIdpEntityDescriptor(
						PORTAL_URL, entityId, _idpNeedsSignature, true,
						credential, null);

				IDPSSODescriptor idpSSODescriptor =
					entityDescriptor.getIDPSSODescriptor(
						SAMLConstants.SAML20P_NS);

				List<SingleLogoutService> singleLogoutServices =
					idpSSODescriptor.getSingleLogoutServices();

				for (SingleLogoutService singleLogoutService :
						singleLogoutServices) {

					String binding = singleLogoutService.getBinding();

					if (binding.equals(SAMLConstants.SAML2_POST_BINDING_URI)) {
						singleLogoutServices.remove(singleLogoutService);

						break;
					}
				}

				List<SingleSignOnService> singleSignOnServices =
					idpSSODescriptor.getSingleSignOnServices();

				for (SingleSignOnService singleSignOnService :
						singleSignOnServices) {

					String binding = singleSignOnService.getBinding();

					if (binding.equals(SAMLConstants.SAML2_POST_BINDING_URI)) {
						singleSignOnServices.remove(singleSignOnService);

						break;
					}
				}

				return entityDescriptor;
			}
			else if (entityId.equals(SP_ENTITY_ID)) {
				return MetadataGeneratorUtil.buildSpEntityDescriptor(
					PORTAL_URL, entityId, true, true, false, credential, null);
			}

			return null;
		}

		private boolean _idpNeedsSignature = true;

	}

	private KeyStoreCredentialResolver _getKeyStoreCredentialResolver(
		String entityId) {

		KeyStoreCredentialResolver keyStoreCredentialResolver =
			new KeyStoreCredentialResolver();

		keyStoreCredentialResolver.setKeyStoreManager(
			fileSystemKeyStoreManagerImpl);

		SamlProviderConfigurationHelper peerSamlProviderConfigurationHelper =
			Mockito.mock(SamlProviderConfigurationHelper.class);

		SamlProviderConfiguration peerSamlProviderConfiguration = Mockito.mock(
			SamlProviderConfiguration.class);

		Mockito.when(
			peerSamlProviderConfiguration.entityId()
		).thenReturn(
			entityId
		);

		String keyStoreCredentialPassword =
			samlProviderConfiguration.keyStoreCredentialPassword();

		Mockito.when(
			peerSamlProviderConfiguration.keyStoreCredentialPassword()
		).thenReturn(
			keyStoreCredentialPassword
		);

		Mockito.when(
			peerSamlProviderConfigurationHelper.getSamlProviderConfiguration()
		).thenReturn(
			peerSamlProviderConfiguration
		);

		keyStoreCredentialResolver.setSamlProviderConfigurationHelper(
			peerSamlProviderConfigurationHelper);

		return keyStoreCredentialResolver;
	}

	private <T> T _getMockService(
		Class<?> serviceUtilClass, Class<T> serviceClass) {

		serviceUtilClasses.add(serviceUtilClass);

		T serviceMock = Mockito.mock(serviceClass);

		ReflectionTestUtil.setFieldValue(
			serviceUtilClass, "_service", serviceMock);

		return serviceMock;
	}

	private void _setupConfiguration() {
		Thread currentThread = Thread.currentThread();

		ClassLoaderPool.register(
			"saml-portlet", currentThread.getContextClassLoader());

		PortletClassLoaderUtil.setServletContextName("saml-portlet");

		Configuration configuration = Mockito.mock(Configuration.class);

		ConfigurationFactory configurationFactory = Mockito.mock(
			ConfigurationFactory.class);

		ConfigurationFactoryUtil.setConfigurationFactory(configurationFactory);

		Mockito.when(
			configurationFactory.getConfiguration(
				Mockito.any(ClassLoader.class), Mockito.eq("portlet"))
		).thenReturn(
			configuration
		);

		Mockito.when(
			configurationFactory.getConfiguration(
				Mockito.any(ClassLoader.class), Mockito.eq("service"))
		).thenReturn(
			configuration
		);

		Mockito.when(
			configuration.get(PortletPropsKeys.SAML_KEYSTORE_MANAGER_IMPL)
		).thenReturn(
			FileSystemKeyStoreManagerImpl.class.getName()
		);

		samlProviderConfigurationHelper = Mockito.mock(
			SamlProviderConfigurationHelper.class);

		Mockito.when(
			samlProviderConfigurationHelper.isEnabled()
		).thenReturn(
			true
		);

		samlProviderConfiguration = Mockito.mock(
			SamlProviderConfiguration.class);

		Mockito.when(
			samlProviderConfiguration.defaultAssertionLifetime()
		).thenReturn(
			1800
		);

		Mockito.when(
			samlProviderConfigurationHelper.getSamlProviderConfiguration()
		).thenReturn(
			samlProviderConfiguration
		);

		Mockito.when(
			samlProviderConfiguration.enabled()
		).thenReturn(
			true
		);

		Mockito.when(
			samlProviderConfiguration.keyStoreCredentialPassword()
		).thenReturn(
			"liferay"
		);
	}

	private void _setupIdentifiers() {
		SamlIdentifierGeneratorStrategyFactory
			samlIdentifierGeneratorStrategyFactory =
				new SamlIdentifierGeneratorStrategyFactory();

		samlIdentifierGenerator = samlIdentifierGeneratorStrategyFactory.create(
			16);

		IdentifierGenerationStrategy identifierGenerationStrategy =
			Mockito.mock(IdentifierGenerationStrategy.class);

		identifierGenerationStrategyFactory = Mockito.mock(
			IdentifierGenerationStrategyFactory.class);

		Mockito.when(
			identifierGenerationStrategyFactory.create(Mockito.anyInt())
		).thenReturn(
			identifierGenerationStrategy
		);

		Mockito.when(
			identifierGenerationStrategy.generateIdentifier()
		).thenAnswer(
			(Answer<String>)invocationOnMock -> {
				String identifier =
					samlIdentifierGenerator.generateIdentifier();

				identifiers.add(identifier);

				return identifier;
			}
		);

		Mockito.when(
			identifierGenerationStrategy.generateIdentifier(
				Mockito.anyBoolean())
		).thenAnswer(
			(Answer<String>)invocationOnMock -> {
				boolean xmlSafe = GetterUtil.getBoolean(
					invocationOnMock.getArguments()[0]);

				String identifier = samlIdentifierGenerator.generateIdentifier(
					xmlSafe);

				identifiers.add(identifier);

				return identifier;
			}
		);
	}

	private void _setupMetadata() throws Exception {
		metadataManagerImpl = new MetadataManagerImpl();

		fileSystemKeyStoreManagerImpl = new FileSystemKeyStoreManagerImpl();

		ReflectionTestUtil.invoke(
			fileSystemKeyStoreManagerImpl, "activate",
			new Class<?>[] {Map.class},
			HashMapBuilder.<String, Object>put(
				"saml.keystore.path",
				"classpath:/com/liferay/saml/opensaml/integration/internal" +
					"/credential/dependencies/keystore.jks"
			).build());

		credentialResolver = new KeyStoreCredentialResolver();

		credentialResolver.setKeyStoreManager(fileSystemKeyStoreManagerImpl);

		credentialResolver.setSamlProviderConfigurationHelper(
			samlProviderConfigurationHelper);

		metadataManagerImpl.setCredentialResolver(credentialResolver);

		metadataManagerImpl.setParserPool(parserPool);

		metadataManagerImpl.setMetadataResolver(new MockMetadataResolver());

		metadataManagerImpl.setSamlProviderConfigurationHelper(
			samlProviderConfigurationHelper);

		metadataManagerImpl.setPortal(portal);

		metadataManagerImpl.setLocalEntityManager(credentialResolver);

		ReflectionTestUtil.invoke(
			metadataManagerImpl, "activate", new Class<?>[0]);
	}

	private void _setupParserPool() {
		XMLObjectProviderRegistry xmlObjectProviderRegistry =
			ConfigurationService.get(XMLObjectProviderRegistry.class);

		parserPool = xmlObjectProviderRegistry.getParserPool();
	}

	private void _setupPortal() throws Exception {
		httpClient = Mockito.mock(HttpClient.class);

		PortalUtil portalUtil = new PortalUtil();

		portal = Mockito.mock(Portal.class);

		portalUtil.setPortal(portal);

		Mockito.when(
			portal.getCompanyId(Mockito.any(HttpServletRequest.class))
		).thenReturn(
			COMPANY_ID
		);

		Mockito.when(
			portal.getPathContext()
		).thenReturn(
			""
		);

		Mockito.when(
			portal.getPathMain()
		).thenReturn(
			Portal.PATH_MAIN
		);

		Mockito.when(
			portal.getPortalURL(Mockito.any(MockHttpServletRequest.class))
		).thenReturn(
			PORTAL_URL
		);

		Mockito.when(
			portal.getPortalURL(
				Mockito.any(MockHttpServletRequest.class), Mockito.eq(false))
		).thenReturn(
			PORTAL_URL
		);

		Mockito.when(
			portal.getPortalURL(
				Mockito.any(MockHttpServletRequest.class), Mockito.eq(true))
		).thenReturn(
			StringUtil.replace(
				PORTAL_URL, new String[] {"http://", "https://"},
				new String[] {"8080", "8443"})
		);

		groupLocalService = getMockPortalService(
			GroupLocalServiceUtil.class, GroupLocalService.class);

		Group guestGroup = Mockito.mock(Group.class);

		Mockito.when(
			groupLocalService.getGroup(
				Mockito.anyLong(), Mockito.eq(GroupConstants.GUEST))
		).thenReturn(
			guestGroup
		);

		LayoutLocalService layoutLocalService = getMockPortalService(
			LayoutLocalServiceUtil.class, LayoutLocalService.class);

		Mockito.when(
			layoutLocalService.getDefaultPlid(
				Mockito.anyLong(), Mockito.anyBoolean())
		).thenReturn(
			1L
		);

		userLocalService = getMockPortalService(
			UserLocalServiceUtil.class, UserLocalService.class);
	}

	private void _setupProps() {
		PropsTestUtil.setProps(
			HashMapBuilder.<String, Object>put(
				PropsKeys.LIFERAY_HOME, System.getProperty("java.io.tmpdir")
			).put(
				"configuration.override.", new Properties()
			).build());
	}

	private void _setupSamlBindings() {
		VelocityEngineFactory velocityEngineFactory =
			new VelocityEngineFactory();

		Thread currentThread = Thread.currentThread();

		VelocityEngine velocityEngine = velocityEngineFactory.getVelocityEngine(
			currentThread.getContextClassLoader());

		samlBindings = new ArrayList<>();

		samlBindings.add(new HttpPostBinding(parserPool, velocityEngine));
		samlBindings.add(new HttpRedirectBinding(parserPool));
		samlBindings.add(new HttpSoap11Binding(parserPool, httpClient));
	}

	private void _setupSamlPeerBindingsLocalService() throws Exception {
		samlPeerBindingLocalService = getMockPortletService(
			SamlPeerBindingLocalServiceUtil.class,
			SamlPeerBindingLocalService.class);

		Mockito.when(
			samlPeerBindingLocalService.getSamlPeerBinding(Mockito.anyLong())
		).thenAnswer(
			answer -> _samlPeerBindings.get((long)answer.getArguments()[0])
		);
	}

	private final Map<Long, SamlPeerBinding> _samlPeerBindings =
		new HashMap<>();

}