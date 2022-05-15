<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	redirect = PortletURLBuilder.createRenderURL(
		renderResponse
	).setMVCRenderCommandName(
		"/sxp_blueprint_admin/view_sxp_blueprints"
	).buildString();
}

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(LanguageUtil.get(request, "edit-blueprint"));
%>

<div>
	<span aria-hidden="true" class="loading-animation"></span>

	<react:component
		module="sxp_blueprint_admin/js/edit_sxp_blueprint/index"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"contextPath", application.getContextPath()
			).put(
				"defaultLocale", LocaleUtil.toLanguageId(LocaleUtil.getDefault())
			).put(
				"jsonAutocompleteEnabled", GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-148749"))
			).put(
				"learnMessages", LearnMessageUtil.getJSONObject("search-experiences-web")
			).put(
				"locale", themeDisplay.getLanguageId()
			).put(
				"namespace", liferayPortletResponse.getNamespace()
			).put(
				"redirectURL", redirect
			).put(
				"sxpBlueprintId", ParamUtil.getLong(renderRequest, "sxpBlueprintId")
			).build()
		%>'
	/>
</div>