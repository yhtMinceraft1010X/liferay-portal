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

<div class="container product-publisher-container">
	<react:component
		data="<%= (Map<String, Object>)request.getAttribute(OSBCommerceProvisioningThemeWebKeys.OSB_COMMERCE_PROVISIONING_THEME_CP_ENTRIES_MAP) %>"
		module="js/components/list_renderer/ListRendererWrapper"
	/>
</div>