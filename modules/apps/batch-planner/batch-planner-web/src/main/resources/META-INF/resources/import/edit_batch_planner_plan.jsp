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
long batchPlannerPlanId = ParamUtil.getLong(renderRequest, "batchPlannerPlanId");

boolean editable = ParamUtil.getBoolean(renderRequest, "editable");

EditBatchPlannerPlanDisplayContext editBatchPlannerPlanDisplayContext = (EditBatchPlannerPlanDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(ParamUtil.getString(request, "backURL", String.valueOf(renderResponse.createRenderURL())));

renderResponse.setTitle(editable ? LanguageUtil.get(request, "edit-template") : LanguageUtil.get(request, "import"));
%>

<clay:container
	cssClass="container pt-4"
>
	<form id="<portlet:namespace />fm" name="<portlet:namespace />fm">
		<input id="<portlet:namespace />batchPlannerPlanId" name="<portlet:namespace />batchPlannerPlanId" type="hidden" value="<%= batchPlannerPlanId %>" />
		<input id="<portlet:namespace />externalType" name="<portlet:namespace />externalType" type="hidden" value="" />
		<input id="<portlet:namespace />taskItemDelegateName" name="<portlet:namespace />taskItemDelegateName" type="hidden" value="DEFAULT" />

		<div class="row">
			<div class="col-lg-6 d-flex flex-column">
				<div class="card flex-fill">
					<h4 class="card-header"><%= LanguageUtil.get(request, "import-settings") %></h4>

					<div class="card-body">
						<liferay-frontend:edit-form-body>
							<aui:input name="name" />

							<clay:row>
								<clay:col
									md="6"
								>
									<div id="<portlet:namespace />templateSelect"></div>
								</clay:col>

								<clay:col
									md="6"
								>
									<clay:select
										id='<%= liferayPortletResponse.getNamespace() + "internalClassName" %>'
										label='<%= LanguageUtil.get(request, "entity-type") %>'
										name="internalClassName"
										options="<%= editBatchPlannerPlanDisplayContext.getInternalClassNameSelectOptions() %>"
									/>
								</clay:col>
							</clay:row>

							<clay:alert
								displayType="info"
								title="download-a-sample-file-for-this-entity"
							>
								<clay:link
									cssClass="link-primary single-link"
									disabled="<%= true %>"
									href="#"
									id='<%= liferayPortletResponse.getNamespace() + "downloadBatchPlannerPlanTemplate" %>'
									label="download"
								/>
							</clay:alert>

							<liferay-frontend:component
								context='<%=
									HashMapBuilder.<String, Object>put(
										"externalReferenceCode", liferayPortletResponse.getNamespace() + "internalClassName"
									).put(
										"HTMLElementId", liferayPortletResponse.getNamespace() + "downloadBatchPlannerPlanTemplate"
									).put(
										"type", "batchPlannerTemplate"
									).build()
								%>'
								module="js/DownloadHelper"
							/>

							<div class="mt-2">
								<clay:checkbox
									checked="<%= false %>"
									disabled="<%= true %>"
									label='<%= LanguageUtil.get(request, "detect-category-names-from-CSV-file") %>'
									name="headerCheckbox"
								/>
							</div>

							<div class="mt-2">
								<clay:checkbox
									checked="<%= false %>"
									id='<%= liferayPortletResponse.getNamespace() + "allowUpdate" %>'
									label='<%= LanguageUtil.get(request, "override-existing-records") %>'
									name='<%= liferayPortletResponse.getNamespace() + "allowUpdate" %>'
								/>
							</div>

							<div class="mt-2">
								<clay:checkbox
									checked="<%= true %>"
									disabled="<%= true %>"
									id='<%= liferayPortletResponse.getNamespace() + "onUpdateDoPatch" %>'
									label='<%= LanguageUtil.get(request, "ignore-blank-field-values-during-import") %>'
									name='<%= liferayPortletResponse.getNamespace() + "onUpdateDoPatch" %>'
								/>
							</div>

							<div class="mt-2">
								<clay:checkbox
									checked="<%= true %>"
									id='<%= liferayPortletResponse.getNamespace() + "onErrorFail" %>'
									label='<%= LanguageUtil.get(request, "stop-the-import-on-error") %>'
									name='<%= liferayPortletResponse.getNamespace() + "onErrorFail" %>'
								/>
							</div>
						</liferay-frontend:edit-form-body>
					</div>
				</div>
			</div>

			<div class="col-lg-6 d-flex flex-column">
				<div class="card flex-fill">
					<h4 class="card-header"><%= LanguageUtil.get(request, "file-settings") %></h4>

					<div class="card-body">
						<liferay-frontend:edit-form-body>
							<div id="<portlet:namespace />fileSettings"></div>

							<div class="form-group">
								<clay:radio
									checked="<%= true %>"
									label='<%= LanguageUtil.get(request, "upload-a-file-from-my-computer") %>'
									name="selectFile"
									value="computer"
								/>

								<clay:radio
									disabled="<%= true %>"
									label='<%= LanguageUtil.get(request, "use-a-file-already-on-the-server") %>'
									name="selectFile"
									value="server"
								/>
							</div>

							<div id="<portlet:namespace />fileUpload">
								<react:component
									module="js/components/FileUpload"
								/>
							</div>
						</liferay-frontend:edit-form-body>
					</div>
				</div>
			</div>
		</div>

		<span>
			<react:component
				module="js/import/ImportForm"
				props='<%=
					HashMapBuilder.<String, Object>put(
						"formDataQuerySelector", "#" + liferayPortletResponse.getNamespace() + "fm"
					).put(
						"formImportURL",
						ResourceURLBuilder.createResourceURL(
							renderResponse
						).setCMD(
							Constants.IMPORT
						).setResourceID(
							"/batch_planner/submit_batch_planner_plan"
						).buildString()
					).put(
						"formSaveAsTemplateURL",
						ActionURLBuilder.createActionURL(
							renderResponse
						).setActionName(
							"/batch_planner/edit_import_batch_planner_plan_template"
						).setCMD(
							Constants.ADD
						).setParameter(
							"template", true
						).buildString()
					).put(
						"mappedFields", editBatchPlannerPlanDisplayContext.getSelectedBatchPlannerPlanMappings()
					).build()
				%>'
			/>
		</span>
	</form>
</clay:container>

<liferay-frontend:component
	context='<%=
		HashMapBuilder.<String, Object>put(
			"initialTemplateClassName", editBatchPlannerPlanDisplayContext.getSelectedInternalClassName()
		).put(
			"initialTemplateMapping", editBatchPlannerPlanDisplayContext.getSelectedBatchPlannerPlanMappings()
		).put(
			"isExport", false
		).put(
			"templatesOptions", editBatchPlannerPlanDisplayContext.getTemplateSelectOptions()
		).build()
	%>'
	module="js/edit_batch_planner_plan"
/>

<liferay-frontend:component
	module="js/show_upload_input"
/>