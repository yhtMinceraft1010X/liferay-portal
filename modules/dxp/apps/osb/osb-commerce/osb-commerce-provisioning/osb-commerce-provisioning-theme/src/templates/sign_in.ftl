<div class="nav-item">
	<div class="btn sign-in-btn">
		<svg class="lexicon-icon">
			<use
				href="${themeDisplay.getPathThemeImages()}/lexicon/icons.svg#user"/>
		</svg>

		<div class="modal-dialog sign-in-wrapper">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title" id="clayDefaultModalLabel">
						<@liferay.language key="sign-in" />
					</div>
				</div>

				<div class="modal-body">
					<@liferay_portlet["runtime"] portletName="com_liferay_login_web_portlet_LoginPortlet" />
				</div>
			</div>
		</div>
	</div>
</div>