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

<div id="osb-checkout-summary">
	<commerce-ui:mini-cart
		toggleable="<%= false %>"
		views='<%=
			HashMapBuilder.<String, String>put(
				"Cart", "osb-commerce-provisioning-theme-impl@1.0.0/js/components/cart_summary/CartSummary"
			).build()
		%>'
	/>
</div>