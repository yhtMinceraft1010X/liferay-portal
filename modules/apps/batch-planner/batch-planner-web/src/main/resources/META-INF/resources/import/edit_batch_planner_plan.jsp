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

<%@ include file="/init.jsp" %>

<%
String backURL = ParamUtil.getString(request, "backURL", String.valueOf(renderResponse.createRenderURL()));

long batchPlannerPlanId = ParamUtil.getLong(renderRequest, "batchPlannerPlanId");

BatchPlannerPlan batchPlannerPlan = BatchPlannerPlanServiceUtil.fetchBatchPlannerPlan(batchPlannerPlanId);

renderResponse.setTitle((batchPlannerPlan == null) ? LanguageUtil.get(request, "import") : LanguageUtil.get(request, "edit"));
%>

<div class="container pt-4">
	<form id="<portlet:namespace />fm" name="<portlet:namespace />fm" >
		<aui:input name="batchPlannerPlanId" type="hidden" value="<%= batchPlannerPlanId %>" />
		<aui:input name="taskItemDelegateName" type="hidden" value="DEFAULT" />

		<div class="card">
			<h4 class="card-header"><%= LanguageUtil.get(request, "import-settings") %></h4>

			<div class="card-body">
				<liferay-frontend:edit-form-body>
					<div class="form-group">
						<label for="<portlet:namespace />name"><%= LanguageUtil.get(request, "template-name") %></label>

						<input class="form-control" id="<portlet:namespace />name" type="text" />
					</div>

					<aui:select bean="<%= batchPlannerPlan %>" model="<%= BatchPlannerPlan.class %>" name="externalType">
						<aui:option label="CSV" value="CSV" />
						<aui:option label="JSON" value="JSON" />
						<aui:option label="JSONL" value="JSONL" />
						<aui:option label="TXT" value="TXT" />
						<aui:option label="XLS" value="XLS" />
						<aui:option label="XML" value="XML" />
					</aui:select>

					<aui:input name="importFile" required="<%= true %>" type="file" />

					<%
					EditBatchPlannerPlanDisplayContext editBatchPlannerPlanDisplayContext = (EditBatchPlannerPlanDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
					%>

					<clay:row>
						<clay:col
							md="6"
						>
							<clay:select
								id='<%= liferayPortletResponse.getNamespace() + "headlessEndpoint" %>'
								label="headless-endpoint"
								name="headlessEndpoint"
								options="<%= editBatchPlannerPlanDisplayContext.getSelectOptions() %>"
							/>
						</clay:col>

						<clay:col
							md="6"
						>
							<clay:select
								disabled="<%= true %>"
								id='<%= liferayPortletResponse.getNamespace() + "internalClassName" %>'
								label="internal-class-name"
								name="internalClassName"
								options="<%= editBatchPlannerPlanDisplayContext.getSelectOptions() %>"
							/>
						</clay:col>
					</clay:row>

					<clay:content-section>
						<clay:row>
							<clay:col
								md="6"
							>
								<clay:checkbox
									checked="<%= true %>"
									id='<%= liferayPortletResponse.getNamespace() + "containsHeaders" %>'
									label="contains-headers"
									name='<%= liferayPortletResponse.getNamespace() + "containsHeaders" %>'
								/>
							</clay:col>
						</clay:row>
					</clay:content-section>
				</liferay-frontend:edit-form-body>
			</div>
		</div>
		<span>
			<react:component
				module="js/import/ImportForm"
				props='<%=
					HashMapBuilder.<String, Object>put(
						"backUrl",
						backURL
					).put(
						"formDataQuerySelector", "#" + liferayPortletResponse.getNamespace() + "fm"
					).put(
						"formImportURL",
						PortletURLBuilder.createActionURL(
							renderResponse
						).setActionName(
							"/batch_planner/edit_import_batch_planner_plan"
						).setCMD(
							(batchPlannerPlanId == 0) ? Constants.IMPORT : Constants.UPDATE
						).setRedirect(
							backURL
						).buildString()
					).put(
						"formSaveAsTemplateURL",
						ResourceURLBuilder.createResourceURL(
							renderResponse
						).setCMD(
							Constants.SAVE
						).setParameter(
							"template", true
						).setResourceID(
							"/batch_planner/edit_import_batch_planner_plan"
						).buildString()
					).build()
				%>'
			/>
		</span>
	</form>
</div>


<liferay-frontend:component
	module="js/edit_batch_planner_plan"
/>