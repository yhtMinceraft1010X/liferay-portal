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

package com.liferay.layout.page.template.admin.web.internal.portlet.action;

import com.liferay.layout.page.template.admin.constants.LayoutPageTemplateAdminPortletKeys;
import com.liferay.layout.page.template.admin.web.internal.handler.LayoutPageTemplateEntryExceptionRequestHandler;
import com.liferay.layout.page.template.exception.LayoutPageTemplateEntryNameException;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.portal.kernel.exception.LayoutNameException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.LayoutPrototypeService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LayoutPageTemplateAdminPortletKeys.LAYOUT_PAGE_TEMPLATES,
		"mvc.command.name=/layout_page_template_admin/add_layout_prototype"
	},
	service = MVCActionCommand.class
)
public class AddLayoutPrototypeMVCActionCommand extends BaseMVCActionCommand {

	protected LayoutPrototype addLayoutPrototype(ActionRequest actionRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String name = ParamUtil.getString(actionRequest, "name");

		Locale defaultLocale = LocaleUtil.getDefault();

		Map<Locale, String> nameMap = HashMapBuilder.put(
			themeDisplay.getSiteDefaultLocale(), name
		).put(
			defaultLocale,
			() -> {
				if (themeDisplay.getSiteDefaultLocale() != defaultLocale) {
					return name;
				}

				return null;
			}
		).build();

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			LayoutPrototype.class.getName(), actionRequest);

		LayoutPrototype layoutPrototype =
			_layoutPrototypeService.addLayoutPrototype(
				nameMap, new HashMap<>(), true, serviceContext);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.
				fetchFirstLayoutPageTemplateEntry(
					layoutPrototype.getLayoutPrototypeId());

		if (layoutPageTemplateEntry == null) {
			return layoutPrototype;
		}

		layoutPageTemplateEntry.setGroupId(themeDisplay.getScopeGroupId());
		layoutPageTemplateEntry.setLayoutPageTemplateCollectionId(
			ParamUtil.getLong(actionRequest, "layoutPageTemplateCollectionId"));

		_layoutPageTemplateEntryLocalService.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry);

		return layoutPrototype;
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Callable<LayoutPrototype> addLayoutPrototypeCallable =
			new AddLayoutPrototypeCallable(actionRequest);

		try {
			LayoutPrototype layoutPrototype = TransactionInvokerUtil.invoke(
				_transactionConfig, addLayoutPrototypeCallable);

			Group layoutPrototypeGroup = layoutPrototype.getGroup();

			String redirectURL = layoutPrototypeGroup.getDisplayURL(
				themeDisplay, true);

			String backURL = ParamUtil.getString(actionRequest, "backURL");

			if (Validator.isNotNull(backURL)) {
				redirectURL = HttpComponentsUtil.setParameter(
					redirectURL, "p_l_back_url", backURL);
			}

			JSONPortletResponseUtil.writeJSON(
				actionRequest, actionResponse,
				JSONUtil.put("redirectURL", redirectURL));
		}
		catch (Throwable throwable) {
			if (_log.isDebugEnabled()) {
				_log.debug(throwable, throwable);
			}

			if (throwable instanceof LayoutNameException) {
				JSONPortletResponseUtil.writeJSON(
					actionRequest, actionResponse,
					JSONUtil.put(
						"error",
						LanguageUtil.get(
							themeDisplay.getRequest(),
							"please-enter-a-valid-name")));
			}
			else if (throwable instanceof
						LayoutPageTemplateEntryNameException) {

				LayoutPageTemplateEntryNameException
					layoutPageTemplateEntryNameException =
						(LayoutPageTemplateEntryNameException)throwable;

				_layoutPageTemplateEntryExceptionRequestHandler.
					handlePortalException(
						actionRequest, actionResponse,
						layoutPageTemplateEntryNameException);
			}
			else {
				JSONPortletResponseUtil.writeJSON(
					actionRequest, actionResponse,
					JSONUtil.put(
						"error",
						LanguageUtil.get(
							themeDisplay.getRequest(),
							"an-unexpected-error-occurred")));
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AddLayoutPrototypeMVCActionCommand.class);

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private LayoutPageTemplateEntryExceptionRequestHandler
		_layoutPageTemplateEntryExceptionRequestHandler;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutPrototypeService _layoutPrototypeService;

	@Reference
	private Portal _portal;

	private class AddLayoutPrototypeCallable
		implements Callable<LayoutPrototype> {

		@Override
		public LayoutPrototype call() throws Exception {
			return addLayoutPrototype(_actionRequest);
		}

		private AddLayoutPrototypeCallable(ActionRequest actionRequest) {
			_actionRequest = actionRequest;
		}

		private final ActionRequest _actionRequest;

	}

}