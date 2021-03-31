<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">
	<head>
		<title>${the_title} - ${company_name}</title>

		<meta content="initial-scale=1.0, width=device-width" name="viewport" />

		<@liferay_util["include"] page=top_head_include />
	</head>

	<body class="${css_class}">
		<@liferay_ui["quick-access"] contentId="#main-content" />
		<@liferay_util["include"] page=body_top_include />
		<@liferay.control_menu />

		<div id="wrapper">
			<#if show_header>
				<#include "${full_templates_path}/header.ftl" />
			</#if>

			<div class="parallax-container">
				<div class="parallax">
					<div class="bg-wrapper container">
						<div class="back bg"></div>
						<div class="bg front"></div>
					</div>

					<div class="content-wrapper">
						<main class="${portal_content_css_class}" id="content" role="main">
							<h2 class="sr-only" role="heading" aria-level="1">${the_title}</h2>

							<#if selectable>
								<@liferay_util["include"] page=content_include />
							<#else>
								${portletDisplay.recycle()}

								${portletDisplay.setTitle(the_title)}

								<@liferay_theme["wrap-portlet"] page="portlet.ftl">
									<@liferay_util["include"] page=content_include />
								</@>
							</#if>
						</main>

						<#if show_footer>
							<#include "${full_templates_path}/footer.ftl" />
						</#if>
					</div>
				</div>
			</div>
		</div>

	<@liferay_util["include"] page=body_bottom_include />

	<@liferay_util["include"] page=bottom_include />

	</body>
</html>