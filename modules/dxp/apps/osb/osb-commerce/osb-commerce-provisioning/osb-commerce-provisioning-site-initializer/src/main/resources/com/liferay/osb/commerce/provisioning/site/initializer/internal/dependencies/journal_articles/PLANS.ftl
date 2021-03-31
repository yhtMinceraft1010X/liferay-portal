<#if plansImage.getSiblings()?has_content>
	<#list plansImage.getSiblings() as curPlansImage>
		<#if curPlansImage.getData()?? && curPlansImage.getData() != "">
			<img alt="${curPlansImage.getAttribute("alt")}" src="${curPlansImage.getData()}" />
		</#if>
	</#list>
</#if>