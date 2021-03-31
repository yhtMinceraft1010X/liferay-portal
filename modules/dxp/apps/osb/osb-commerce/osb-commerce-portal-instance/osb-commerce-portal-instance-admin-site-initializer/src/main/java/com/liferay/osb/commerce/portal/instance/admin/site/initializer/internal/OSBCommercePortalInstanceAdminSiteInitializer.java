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

package com.liferay.osb.commerce.portal.instance.admin.site.initializer.internal;

import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.constants.FragmentPortletKeys;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryModel;
import com.liferay.fragment.service.FragmentCollectionLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateCollectionLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.util.LayoutCopyHelper;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 * @author Gianmarco Brunialti Masera
 */
@Component(
	immediate = true,
	property = "site.initializer.key=" + OSBCommercePortalInstanceAdminSiteInitializer.KEY,
	service = SiteInitializer.class
)
public class OSBCommercePortalInstanceAdminSiteInitializer
	implements SiteInitializer {

	public static final String KEY =
		"osb-commerce-portal-instance-admin-initializer";

	@Override
	public String getDescription(Locale locale) {
		return StringPool.BLANK;
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return _THEME_NAME;
	}

	@Override
	public String getThumbnailSrc() {
		return _servletContext.getContextPath() + "/images/thumbnail.png";
	}

	@Override
	public void initialize(long groupId) throws InitializationException {
		try {
			ServiceContext serviceContext = _getServiceContext(groupId);

			_updateLogo(serviceContext);
			_updateLookAndFeel(serviceContext);

			_initializeLayoutFragments(serviceContext);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new InitializationException(exception);
		}
	}

	@Override
	public boolean isActive(long companyId) {
		return true;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundle = bundleContext.getBundle();
	}

	private void _addFileEntries(
			long fragmentCollectionId, long folderId,
			ServiceContext serviceContext)
		throws Exception {

		Enumeration<URL> urlEnumeration = _bundle.findEntries(
			_PATH + "images", StringPool.STAR, false);

		while (urlEnumeration.hasMoreElements()) {
			URL url = urlEnumeration.nextElement();

			byte[] bytes = null;

			try (InputStream is = url.openStream()) {
				bytes = FileUtil.getBytes(is);
			}

			String fileName = FileUtil.getShortFileName(url.getPath());

			PortletFileRepositoryUtil.addPortletFileEntry(
				serviceContext.getScopeGroupId(), serviceContext.getUserId(),
				FragmentCollection.class.getName(), fragmentCollectionId,
				FragmentPortletKeys.FRAGMENT, folderId, bytes, fileName,
				MimeTypesUtil.getContentType(fileName), false);
		}
	}

	private FragmentCollection _addFragmentCollection(
			ServiceContext serviceContext)
		throws Exception {

		return _fragmentCollectionLocalService.addFragmentCollection(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			_THEME_NAME, null, serviceContext);
	}

	private List<FragmentEntry> _addFragmentEntries(
			long fragmentCollectionId, String path,
			ServiceContext serviceContext)
		throws Exception {

		List<FragmentEntry> fragmentEntries = new ArrayList<>();

		Enumeration<URL> enumeration = _bundle.findEntries(
			path, "*.html", false);

		while (enumeration.hasMoreElements()) {
			URL url = enumeration.nextElement();

			String shortFileName = FileUtil.getShortFileName(url.getPath());

			StringBundler sb = new StringBundler(4);

			sb.append(path);
			sb.append(StringPool.SLASH);
			sb.append(FileUtil.stripExtension(shortFileName));
			sb.append(".css");

			URL cssURL = _bundle.getEntry(sb.toString());

			FragmentEntry fragmentEntry =
				_fragmentEntryLocalService.addFragmentEntry(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(), fragmentCollectionId,
					null,
					StringUtil.upperCaseFirstLetter(
						FileUtil.stripExtension(shortFileName)),
					StringUtil.read(cssURL.openStream()),
					StringUtil.read(url.openStream()), StringPool.BLANK,
					StringPool.BLANK, 0, FragmentConstants.TYPE_SECTION,
					WorkflowConstants.STATUS_APPROVED, serviceContext);

			long fragmentEntryPreviewFileEntryId = _getPreviewFileEntryId(
				FragmentPortletKeys.FRAGMENT, FragmentEntry.class.getName(),
				fragmentEntry.getFragmentEntryId(), path, shortFileName,
				serviceContext);

			fragmentEntry = _fragmentEntryLocalService.updateFragmentEntry(
				fragmentEntry.getFragmentEntryId(),
				fragmentEntryPreviewFileEntryId);

			fragmentEntries.add(fragmentEntry);
		}

		return fragmentEntries;
	}

	private void _addLayout(
			FragmentCollection fragmentCollection,
			LayoutPageTemplateCollection layoutPageTemplateCollection,
			String fragmentName, String fragmentPath, boolean privateLayout,
			ServiceContext serviceContext)
		throws Exception {

		fragmentPath = StringBundler.concat(
			_PATH, "fragments/layouts/", privateLayout ? "private" : "public",
			"/", fragmentPath);

		List<FragmentEntry> fragmentEntry = _addFragmentEntries(
			fragmentCollection.getFragmentCollectionId(), fragmentPath,
			serviceContext);

		_addLayout(
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			fragmentName, fragmentEntry, fragmentPath, privateLayout,
			serviceContext);
	}

	private void _addLayout(
			long layoutPageTemplateCollectionId, String name,
			List<FragmentEntry> fragmentEntries, String path,
			boolean privateLayout, ServiceContext serviceContext)
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_addLayoutPageTemplateEntry(
				layoutPageTemplateCollectionId, name, path, serviceContext);

		long[] fragmentEntryIds = ListUtil.toLongArray(
			fragmentEntries, FragmentEntryModel::getFragmentEntryId);

		_layoutPageTemplateEntryLocalService.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), name,
			fragmentEntryIds, StringPool.BLANK, serviceContext);

		Layout layout = _layoutLocalService.addLayout(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			privateLayout, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			_portal.getClassNameId(LayoutPageTemplateEntry.class),
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
			HashMapBuilder.put(
				LocaleUtil.getSiteDefault(), name
			).build(),
			new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(),
			"content", null, false, false, new HashMap<>(), serviceContext);

		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				_copyLayout(layout);

				return null;
			});
	}

	private LayoutPageTemplateCollection _addLayoutPageTemplateCollection(
			ServiceContext serviceContext)
		throws Exception {

		return _layoutPageTemplateCollectionLocalService.
			addLayoutPageTemplateCollection(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				_THEME_NAME, _THEME_NAME, serviceContext);
	}

	private LayoutPageTemplateEntry _addLayoutPageTemplateEntry(
			long layoutPageTemplateCollectionId, String name, String path,
			ServiceContext serviceContext)
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				layoutPageTemplateCollectionId, name,
				LayoutPageTemplateEntryTypeConstants.TYPE_BASIC,
				WorkflowConstants.STATUS_APPROVED, serviceContext);

		long previewFileEntryId = _getPreviewFileEntryId(
			LayoutAdminPortletKeys.GROUP_PAGES,
			LayoutPageTemplateEntry.class.getName(),
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), path,
			"thumbnail.jpg", serviceContext);

		return _layoutPageTemplateEntryLocalService.
			updateLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				previewFileEntryId);
	}

	private void _copyLayout(Layout layout) throws Exception {
		Layout draftLayout = _layoutLocalService.fetchLayout(
			_portal.getClassNameId(Layout.class), layout.getPlid());

		if (draftLayout != null) {
			_layoutCopyHelper.copyLayout(draftLayout, layout);
		}

		_layoutLocalService.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			new Date());
	}

	private long _getPreviewFileEntryId(
			String portletId, String className, long classPK, String path,
			String fileName, ServiceContext serviceContext)
		throws Exception {

		StringBundler sb = new StringBundler(4);

		sb.append(path);
		sb.append(StringPool.SLASH);
		sb.append(StringUtil.split(fileName, StringPool.PERIOD)[0]);
		sb.append(".jpg");

		URL url = _bundle.getEntry(sb.toString());

		if (url == null) {
			return 0;
		}

		Repository repository =
			PortletFileRepositoryUtil.fetchPortletRepository(
				serviceContext.getScopeGroupId(), portletId);

		if (repository == null) {
			repository = PortletFileRepositoryUtil.addPortletRepository(
				serviceContext.getScopeGroupId(), portletId, serviceContext);
		}

		String imageFileName =
			classPK + "_preview." + FileUtil.getExtension(url.getPath());

		byte[] bytes = null;

		try (InputStream is = url.openStream()) {
			bytes = FileUtil.getBytes(is);
		}

		FileEntry fileEntry = PortletFileRepositoryUtil.addPortletFileEntry(
			serviceContext.getScopeGroupId(), serviceContext.getUserId(),
			className, classPK, portletId, repository.getDlFolderId(), bytes,
			imageFileName, MimeTypesUtil.getContentType(imageFileName), false);

		return fileEntry.getFileEntryId();
	}

	private ServiceContext _getServiceContext(long groupId)
		throws PortalException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		Group group = _groupLocalService.getGroup(groupId);

		serviceContext.setCompanyId(group.getCompanyId());

		Locale locale = LocaleUtil.getSiteDefault();

		serviceContext.setLanguageId(LanguageUtil.getLanguageId(locale));

		serviceContext.setScopeGroupId(groupId);

		User user = _userLocalService.getUser(PrincipalThreadLocal.getUserId());

		serviceContext.setTimeZone(user.getTimeZone());
		serviceContext.setUserId(user.getUserId());

		return serviceContext;
	}

	private void _initializeLayoutFragments(ServiceContext serviceContext)
		throws Exception {

		FragmentCollection fragmentCollection = _addFragmentCollection(
			serviceContext);

		_addFileEntries(
			fragmentCollection.getFragmentCollectionId(),
			fragmentCollection.getResourcesFolderId(), serviceContext);

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			_addLayoutPageTemplateCollection(serviceContext);

		_addLayout(
			fragmentCollection, layoutPageTemplateCollection, "Dashboard",
			"dashboard", true, serviceContext);
	}

	private void _updateLogo(ServiceContext serviceContext) throws Exception {
		URL url = _bundle.getEntry(_PATH + "images/logo.png");

		byte[] bytes = null;

		try (InputStream is = url.openStream()) {
			bytes = FileUtil.getBytes(is);
		}

		_layoutSetLocalService.updateLogo(
			serviceContext.getScopeGroupId(), false, true, bytes);
	}

	private void _updateLookAndFeel(ServiceContext serviceContext)
		throws PortalException {

		Theme theme = _themeLocalService.fetchTheme(
			serviceContext.getCompanyId(), _THEME_ID);

		if (theme == null) {
			if (_log.isInfoEnabled()) {
				_log.info("No theme found for " + _THEME_ID);
			}

			return;
		}

		_layoutSetLocalService.updateLookAndFeel(
			serviceContext.getScopeGroupId(), _THEME_ID, StringPool.BLANK,
			StringPool.BLANK);
	}

	private static final String _PATH =
		"com/liferay/osb/commerce/portal/instance/admin/site/initializer" +
			"/internal/dependencies/";

	private static final String _THEME_ID =
		"osbcommerceportalinstanceadmintheme_WAR_" +
			"osbcommerceportalinstanceadmintheme";

	private static final String _THEME_NAME =
		"OSB Commerce Portal Instance Admin";

	private static final Log _log = LogFactoryUtil.getLog(
		OSBCommercePortalInstanceAdminSiteInitializer.class);

	private Bundle _bundle;

	@Reference
	private FragmentCollectionLocalService _fragmentCollectionLocalService;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LayoutCopyHelper _layoutCopyHelper;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateCollectionLocalService
		_layoutPageTemplateCollectionLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.osb.commerce.portal.instance.admin.site.initializer)"
	)
	private ServletContext _servletContext;

	@Reference
	private ThemeLocalService _themeLocalService;

	@Reference
	private UserLocalService _userLocalService;

}