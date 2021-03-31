<#if !trial_started && !is_signed_in>
	<#assign
		trial_navigation_preferences = freeMarkerPortletPreferences.getPreferences({
			"portletSetupPortletDecoratorId": "barebone",
			"displayStyle": "ddmTemplate_NAV-PILLS-FTL"
		})
	/>

	<button aria-controls="navigationCollapse" aria-expanded="false" aria-label="Toggle navigation" class="d-md-none navbar-toggler navbar-toggler-right" data-target="#navigationCollapse" data-toggle="collapse" type="button">
		<span class="navbar-toggler-icon"></span>
	</button>

	<div aria-expanded="false" class="collapse navbar-collapse" id="navigationCollapse">
		<a class="btn btn-primary" href="${site_default_url}/trial-registration" target="_blank">
			<@liferay.language key="start-trial" />
		</a>
	</div>
</#if>