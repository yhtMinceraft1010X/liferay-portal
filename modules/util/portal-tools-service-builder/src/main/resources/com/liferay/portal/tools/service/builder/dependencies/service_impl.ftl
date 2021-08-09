package ${packagePath}.service.impl;

import ${apiPackagePath}.service.${entity.name}${sessionTypeName}Service;
import ${packagePath}.service.base.${entity.name}${sessionTypeName}ServiceBaseImpl;

import com.liferay.portal.aop.AopService;

import org.osgi.service.component.annotations.Component;

<#if stringUtil.equals(sessionTypeName, "Local")>
/**
 * @author ${author}
 */
<#else>
/**
 * @author ${author}
 */
</#if>

<#if dependencyInjectorDS>
	@Component(
		<#if stringUtil.equals(sessionTypeName, "Local")>
			property = "model.class.name=${apiPackagePath}.model.${entity.name}"
		<#else>
			property = {
				"json.web.service.context.name=${portletShortName?lower_case}",
				"json.web.service.context.path=${entity.name}"
			}
		</#if>,
		service = AopService.class
	)
</#if>
public class ${entity.name}${sessionTypeName}ServiceImpl extends ${entity.name}${sessionTypeName}ServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
<#if stringUtil.equals(sessionTypeName, "Local")>
	 * Never reference this class directly. Use <code>${apiPackagePath}.service.${entity.name}LocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>${apiPackagePath}.service.${entity.name}LocalServiceUtil</code>.
<#else>
	 * Never reference this class directly. Always use <code>${apiPackagePath}.service.${entity.name}ServiceUtil</code> to access the ${entity.humanName} remote service.
</#if>
	 */
}