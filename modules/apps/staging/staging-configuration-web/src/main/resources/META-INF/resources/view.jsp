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
GroupDisplayContextHelper groupDisplayContextHelper = new GroupDisplayContextHelper(request);

liveGroup = groupDisplayContextHelper.getLiveGroup();
liveGroupId = groupDisplayContextHelper.getLiveGroupId();

UnicodeProperties liveGroupTypeSettings = liveGroup.getTypeSettingsProperties();

LayoutSet privateLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(liveGroup.getGroupId(), true);
LayoutSet publicLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(liveGroup.getGroupId(), false);

boolean liveGroupRemoteStaging = liveGroup.hasRemoteStagingGroup() && PropsValues.STAGING_LIVE_GROUP_REMOTE_STAGING_ENABLED;

boolean stagedLocally = liveGroup.isStaged() && !liveGroup.isStagedRemotely();

boolean stagedRemotely = liveGroup.isStaged() && !stagedLocally;

if (stagedLocally) {
	stagingGroup = liveGroup.getStagingGroup();

	stagingGroupId = stagingGroup.getGroupId();
}

BackgroundTask lastCompletedInitialPublicationBackgroundTask = BackgroundTaskManagerUtil.fetchFirstBackgroundTask(liveGroupId, BackgroundTaskExecutorNames.LAYOUT_STAGING_BACKGROUND_TASK_EXECUTOR, true, new BackgroundTaskCreateDateComparator(false));
%>

<c:choose>
	<c:when test="<%= GroupPermissionUtil.contains(permissionChecker, liveGroup, ActionKeys.MANAGE_STAGING) && GroupPermissionUtil.contains(permissionChecker, liveGroup, ActionKeys.VIEW_STAGING) %>">
		<%@ include file="/staging_configuration_exceptions.jspf" %>

		<clay:container-fluid
			cssClass="main-content-body mt-4"
		>
			<liferay-ui:breadcrumb
				showLayout="<%= false %>"
			/>

			<clay:sheet
				cssClass="custom-sheet"
			>
				<liferay-ui:success key="stagingDisabled" message="staging-is-successfully-disabled" />

				<liferay-ui:success key="localStagingModified" message="local-staging-configuration-is-successfully-modified" />

				<liferay-ui:success key="remoteStagingModified" message="remote-staging-configuration-is-successfully-modified" />

				<portlet:actionURL name="editStagingConfiguration" var="editStagingConfigurationURL">
					<portlet:param name="mvcPath" value="/view.jsp" />
				</portlet:actionURL>

				<portlet:renderURL var="redirectURL">
					<portlet:param name="mvcRenderCommandName" value="/staging_configuration/view" />
				</portlet:renderURL>

				<aui:form action="<%= editStagingConfigurationURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "saveGroup();" %>'>
					<aui:input name="redirect" type="hidden" value="<%= redirectURL %>" />
					<aui:input name="groupId" type="hidden" value="<%= liveGroupId %>" />
					<aui:input name="liveGroupId" type="hidden" value="<%= liveGroupId %>" />
					<aui:input name="stagingGroupId" type="hidden" value="<%= stagingGroupId %>" />
					<aui:input name="forceDisable" type="hidden" value="<%= false %>" />

					<c:if test="<%= !privateLayoutSet.isLayoutSetPrototypeLinkActive() && !publicLayoutSet.isLayoutSetPrototypeLinkActive() %>">
						<clay:sheet-header>
							<div class="sheet-title">
								<liferay-ui:message key="javax.portlet.title.com_liferay_staging_configuration_web_portlet_StagingConfigurationPortlet" />
							</div>
						</clay:sheet-header>

						<%@ include file="/staging_configuration_select_staging_type.jspf" %>

						<%@ include file="/staging_configuration_remote_options.jspf" %>

						<%@ include file="/staging_configuration_staged_portlets.jspf" %>

						<clay:sheet-footer>
							<div class="btn-group-item">
								<div class="btn-group-item">
									<button class="btn btn-primary">
										<span class="lfr-btn-label">
											<%= LanguageUtil.get(request, "save") %>
										</span>
									</button>
								</div>
							</div>
						</clay:sheet-footer>

						<aui:script require="frontend-js-web/liferay/delegate/delegate.es as delegateModule">
							var pwcWarning = document.getElementById('<portlet:namespace />pwcWarning');
							var remoteStagingOptions = document.getElementById(
								'<portlet:namespace />remoteStagingOptions'
							);
							var stagedPortlets = document.getElementById(
								'<portlet:namespace />stagedPortlets'
							);
							var trashWarning = document.getElementById('<portlet:namespace />trashWarning');
							var stagingTypes = document.getElementById('<portlet:namespace />stagingTypes');

							if (
								stagingTypes &&
								pwcWarning &&
								stagedPortlets &&
								remoteStagingOptions &&
								trashWarning
							) {
								var delegate = delegateModule.default;

								delegate(stagingTypes, 'click', 'input', (event) => {
									var value = event.target.closest('input').value;

									if (value != '<%= StagingConstants.TYPE_LOCAL_STAGING %>') {
										pwcWarning.classList.add('hide');
									}
									else {
										pwcWarning.classList.remove('hide');
									}

									if (value == '<%= StagingConstants.TYPE_NOT_STAGED %>') {
										stagedPortlets.classList.add('hide');
									}
									else {
										stagedPortlets.classList.remove('hide');
									}

									if (value != '<%= StagingConstants.TYPE_REMOTE_STAGING %>') {
										remoteStagingOptions.classList.add('hide');
									}
									else {
										remoteStagingOptions.classList.remove('hide');
									}

									if (value != '<%= StagingConstants.TYPE_LOCAL_STAGING %>') {
										trashWarning.classList.add('hide');
									}
									else {
										trashWarning.classList.remove('hide');
									}
								});
							}
						</aui:script>
					</c:if>
				</aui:form>
			</clay:sheet>
		</clay:container-fluid>
	</c:when>
	<c:otherwise>
		<liferay-staging:alert
			type="INFO"
		>
			<liferay-ui:message key="you-do-not-have-permission-to-manage-settings-related-to-staging" />
		</liferay-staging:alert>
	</c:otherwise>
