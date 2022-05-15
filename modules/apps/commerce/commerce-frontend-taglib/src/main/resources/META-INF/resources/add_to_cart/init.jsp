<%--
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
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.commerce.frontend.model.ProductSettingsModel" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.json.JSONFactoryUtil" %><%@
page import="com.liferay.portal.kernel.json.JSONSerializer" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.PortalUtil" %>

<liferay-theme:defineObjects />

<%
String alignment = (String)request.getAttribute("liferay-commerce:add-to-cart:alignment");
String disabled = (String)request.getAttribute("liferay-commerce:add-to-cart:disabled");
String commerceAccountId = (String)request.getAttribute("liferay-commerce:add-to-cart:commerceAccountId");
String commerceChannelGroupId = (String)request.getAttribute("liferay-commerce:add-to-cart:commerceChannelGroupId");
String commerceChannelId = (String)request.getAttribute("liferay-commerce:add-to-cart:commerceChannelId");
String commerceCurrencyCode = (String)request.getAttribute("liferay-commerce:add-to-cart:commerceCurrencyCode");
String commerceOrderId = (String)request.getAttribute("liferay-commerce:add-to-cart:commerceOrderId");
String cpInstanceId = (String)request.getAttribute("liferay-commerce:add-to-cart:cpInstanceId");
String iconOnly = (String)request.getAttribute("liferay-commerce:add-to-cart:iconOnly");
String inCart = (String)request.getAttribute("liferay-commerce:add-to-cart:inCart");
String inline = (String)request.getAttribute("liferay-commerce:add-to-cart:inline");
String namespace = (String)request.getAttribute("liferay-commerce:add-to-cart:namespace");
ProductSettingsModel productSettingsModel = (ProductSettingsModel)request.getAttribute("liferay-commerce:add-to-cart:productSettingsModel");
String size = (String)request.getAttribute("liferay-commerce:add-to-cart:size");
String skuOptions = (String)request.getAttribute("liferay-commerce:add-to-cart:skuOptions");
String stockQuantity = (String)request.getAttribute("liferay-commerce:add-to-cart:stockQuantity");

String randomNamespace = PortalUtil.generateRandomKey(request, "taglib") + StringPool.UNDERLINE;

String addToCartId = randomNamespace + "add_to_cart";
%>