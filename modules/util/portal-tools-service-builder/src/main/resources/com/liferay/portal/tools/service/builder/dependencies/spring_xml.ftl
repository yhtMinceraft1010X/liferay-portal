<#assign parent = "" />

<#if serviceBuilder.isVersionLTE_7_1_0()>
	<#assign parent = " parent=\"basePersistence\"" />
</#if>

<#list entities as entity>
	<#if entity.hasLocalService()>
		<#assign sessionType = "Local" />

		<#include "spring_xml_session.ftl">
	<#elseif entity.hasEntityColumns() && entity.hasPersistence() && entity.isChangeTrackingEnabled()>
		<bean class="${packagePath}.service.impl.${entity.name}CTServiceImpl" id="${apiPackagePath}.service.${entity.name}CTService" />
	</#if>

	<#if entity.hasRemoteService()>
		<#assign sessionType = "" />

		<#include "spring_xml_session.ftl">
	</#if>

	<#if entity.hasEntityColumns() && entity.hasPersistence()>
		<#if serviceBuilder.isVersionGTE_7_4_0()>
			<bean class="${packagePath}.service.persistence.impl.${entity.name}ModelArgumentsResolver" id="${packagePath}.service.persistence.impl.${entity.name}ModelArgumentsResolver" />
		</#if>

		<#if !stringUtil.equals(entity.dataSource, "liferayDataSource") || !stringUtil.equals(entity.sessionFactory, "liferaySessionFactory")>
			<bean class="${entity.persistenceClassName}" id="${apiPackagePath}.service.persistence.${entity.name}Persistence"${parent}>
				<#if !stringUtil.equals(entity.dataSource, "liferayDataSource")>
					<property name="dataSource" ref="${entity.getDataSource()}" />
				</#if>

				<#if !stringUtil.equals(entity.sessionFactory, "liferaySessionFactory")>
					<property name="sessionFactory" ref="${entity.getSessionFactory()}" />
				</#if>
			</bean>
		<#else>
			<bean class="${entity.persistenceClassName}" id="${apiPackagePath}.service.persistence.${entity.name}Persistence"${parent} />
		</#if>
	</#if>

	<#if entity.hasFinderClassName() && entity.hasPersistence()>
		<#if !stringUtil.equals(entity.dataSource, "liferayDataSource") || !stringUtil.equals(entity.sessionFactory, "liferaySessionFactory")>
			<bean class="${entity.finderClassName}" id="${apiPackagePath}.service.persistence.${entity.name}Finder"${parent}>
				<#if !stringUtil.equals(entity.dataSource, "liferayDataSource")>
					<property name="dataSource" ref="${entity.getDataSource()}" />
				</#if>

				<#if !stringUtil.equals(entity.sessionFactory, "liferaySessionFactory")>
					<property name="sessionFactory" ref="${entity.getSessionFactory()}" />
				</#if>
			</bean>
		<#else>
			<bean class="${entity.finderClassName}" id="${apiPackagePath}.service.persistence.${entity.name}Finder"${parent} />
		</#if>
	</#if>
</#list>