</c:choose>

<script>
	function <portlet:namespace />saveGroup(forceDisable) {
		var form = document.<portlet:namespace />fm;
		var ok = true;

		<c:if test="<%= liveGroup != null %>">
			var oldValue;

			<c:choose>
				<c:when test="<%= liveGroup.isStaged() && !liveGroup.isStagedRemotely() %>">
					oldValue = <%= StagingConstants.TYPE_LOCAL_STAGING %>;
				</c:when>
				<c:when test="<%= liveGroup.isStaged() && liveGroup.isStagedRemotely() %>">
					oldValue = <%= StagingConstants.TYPE_REMOTE_STAGING %>;
				</c:when>
				<c:otherwise>
					oldValue = <%= StagingConstants.TYPE_NOT_STAGED %>;
				</c:otherwise>
			</c:choose>

			var selectedStagingTypeInput = document.querySelector(
				'input[name=<portlet:namespace />stagingType]:checked'
			);

			if (forceDisable || selectedStagingTypeInput) {
				var currentValue = selectedStagingTypeInput.value;

				if (forceDisable) {
					currentValue = <%= StagingConstants.TYPE_NOT_STAGED %>;
				}

				if (currentValue != oldValue) {
					ok = false;

					if (currentValue == <%= StagingConstants.TYPE_NOT_STAGED %>) {
						ok = confirm(
							'<%= UnicodeLanguageUtil.format(request, "are-you-sure-you-want-to-deactivate-staging-for-x", liveGroup.getDescriptiveName(locale), false) %>'
						);
					}
					else if (
						currentValue == <%= StagingConstants.TYPE_LOCAL_STAGING %>
					) {
						ok = confirm(
							'<%= UnicodeLanguageUtil.format(request, "are-you-sure-you-want-to-activate-local-staging-for-x", liveGroup.getDescriptiveName(locale), false) %>'
						);
					}
					else if (
						currentValue == <%= StagingConstants.TYPE_REMOTE_STAGING %>
					) {
						ok = confirm(
							'<%= UnicodeLanguageUtil.format(request, "are-you-sure-you-want-to-activate-remote-staging-for-x", liveGroup.getDescriptiveName(locale), false) %>'
						);
					}
				}
			}
		</c:if>

		if (ok) {
			if (forceDisable) {
				form.elements['<portlet:namespace />forceDisable'].value = true;
				form.elements[
					'<portlet:namespace />stagingType'
				].value = <%= StagingConstants.TYPE_NOT_STAGED %>;
			}

			submitForm(form);
		}
	}

	(function () {
		var allCheckboxes = document.querySelectorAll(
			'#stagingConfigurationControls input[type=checkbox]'
		);
		var selectAllCheckbox = document.getElementById(
			'<portlet:namespace />selectAllCheckbox'
		);

		if (selectAllCheckbox) {
			selectAllCheckbox.addEventListener('change', () => {
				Array.prototype.forEach.call(allCheckboxes, (checkbox) => {
					checkbox.checked = selectAllCheckbox.checked;
				});
			});
		}
	})();
</script>