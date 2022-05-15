package ${configYAML.apiPackagePath}.resource.${escapedVersion}.test;

<#list allExternalSchemas?keys as schemaName>
	import ${configYAML.apiPackagePath}.client.dto.${escapedVersion}.${schemaName};
	import ${configYAML.apiPackagePath}.client.resource.${escapedVersion}.${schemaName}Resource;
	import ${configYAML.apiPackagePath}.client.serdes.${escapedVersion}.${schemaName}SerDes;
</#list>

<#list allSchemas?keys as schemaName>
	import ${configYAML.apiPackagePath}.client.dto.${escapedVersion}.${schemaName};
	import ${configYAML.apiPackagePath}.client.resource.${escapedVersion}.${schemaName}Resource;
	import ${configYAML.apiPackagePath}.client.serdes.${escapedVersion}.${schemaName}SerDes;
</#list>

import ${configYAML.apiPackagePath}.client.http.HttpInvoker;
import ${configYAML.apiPackagePath}.client.pagination.Page;
import ${configYAML.apiPackagePath}.client.pagination.Pagination;
import ${configYAML.apiPackagePath}.client.permission.Permission;
import ${configYAML.apiPackagePath}.client.resource.${escapedVersion}.${schemaName}Resource;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

<#assign
	javaMethodSignatures = freeMarkerTool.getResourceTestCaseJavaMethodSignatures(configYAML, openAPIYAML, schemaName)

	generateDepotEntry = freeMarkerTool.containsJavaMethodSignature(javaMethodSignatures, "AssetLibrary")
/>

<#if generateDepotEntry>
	import com.liferay.depot.model.DepotEntry;
	import com.liferay.depot.service.DepotEntryLocalServiceUtil;
</#if>

import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONDeserializer;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.search.test.util.SearchTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.io.File;

import java.lang.reflect.Method;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author ${configYAML.author}
 * @generated
 */
@Generated("")
public abstract class Base${schemaName}ResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule = new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(testGroup.getCompanyId());

		<#if generateDepotEntry>
			testDepotEntry = DepotEntryLocalServiceUtil.addDepotEntry(
				Collections.singletonMap(LocaleUtil.getDefault(), RandomTestUtil.randomString()),
				null, new ServiceContext() {
					{
						setCompanyId(testGroup.getCompanyId());
						setUserId(TestPropsValues.getUserId());
					}
				});
		</#if>

		_${schemaVarName}Resource.setContextCompany(testCompany);

		${schemaName}Resource.Builder builder = ${schemaName}Resource.builder();

		${schemaVarName}Resource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	<#assign properties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, schema) />

	<#if javaDataTypeMap?keys?seq_contains(schemaName)>
		@Test
		public void testClientSerDesToDTO() throws Exception {
			ObjectMapper objectMapper = new ObjectMapper() {
				{
					configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
					configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
					enable(SerializationFeature.INDENT_OUTPUT);
					setDateFormat(new ISO8601DateFormat());
					setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
					setSerializationInclusion(JsonInclude.Include.NON_NULL);
					setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
					setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
				}
			};

			${schemaName} ${schemaVarName}1 = random${schemaName}();

			String json = objectMapper.writeValueAsString(${schemaVarName}1);

			${schemaName} ${schemaVarName}2 = ${schemaName}SerDes.toDTO(json);

			Assert.assertTrue(equals(${schemaVarName}1, ${schemaVarName}2));
		}

		@Test
		public void testClientSerDesToJSON() throws Exception {
			ObjectMapper objectMapper = new ObjectMapper() {
				{
					configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
					configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
					setDateFormat(new ISO8601DateFormat());
					setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
					setSerializationInclusion(JsonInclude.Include.NON_NULL);
					setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
					setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
				}
			};

			${schemaName} ${schemaVarName} = random${schemaName}();

			String json1 = objectMapper.writeValueAsString(${schemaVarName});
			String json2 = ${schemaName}SerDes.toJSON(${schemaVarName});

			Assert.assertEquals(
				objectMapper.readTree(json1), objectMapper.readTree(json2));
		}

		@Test
		public void testEscapeRegexInStringFields() throws Exception {
			String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

			${schemaName} ${schemaVarName} = random${schemaName}();

			<#list properties?keys as propertyName>
				<#if stringUtil.equals(properties[propertyName], "String")>
					${schemaVarName}.set${propertyName?cap_first}(regex);
				</#if>
			</#list>

			String json = ${schemaName}SerDes.toJSON(${schemaVarName});

			Assert.assertFalse(json.contains(regex));

			${schemaVarName} = ${schemaName}SerDes.toDTO(json);

			<#list properties?keys as propertyName>
				<#if stringUtil.equals(properties[propertyName], "String")>
					Assert.assertEquals(regex, ${schemaVarName}.get${propertyName?cap_first}());
				</#if>
			</#list>
		}
	</#if>

	<#assign
		enumSchemas = freeMarkerTool.getDTOEnumSchemas(openAPIYAML, schema)
		generateGetMultipartFilesMethod = false
		generateSearchTestRule = false
		randomDataTypes = ["Boolean", "Double", "Integer", "Long", "String"]
	/>

	<#list javaMethodSignatures as javaMethodSignature>
		<#assign
			arguments = freeMarkerTool.getResourceTestCaseArguments(javaMethodSignature.javaMethodParameters)
			parameters = freeMarkerTool.getResourceTestCaseParameters(javaMethodSignature.javaMethodParameters, openAPIYAML, javaMethodSignature.operation, false)
		/>

		<#if stringUtil.endsWith(javaMethodSignature.methodName, schemaName + "Batch")>
			<#continue>
		</#if>

		<#if freeMarkerTool.hasHTTPMethod(javaMethodSignature, "delete")>
			<#assign missingGetterJavaMethodParametersMap = {} />

			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if properties?keys?seq_contains("id")>
					@SuppressWarnings("PMD.UnusedLocalVariable")
					${schemaName} ${schemaVarName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					assertHttpResponseStatusCode(204, ${schemaVarName}Resource.${javaMethodSignature.methodName}HttpResponse(

					<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if !javaMethodParameter?is_first>
							,
						</#if>

						<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
							<#if stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								${schemaVarName}.getId()
							<#elseif properties?keys?seq_contains(javaMethodParameter.parameterName)>
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
							<#else>
								<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

								test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
							</#if>
						<#else>
							null
						</#if>
					</#list>

					));

					<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "get" + javaMethodSignature.methodName?remove_beginning("delete"))>
						assertHttpResponseStatusCode(404, ${schemaVarName}Resource.get${javaMethodSignature.methodName?remove_beginning("delete")}HttpResponse(

						<#assign
							getJavaMethodSignature = freeMarkerTool.getJavaMethodSignature(javaMethodSignatures, "get" + javaMethodSignature.methodName?remove_beginning("delete"))
						/>

						<#list getJavaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								${schemaVarName}.getId()
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
							<#else>
								<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

								test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
							</#if>

							<#sep>, </#sep>
						</#list>

						));

						assertHttpResponseStatusCode(404, ${schemaVarName}Resource.get${javaMethodSignature.methodName?remove_beginning("delete")}HttpResponse(

						<#list getJavaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								<@getDefaultParameter javaMethodParameter=javaMethodParameter />
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
							<#else>
								<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

								test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
							</#if>

							<#sep>, </#sep>
						</#list>

						));
					</#if>
				<#else>
					Assert.assertTrue(false);
				</#if>
			}

			<@getTestGetterMethods
				javaMethodSignature=javaMethodSignature
				missingGetterJavaMethodParametersMap=missingGetterJavaMethodParametersMap
				testNamePrefix="test"
			/>

			<#if properties?keys?seq_contains("id")>
				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
					<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName) || freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
						<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

						return ${schemaVarName}Resource.postSite${schemaName}(testGroup.getGroupId(), random${schemaName}()

						<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
							<#assign generateGetMultipartFilesMethod = true />

							, getMultipartFiles()
						</#if>

						);
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			</#if>
		<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "get") && javaMethodSignature.returnType?contains("Page<")>
			<#if javaMethodSignature.methodName?contains("Permission")>
				@Test
				public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
					<#assign firstParameterName = javaMethodSignature.javaMethodParameters[0].parameterName />

					<#if !stringUtil.equals("assetLibraryId", firstParameterName) && !stringUtil.equals("siteId", firstParameterName)>
						${schemaName} post${schemaName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();
					</#if>

					Page<Permission> page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(
						<#if stringUtil.equals("assetLibraryId", firstParameterName)>
							testDepotEntry.getDepotEntryId()
						<#elseif stringUtil.equals("siteId", firstParameterName)>
							testGroup.getGroupId()
						<#else>
							post${schemaName}.getId()
						</#if>
					, RoleConstants.GUEST);

					Assert.assertNotNull(page);
				}

				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
					<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, javaMethodSignature.javaMethodParameters[0].parameterName, schemaName)>
						<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, javaMethodSignature.javaMethodParameters[0].parameterName, schemaName) />

						return test${postSchemaJavaMethodSignature.methodName?cap_first}_add${schemaName}(random${schemaName}()

						<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
							<#assign generateGetMultipartFilesMethod = true />

							, getMultipartFiles()
						</#if>

						);
					<#elseif freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
						<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

						return test${postSchemaJavaMethodSignature.methodName?cap_first}_add${schemaName}(random${schemaName}()

						<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
							<#assign generateGetMultipartFilesMethod = true />

							, getMultipartFiles()
						</#if>

						);
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			<#elseif !javaMethodSignature.methodName?contains("Permission")>
				@Test
				public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
					<#assign topLevel = javaMethodSignature.pathJavaMethodParameters?size == 0 />

					<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
						${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
						${javaMethodParameter.parameterType} irrelevant${javaMethodParameter.parameterName?cap_first} = test${javaMethodSignature.methodName?cap_first}_getIrrelevant${javaMethodParameter.parameterName?cap_first}();
					</#list>

					Page<${schemaName}> page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

					<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if !javaMethodParameter?is_first>
							,
						</#if>

						<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
							Pagination.of(1, 10)
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, "search")>
							null
						<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
							${javaMethodParameter.parameterName}
						<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.String")>
							RandomTestUtil.randomString()
						<#elseif stringUtil.equals(javaMethodParameter.parameterType, "boolean")>
							RandomTestUtil.randomBoolean()
						<#elseif stringUtil.equals(javaMethodParameter.parameterType, "double")>
							RandomTestUtil.randomDouble()
						<#elseif stringUtil.equals(javaMethodParameter.parameterType, "long")>
							RandomTestUtil.randomLong()
						<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.util.Date")>
							RandomTestUtil.nextDate()
						<#else>
							null
						</#if>
					</#list>

					);

					<#if topLevel>
						long totalCount = page.getTotalCount();
					<#else>
						Assert.assertEquals(0, page.getTotalCount());
					</#if>

					<#if freeMarkerTool.hasPathParameter(javaMethodSignature)>
						if (<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									&&
								</#if>

								(irrelevant${javaMethodParameter.parameterName?cap_first} != null)
							</#list>) {

							${schemaName} irrelevant${schemaName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

							<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
								irrelevant${javaMethodParameter.parameterName?cap_first},
							</#list>

							randomIrrelevant${schemaName}());

							page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
									Pagination.of(1, 2)
								<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									irrelevant${javaMethodParameter.parameterName?cap_first}
								<#else>
									null
								</#if>
							</#list>

							);

							Assert.assertEquals(1, page.getTotalCount());

							assertEquals(Arrays.asList(irrelevant${schemaName}), (List<${schemaName}>)page.getItems());
							assertValid(page);
						}
					</#if>

					${schemaName} ${schemaVarName}1 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

					<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
						${javaMethodParameter.parameterName},
					</#list>

					random${schemaName}());

					${schemaName} ${schemaVarName}2 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

					<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
						${javaMethodParameter.parameterName},
					</#list>

					random${schemaName}());

					page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

					<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if !javaMethodParameter?is_first>
							,
						</#if>

						<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
							Pagination.of(1, 10)
						<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
							${javaMethodParameter.parameterName}
						<#else>
							null
						</#if>
					</#list>

					);

					Assert.assertEquals(<#if topLevel>totalCount + </#if>2, page.getTotalCount());

					<#if topLevel>
						assertContains(${schemaVarName}1, (List<${schemaName}>)page.getItems());
						assertContains(${schemaVarName}2, (List<${schemaName}>)page.getItems());
						assertValid(page);
					<#else>
						assertEqualsIgnoringOrder(Arrays.asList(${schemaVarName}1, ${schemaVarName}2), (List<${schemaName}>)page.getItems());
						assertValid(page);
					</#if>

					<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "delete" + schemaName)>
						<#assign deleteJavaMethodSignature = freeMarkerTool.getJavaMethodSignature(javaMethodSignatures, "delete" + schemaName) />

						<#if properties?keys?seq_contains("id")>
							${schemaVarName}Resource.delete${schemaName}(
								<#list deleteJavaMethodSignature.javaMethodParameters as javaMethodParameter>
									<#if freeMarkerTool.isPathParameter(javaMethodParameter, deleteJavaMethodSignature.operation) && (stringUtil.equals(javaMethodParameter.parameterName, "id") || stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id"))>
										${schemaVarName}1.getId()
									<#else>
										null
									</#if>

									<#sep>, </#sep>
								</#list>);

							${schemaVarName}Resource.delete${schemaName}(
								<#list deleteJavaMethodSignature.javaMethodParameters as javaMethodParameter>
									<#if freeMarkerTool.isPathParameter(javaMethodParameter, deleteJavaMethodSignature.operation) && (stringUtil.equals(javaMethodParameter.parameterName, "id") || stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id"))>
										${schemaVarName}2.getId()
									<#else>
										null
									</#if>

									<#sep>, </#sep>
								</#list>);
						</#if>
					</#if>
				}

				<#if parameters?contains("Filter filter")>
					<#assign generateSearchTestRule = true />

					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithFilterDateTimeEquals() throws Exception {
						List<EntityField> entityFields = getEntityFields(EntityField.Type.DATE_TIME);

						if (entityFields.isEmpty()) {
							return;
						}

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
						</#list>

						${schemaName} ${schemaVarName}1 = random${schemaName}();

						${schemaVarName}1 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						${schemaVarName}1);

						for (EntityField entityField : entityFields) {
							Page<${schemaName}> page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if stringUtil.equals(javaMethodParameter.parameterName, "filter")>
									getFilterString(entityField, "between", ${schemaVarName}1)
								<#elseif stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
									Pagination.of(1, 2)
								<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									${javaMethodParameter.parameterName}
								<#else>
									null
								</#if>
							</#list>

							);

							assertEquals(Collections.singletonList(${schemaVarName}1), (List<${schemaName}>)page.getItems());
						}
					}

					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithFilterDoubleEquals() throws Exception {
						List<EntityField> entityFields = getEntityFields(EntityField.Type.DOUBLE);

						if (entityFields.isEmpty()) {
							return;
						}

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
						</#list>

						${schemaName} ${schemaVarName}1 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						@SuppressWarnings("PMD.UnusedLocalVariable")
						${schemaName} ${schemaVarName}2 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						for (EntityField entityField : entityFields) {
							Page<${schemaName}> page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if stringUtil.equals(javaMethodParameter.parameterName, "filter")>
									getFilterString(entityField, "eq", ${schemaVarName}1)
								<#elseif stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
									Pagination.of(1, 2)
								<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									${javaMethodParameter.parameterName}
								<#else>
									null
								</#if>
							</#list>

							);

							assertEquals(Collections.singletonList(${schemaVarName}1), (List<${schemaName}>)page.getItems());
						}
					}

					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithFilterStringEquals() throws Exception {
						List<EntityField> entityFields = getEntityFields(EntityField.Type.STRING);

						if (entityFields.isEmpty()) {
							return;
						}

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
						</#list>

						${schemaName} ${schemaVarName}1 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						@SuppressWarnings("PMD.UnusedLocalVariable")
						${schemaName} ${schemaVarName}2 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						for (EntityField entityField : entityFields) {
							Page<${schemaName}> page = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if stringUtil.equals(javaMethodParameter.parameterName, "filter")>
									getFilterString(entityField, "eq", ${schemaVarName}1)
								<#elseif stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
									Pagination.of(1, 2)
								<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									${javaMethodParameter.parameterName}
								<#else>
									null
								</#if>
							</#list>

							);

							assertEquals(Collections.singletonList(${schemaVarName}1), (List<${schemaName}>)page.getItems());
						}
					}
				</#if>

				<#if parameters?contains("Pagination pagination")>
					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithPagination() throws Exception {
						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
						</#list>

						<#if topLevel>
							Page<${schemaName}> totalPage = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									${javaMethodParameter.parameterName}
								<#else>
									null
								</#if>
							</#list>

							);

							int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());
						</#if>

						${schemaName} ${schemaVarName}1 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						${schemaName} ${schemaVarName}2 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						${schemaName} ${schemaVarName}3 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						random${schemaName}());

						Page<${schemaName}> page1 = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if !javaMethodParameter?is_first>
								,
							</#if>

							<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
								Pagination.of(1, <#if topLevel>totalCount + </#if>2)
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
								${javaMethodParameter.parameterName}
							<#else>
								null
							</#if>
						</#list>

						);

						List<${schemaName}> ${schemaVarNames}1 = (List<${schemaName}>)page1.getItems();

						Assert.assertEquals(${schemaVarNames}1.toString(), <#if topLevel>totalCount + </#if>2, ${schemaVarNames}1.size());

						Page<${schemaName}> page2 = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if !javaMethodParameter?is_first>
								,
							</#if>

							<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
								Pagination.of(2, <#if topLevel>totalCount + </#if>2)
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
								${javaMethodParameter.parameterName}
							<#else>
								null
							</#if>
						</#list>

						);

						Assert.assertEquals(<#if topLevel>totalCount + </#if>3, page2.getTotalCount());

						List<${schemaName}> ${schemaVarNames}2 = (List<${schemaName}>)page2.getItems();

						Assert.assertEquals(${schemaVarNames}2.toString(), 1, ${schemaVarNames}2.size());

						Page<${schemaName}> page3 = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if !javaMethodParameter?is_first>
								,
							</#if>

							<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
								Pagination.of(1, <#if topLevel>totalCount + </#if>3)
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
								${javaMethodParameter.parameterName}
							<#else>
								null
							</#if>
						</#list>

						);

						<#if topLevel>
							assertContains(${schemaVarName}1, (List<${schemaName}>)page3.getItems());
							assertContains(${schemaVarName}2, (List<${schemaName}>)page3.getItems());
							assertContains(${schemaVarName}3, (List<${schemaName}>)page3.getItems());
						<#else>
							assertEqualsIgnoringOrder(Arrays.asList(${schemaVarName}1, ${schemaVarName}2, ${schemaVarName}3), (List<${schemaName}>)page3.getItems());
						</#if>
					}
				</#if>

				<#if parameters?contains("Sort[] sorts")>
					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithSortDateTime() throws Exception {
						test${javaMethodSignature.methodName?cap_first}WithSort(
							EntityField.Type.DATE_TIME,
							(entityField, ${schemaVarName}1, ${schemaVarName}2) -> {
								BeanTestUtil.setProperty(${schemaVarName}1, entityField.getName(), DateUtils.addMinutes(new Date(), -2));
							});
					}

					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithSortDouble() throws Exception {
						test${javaMethodSignature.methodName?cap_first}WithSort(
							EntityField.Type.DOUBLE,
							(entityField, ${schemaVarName}1, ${schemaVarName}2) -> {
								BeanTestUtil.setProperty(${schemaVarName}1, entityField.getName(), 0.1);
								BeanTestUtil.setProperty(${schemaVarName}2, entityField.getName(), 0.5);
							});
					}

					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithSortInteger() throws Exception {
						test${javaMethodSignature.methodName?cap_first}WithSort(
							EntityField.Type.INTEGER,
							(entityField, ${schemaVarName}1, ${schemaVarName}2) -> {
								BeanTestUtil.setProperty(${schemaVarName}1, entityField.getName(), 0);
								BeanTestUtil.setProperty(${schemaVarName}2, entityField.getName(), 1);
							});
					}

					@Test
					public void test${javaMethodSignature.methodName?cap_first}WithSortString() throws Exception {
						test${javaMethodSignature.methodName?cap_first}WithSort(
							EntityField.Type.STRING,
							(entityField, ${schemaVarName}1, ${schemaVarName}2) -> {

								Class<?> clazz = ${schemaVarName}1.getClass();

								String entityFieldName = entityField.getName();

								java.lang.reflect.Method method = clazz.getMethod( "get" + StringUtil.upperCaseFirstLetter(entityFieldName));

								Class<?> returnType = method.getReturnType();

								if (returnType.isAssignableFrom(Map.class)) {
									BeanTestUtil.setProperty(${schemaVarName}1, entityFieldName, Collections.singletonMap("Aaa", "Aaa"));
									BeanTestUtil.setProperty(${schemaVarName}2, entityFieldName, Collections.singletonMap("Bbb", "Bbb"));
								}
								else if (entityFieldName.contains("email")) {
									BeanTestUtil.setProperty(${schemaVarName}1, entityFieldName, "aaa" + StringUtil.toLowerCase(RandomTestUtil.randomString()) + "@liferay.com");
									BeanTestUtil.setProperty(${schemaVarName}2, entityFieldName, "bbb" + StringUtil.toLowerCase(RandomTestUtil.randomString()) + "@liferay.com");
								}
								else {
									BeanTestUtil.setProperty(${schemaVarName}1, entityFieldName, "aaa" + StringUtil.toLowerCase(RandomTestUtil.randomString()));
									BeanTestUtil.setProperty(${schemaVarName}2, entityFieldName, "bbb" + StringUtil.toLowerCase(RandomTestUtil.randomString()));
								}
							});
					}

					protected void test${javaMethodSignature.methodName?cap_first}WithSort(EntityField.Type type, UnsafeTriConsumer<EntityField, ${schemaName}, ${schemaName}, Exception> unsafeTriConsumer) throws Exception {
						List<EntityField> entityFields = getEntityFields(type);

						if (entityFields.isEmpty()) {
							return;
						}

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
						</#list>

						${schemaName} ${schemaVarName}1 = random${schemaName}();
						${schemaName} ${schemaVarName}2 = random${schemaName}();

						for (EntityField entityField : entityFields) {
							unsafeTriConsumer.accept(entityField, ${schemaVarName}1, ${schemaVarName}2);
						}

						${schemaVarName}1 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						${schemaVarName}1);

						${schemaVarName}2 = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(

						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterName},
						</#list>

						${schemaVarName}2);

						for (EntityField entityField : entityFields) {
							Page<${schemaName}> ascPage = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
									Pagination.of(1, 2)
								<#elseif stringUtil.equals(javaMethodParameter.parameterName, "sorts")>
									entityField.getName() + ":asc"
								<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									${javaMethodParameter.parameterName}
								<#else>
									null
								</#if>
							</#list>

							);

							assertEquals(Arrays.asList(${schemaVarName}1, ${schemaVarName}2), (List<${schemaName}>)ascPage.getItems());

							Page<${schemaName}> descPage = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

							<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
								<#if !javaMethodParameter?is_first>
									,
								</#if>

								<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
									Pagination.of(1, 2)
								<#elseif stringUtil.equals(javaMethodParameter.parameterName, "sorts")>
									entityField.getName() + ":desc"
								<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
									${javaMethodParameter.parameterName}
								<#else>
									null
								</#if>
							</#list>

							);

							assertEquals(Arrays.asList(${schemaVarName}2, ${schemaVarName}1), (List<${schemaName}>)descPage.getItems());
						}
					}
				</#if>

				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}(
						<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
							${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName},
						</#list>

						${schemaName} ${schemaVarName}
					) throws Exception {

					<#if (javaMethodSignature.pathJavaMethodParameters?size == 1)>
						<#assign firstPathJavaMethodParameter = javaMethodSignature.pathJavaMethodParameters[0] />

						<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, firstPathJavaMethodParameter.parameterName, schemaName)>
							<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, firstPathJavaMethodParameter.parameterName, schemaName) />

							return ${schemaVarName}Resource.${postSchemaJavaMethodSignature.methodName}(${firstPathJavaMethodParameter.parameterName}, ${schemaVarName}

							<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
								<#assign generateGetMultipartFilesMethod = true />

								, getMultipartFiles()
							</#if>

							);
						<#else>
							throw new UnsupportedOperationException("This method needs to be implemented");
						</#if>
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}

				<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
					protected ${javaMethodParameter.parameterType} test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}() throws Exception {
						<#if stringUtil.equals(javaMethodParameter.parameterName, "assetLibraryId")>
							return testDepotEntry.getDepotEntryId();
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
							return testGroup.getGroupId();
						<#else>
							throw new UnsupportedOperationException("This method needs to be implemented");
						</#if>
					}

					protected ${javaMethodParameter.parameterType} test${javaMethodSignature.methodName?cap_first}_getIrrelevant${javaMethodParameter.parameterName?cap_first}() throws Exception {
						<#if stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
							return irrelevantGroup.getGroupId();
						<#else>
							return null;
						</#if>
					}
				</#list>
			</#if>
		<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "get") && javaMethodSignature.returnType?ends_with(schemaName)>
			<#assign missingGetterJavaMethodParametersMap = {} />

			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if properties?keys?seq_contains("id")>
					${schemaName} post${schemaName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					${schemaName} get${schemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(

					<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if !javaMethodParameter?is_first>
							,
						</#if>

						<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
							Pagination.of(1, 2)
						<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
							<#if stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								post${schemaName}.getId()
							<#elseif properties?keys?seq_contains(javaMethodParameter.parameterName)>
								post${schemaName}.get${javaMethodParameter.parameterName?cap_first}()
							<#else>
								<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

								test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
							</#if>
						<#else>
							null
						</#if>
					</#list>

					);

					assertEquals(post${schemaName}, get${schemaName});
					assertValid(get${schemaName});
				<#else>
					Assert.assertTrue(false);
				</#if>
			}

			<@getTestGetterMethods
				javaMethodSignature=javaMethodSignature
				missingGetterJavaMethodParametersMap=missingGetterJavaMethodParametersMap
				testNamePrefix="test"
			/>

			<#if properties?keys?seq_contains("id")>
				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
					<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName) || freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
						<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

						return ${schemaVarName}Resource.postSite${schemaName}(testGroup.getGroupId(), random${schemaName}()

						<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
							<#assign generateGetMultipartFilesMethod = true />

							, getMultipartFiles()
						</#if>

						);
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			</#if>
		<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "patch") && freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "get" + javaMethodSignature.methodName?remove_beginning("patch")) && javaMethodSignature.returnType?ends_with(schemaName)>
			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if !properties?keys?seq_contains("id")>
					Assert.assertTrue(false);
				<#else>
					${schemaName} post${schemaName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					${schemaName} randomPatch${schemaName} = randomPatch${schemaName}();

					<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
						<#assign generateGetMultipartFilesMethod = true />

						Map<String, File> multipartFiles = getMultipartFiles();
					</#if>

					@SuppressWarnings("PMD.UnusedLocalVariable")
					${schemaName} patch${schemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
								<#if stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
									post${schemaName}.getId()
								<#elseif properties?keys?seq_contains(javaMethodParameter.parameterName)>
									post${schemaName}.get${javaMethodParameter.parameterName?cap_first}()
								<#else>
									null
								</#if>
							</#if>
						</#list>, randomPatch${schemaName}
						<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
							, multipartFiles
						</#if>
					);

					${schemaName} expectedPatch${schemaName} = post${schemaName}.clone();

					BeanTestUtil.copyProperties(randomPatch${schemaName}, expectedPatch${schemaName});

					${schemaName} get${schemaName} = ${schemaVarName}Resource.get${javaMethodSignature.methodName?remove_beginning("patch")}(
						<#if (javaMethodSignature.javaMethodParameters?size != 0) &&
							 stringUtil.equals(javaMethodSignature.javaMethodParameters[0].parameterName, "externalReferenceCode")>

							patch${schemaName}.getExternalReferenceCode()
						<#elseif (javaMethodSignature.javaMethodParameters?size != 0) &&
								 (stringUtil.equals(javaMethodSignature.javaMethodParameters[0].parameterName, "id") ||
								 stringUtil.equals(javaMethodSignature.javaMethodParameters[0].parameterName, schemaVarName + "Id"))>

							patch${schemaName}.getId()
						<#else>
							null
						</#if>
					);

					assertEquals(expectedPatch${schemaName}, get${schemaName});
					assertValid(get${schemaName});

					<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
						assertValid(get${schemaName}, multipartFiles);
					</#if>
				</#if>
			}

			<#if properties?keys?seq_contains("id")>
				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
					<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
						<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

						return ${schemaVarName}Resource.postSite${schemaName}(testGroup.getGroupId(), random${schemaName}()

						<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
							<#assign generateGetMultipartFilesMethod = true />

							, getMultipartFiles()
						</#if>

						);
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			</#if>
		<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "post") && javaMethodSignature.returnType?ends_with(schemaName)>
			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				${schemaName} random${schemaName} = random${schemaName}();

				<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
					<#assign generateGetMultipartFilesMethod = true />

					Map<String, File> multipartFiles = getMultipartFiles();
				</#if>

				${schemaName} post${schemaName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}(random${schemaName}

				<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
					, multipartFiles
				</#if>

				);

				assertEquals(random${schemaName}, post${schemaName});
				assertValid(post${schemaName});

				<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
					assertValid(post${schemaName}, multipartFiles);
				</#if>
			}

			protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}(${schemaName} ${schemaVarName}

			<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
				, Map<String, File> multipartFiles
			</#if>

			) throws Exception {
				<#if (javaMethodSignature.pathJavaMethodParameters?size == 1)>
					<#assign
						firstPathJavaMethodParameter = javaMethodSignature.pathJavaMethodParameters[0]
						modifiedPathJavaMethodParameterName = firstPathJavaMethodParameter.parameterName?remove_beginning("parent")?remove_ending("Id")?cap_first
					/>

					<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, firstPathJavaMethodParameter.parameterName, schemaName) && stringUtil.equals(javaMethodSignature.methodName, "post" + modifiedPathJavaMethodParameterName + schemaName)>
						return ${schemaVarName}Resource.post${modifiedPathJavaMethodParameterName}${schemaName}(testGet${modifiedPathJavaMethodParameterName}${schemaNames}Page_get<#if stringUtil.startsWith(firstPathJavaMethodParameter.parameterName, "parent")>Parent</#if>${modifiedPathJavaMethodParameterName}Id(), ${schemaVarName}

						<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
							, multipartFiles
						</#if>

						);
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				<#else>
					throw new UnsupportedOperationException("This method needs to be implemented");
				</#if>
			}
		<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "put") && javaMethodSignature.methodName?contains("Permission")>
			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				@SuppressWarnings("PMD.UnusedLocalVariable")
				${schemaName} ${schemaVarName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();

				@SuppressWarnings("PMD.UnusedLocalVariable")
				com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

				assertHttpResponseStatusCode(
					200,
					${schemaVarName}Resource.${javaMethodSignature.methodName}HttpResponse(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<@getPermissionParameter
								javaMethodParameter=javaMethodParameter
								javaMethodSignature=javaMethodSignature
								properties=properties
								roleName="role.getName()"
								schemaVarName=schemaVarName
								schemaVarNameId="${schemaVarName}.getId()"
							>
								<#if javaMethodSignature.methodName?contains("AssetLibrary") || javaMethodSignature.methodName?contains("Site")>
									"PERMISSIONS"
								<#else>
									"VIEW"
								</#if>
							</@getPermissionParameter>

							<#sep>, </#sep>
						</#list>
						));

				assertHttpResponseStatusCode(
					404,
					${schemaVarName}Resource.${javaMethodSignature.methodName}HttpResponse(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#assign schemaVarNameId>
								<@getDefaultParameter javaMethodParameter=javaMethodParameter />
							</#assign>

							<@getPermissionParameter
								javaMethodParameter=javaMethodParameter
								javaMethodSignature=javaMethodSignature
								properties=properties
								roleName="\"-\""
								schemaVarName=schemaVarName
								schemaVarNameId=schemaVarNameId
							>
								"-"
							</@getPermissionParameter>

							<#sep>, </#sep>
						</#list>
					));
			}

			protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
				<#if javaMethodSignature.methodName?contains("AssetLibrary") && freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName)>
					<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName) />

					return ${schemaVarName}Resource.postAssetLibrary${schemaName}(testDepotEntry.getDepotEntryId(), random${schemaName}()

					<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
						<#assign generateGetMultipartFilesMethod = true />

						, getMultipartFiles()
					</#if>

					);
				<#elseif freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
					<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

					return ${schemaVarName}Resource.postSite${schemaName}(testGroup.getGroupId(), random${schemaName}()

					<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
						<#assign generateGetMultipartFilesMethod = true />

						, getMultipartFiles()
					</#if>

					);
				<#else>
					throw new UnsupportedOperationException("This method needs to be implemented");
				</#if>
			}
		<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "put") && javaMethodSignature.returnType?ends_with(schemaName)>
			<#assign missingGetterJavaMethodParametersMap = {} />

			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if !properties?keys?seq_contains("id")>
					Assert.assertTrue(false);
				<#else>
					${schemaName} post${schemaName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					${schemaName} random${schemaName} = random${schemaName}();

					<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
						<#assign generateGetMultipartFilesMethod = true />

						Map<String, File> multipartFiles = getMultipartFiles();
					</#if>

					${schemaName} put${schemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(
						<@getPutParameters
							hasMultipartFiles=true
							javaMethodSignature=javaMethodSignature
							newSchemaVarNamePrefix="random"
							schemaName=schemaName
							schemaVarName=schemaVarName
							schemaVarNamePrefix="post"
						/>
					);

					assertEquals(random${schemaName}, put${schemaName});
					assertValid(put${schemaName});

					${schemaName} get${schemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName?replace("put", "get")}(
						<@getGetterParameters javaMethodSignature=javaMethodSignature />
					);

					assertEquals(random${schemaName}, get${schemaName});
					assertValid(get${schemaName});

					<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
						assertValid(get${schemaName}, multipartFiles);
					</#if>
				</#if>

				<#if javaMethodSignature.methodName?ends_with("ByExternalReferenceCode")>
					${schemaName} new${schemaName} = test${javaMethodSignature.methodName?cap_first}_create${schemaName}();

					put${schemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(
						<@getPutParameters
							hasMultipartFiles=false
							javaMethodSignature=javaMethodSignature
							newSchemaVarNamePrefix="new"
							schemaName=schemaName
							schemaVarName=schemaVarName
							schemaVarNamePrefix="new"
						/>
					);

					assertEquals(new${schemaName}, put${schemaName});
					assertValid(put${schemaName});

					get${schemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName?replace("put", "get")}(
						<@getGetterParameters javaMethodSignature=javaMethodSignature />
					);

					assertEquals(new${schemaName}, get${schemaName});

					Assert.assertEquals(
						new${schemaName}.getExternalReferenceCode(),
						put${schemaName}.getExternalReferenceCode());
				</#if>
			}

			<@getTestGetterMethods
				javaMethodSignature=javaMethodSignature
				missingGetterJavaMethodParametersMap=missingGetterJavaMethodParametersMap
				testNamePrefix="test"
			/>

			<#if javaMethodSignature.methodName?cap_first?ends_with("ByExternalReferenceCode")>
				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_create${schemaName}() throws Exception {
					return random${schemaName}();
				}
			</#if>

			<#if properties?keys?seq_contains("id")>
				protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
					<#if freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName) || freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
						<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

						return ${schemaVarName}Resource.postSite${schemaName}(testGroup.getGroupId(), random${schemaName}()

						<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
							<#assign generateGetMultipartFilesMethod = true />

							, getMultipartFiles()
						</#if>

						);
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			</#if>
		<#elseif properties?keys?seq_contains("id") && stringUtil.equals(javaMethodSignature.returnType, "void")>
			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				@SuppressWarnings("PMD.UnusedLocalVariable")
				${schemaName} ${schemaVarName} = test${javaMethodSignature.methodName?cap_first}_add${schemaName}();

				<#if javaMethodSignature.methodName?contains("Permission")>
					@SuppressWarnings("PMD.UnusedLocalVariable")
					com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);
				</#if>

				assertHttpResponseStatusCode(
					204,
					${schemaVarName}Resource.${javaMethodSignature.methodName}HttpResponse(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								${schemaVarName}.getId()
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, "assetLibraryId")>
								testDepotEntry.getDepotEntryId()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
								testGroup.getGroupId()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
								${schemaVarName}
							<#else>
								null
							</#if>

							<#sep>, </#sep>
						</#list>
					));

				assertHttpResponseStatusCode(
					404,
					${schemaVarName}Resource.${javaMethodSignature.methodName}HttpResponse(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								<@getDefaultParameter javaMethodParameter=javaMethodParameter />
							<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, "assetLibraryId")>
								testDepotEntry.getDepotEntryId()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
								testGroup.getGroupId()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
								${schemaVarName}
							<#else>
								null
							</#if>

							<#sep>, </#sep>
						</#list>
					));
			}

			protected ${schemaName} test${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
				<#if javaMethodSignature.methodName?contains("AssetLibrary") && freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName)>
					<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "assetLibraryId", schemaName) />

					return ${schemaVarName}Resource.postAssetLibrary${schemaName}(testDepotEntry.getDepotEntryId(), random${schemaName}()

					<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
						<#assign generateGetMultipartFilesMethod = true />

						, getMultipartFiles()
					</#if>

					);
				<#elseif freeMarkerTool.hasPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName)>
					<#assign postSchemaJavaMethodSignature = freeMarkerTool.getPostSchemaJavaMethodSignature(javaMethodSignatures, "siteId", schemaName) />

					return ${schemaVarName}Resource.postSite${schemaName}(testGroup.getGroupId(), random${schemaName}()

					<#if freeMarkerTool.hasRequestBodyMediaType(postSchemaJavaMethodSignature, "multipart/form-data")>
						<#assign generateGetMultipartFilesMethod = true />

						, getMultipartFiles()
					</#if>

					);
				<#else>
					throw new UnsupportedOperationException("This method needs to be implemented");
				</#if>
			}
		<#elseif !freeMarkerTool.isReturnTypeRelatedSchema(javaMethodSignature, relatedSchemaNames)>
			@Test
			public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
				Assert.assertTrue(false);
			}
		</#if>

		<#assign generateTestGraphQLAddMethod = false />

		<#if configYAML.generateGraphQL && freeMarkerTool.hasHTTPMethod(javaMethodSignature, "delete") && stringUtil.equals(freeMarkerTool.getGraphQLPropertyName(javaMethodSignature, javaMethodSignatures), "delete" + schemaName)>

			@Test
			public void testGraphQL${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if !properties?keys?seq_contains("id")>
					Assert.assertTrue(false);
				<#else>
					<#assign generateTestGraphQLAddMethod = true />

					${schemaName} ${schemaVarName} = testGraphQL${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					Assert.assertTrue(
						JSONUtil.getValueAsBoolean(
							invokeGraphQLMutation(
								new GraphQLField(
									"delete${schemaName}",
									new HashMap<String, Object>() {
										{
											put(
												<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
													<#if stringUtil.equals(javaMethodParameter.parameterName, "id") || stringUtil.equals(javaMethodParameter.parameterName, "${schemaVarName}Id")>
														"${javaMethodParameter.parameterName}",
														<#if stringUtil.equals(properties.id, "String")>
															<@getQuotedString unquotedString="${schemaVarName}.getId()" />
														<#else>
															${schemaVarName}.getId()
														</#if>
													</#if>
												</#list>
											);
										}
									})),
							"JSONObject/data",
							"Object/delete${schemaName}"));
					<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "get" + javaMethodSignature.methodName?remove_beginning("delete"))>
						JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
							invokeGraphQLQuery(
								new GraphQLField(
									"${schemaName?uncap_first}",
									new HashMap<String, Object>() {
										{
											put(
												<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
													<#if stringUtil.equals(javaMethodParameter.parameterName, "id") || stringUtil.equals(javaMethodParameter.parameterName, "${schemaVarName}Id")>
														"${javaMethodParameter.parameterName}",
														<#if stringUtil.equals(properties.id, "String")>
															<@getQuotedString unquotedString="${schemaVarName}.getId()" />
														<#else>
															${schemaVarName}.getId()
														</#if>
													</#if>
												</#list>
											);
										}
									},
									new GraphQLField("id"))),
							"JSONArray/errors");

						Assert.assertTrue(errorsJSONArray.length() > 0);
					</#if>
				</#if>
			}
		<#elseif configYAML.generateGraphQL && freeMarkerTool.hasHTTPMethod(javaMethodSignature, "get") && javaMethodSignature.returnType?contains("Page<") && stringUtil.equals(freeMarkerTool.getGraphQLPropertyName(javaMethodSignature, javaMethodSignatures), schemaVarNames)>
			@Test
			public void testGraphQL${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if !properties?keys?seq_contains("id")>
					Assert.assertTrue(false);
				<#else>
					<#assign topLevel = javaMethodSignature.pathJavaMethodParameters?size == 0 />

					<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
						${javaMethodParameter.parameterType} ${javaMethodParameter.parameterName} = test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}();
					</#list>

					GraphQLField graphQLField = new GraphQLField(
						"${schemaVarNames}",
						new HashMap<String, Object>() {
							{
								<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
									<#if stringUtil.equals(javaMethodParameter.parameterName, "pagination")>
										put("page", 1);
										put("pageSize", 10);
									</#if>
								</#list>

								<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
									<#if stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
										put("siteKey", <@getQuotedString unquotedString="${javaMethodParameter.parameterName}" />);
									<#else>
										put("${javaMethodParameter.parameterName}",
											<#if stringUtil.equals(javaMethodParameter.parameterType, "java.lang.String")>
												<@getQuotedString unquotedString="${javaMethodParameter.parameterName}" />
											<#else>
												${javaMethodParameter.parameterName}
											</#if>
										);
									</#if>
								</#list>
							}
						},
						new GraphQLField("items", getGraphQLFields()),
						new GraphQLField("page"),
						new GraphQLField("totalCount"));

					JSONObject ${schemaVarNames}JSONObject = JSONUtil.getValueAsJSONObject(
						invokeGraphQLQuery(graphQLField),
						"JSONObject/data",
						"JSONObject/${schemaVarNames}");

					<#if topLevel>
						long totalCount = ${schemaVarNames}JSONObject.getLong("totalCount");
					<#else>
						Assert.assertEquals(0, ${schemaVarNames}JSONObject.get("totalCount"));
					</#if>

					<#assign generateTestGraphQLAddMethod = true />

					${schemaName} ${schemaVarName}1 = testGraphQL${javaMethodSignature.methodName?cap_first}_add${schemaName}();
					${schemaName} ${schemaVarName}2 = testGraphQL${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					${schemaVarNames}JSONObject = JSONUtil.getValueAsJSONObject(
						invokeGraphQLQuery(graphQLField),
						"JSONObject/data",
						"JSONObject/${schemaVarNames}");

					Assert.assertEquals(<#if topLevel>totalCount + </#if>2, ${schemaVarNames}JSONObject.getLong("totalCount"));

					<#if topLevel>
						assertContains(${schemaVarName}1, Arrays.asList(${schemaName}SerDes.toDTOs(${schemaVarNames}JSONObject.getString("items"))));
						assertContains(${schemaVarName}2, Arrays.asList(${schemaName}SerDes.toDTOs(${schemaVarNames}JSONObject.getString("items"))));
					<#else>
						assertEqualsIgnoringOrder(Arrays.asList(${schemaVarName}1, ${schemaVarName}2), Arrays.asList((${schemaName}SerDes.toDTOs(${schemaVarNames}JSONObject.getString("items")))));
					</#if>
				</#if>
			}
		<#elseif configYAML.generateGraphQL && freeMarkerTool.hasHTTPMethod(javaMethodSignature, "get") && javaMethodSignature.returnType?ends_with(schemaName)>
			<#assign missingGetterJavaMethodParametersMap = {} />

			@Test
			public void testGraphQL${javaMethodSignature.methodName?cap_first}() throws Exception {
				<#if properties?keys?seq_contains("id")>
					<#assign generateTestGraphQLAddMethod = true />

					${schemaName} ${schemaVarName} = testGraphQL${javaMethodSignature.methodName?cap_first}_add${schemaName}();

					Assert.assertTrue(
						equals(${schemaVarName},
						${schemaName}SerDes.toDTO(
							JSONUtil.getValueAsString(
								invokeGraphQLQuery(
									new GraphQLField(
										"${freeMarkerTool.getGraphQLPropertyName(javaMethodSignature, javaMethodSignatures)}",
										new HashMap<String, Object>() {
											{
												<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
													<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
														<#if stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
															put("${javaMethodParameter.parameterName}",
																<#if stringUtil.equals(properties.id, "String")>
																	<@getQuotedString unquotedString="${schemaVarName}.getId()" />
																<#else>
																	${schemaVarName}.getId()
																</#if>
															);
														<#elseif properties?keys?seq_contains(javaMethodParameter.parameterName)>
															<#if stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
																put("siteKey", <@getQuotedString unquotedString="${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()" />);
															<#else>
																put("${javaMethodParameter.parameterName}",
																	<#if stringUtil.equals(javaMethodParameter.parameterType, "java.lang.String")>
																		<@getQuotedString unquotedString="${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()" />
																	<#else>
																		${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
																	</#if>
																);
															</#if>
														<#else>
															<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

															<#if stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
																put("siteKey", <@getQuotedString unquotedString="testGraphQL${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()" />);
															<#else>
																put("${javaMethodParameter.parameterName}",
																	<#if stringUtil.equals(javaMethodParameter.parameterType, "java.lang.String")>
																		<@getQuotedString unquotedString="testGraphQL${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()" />
																	<#else>
																		testGraphQL${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
																	</#if>
																);
															</#if>
														</#if>
													</#if>
												</#list>
											}
										},
										getGraphQLFields())),
								"JSONObject/data",
								"Object/${freeMarkerTool.getGraphQLPropertyName(javaMethodSignature, javaMethodSignatures)}"))));
				<#else>
					Assert.assertTrue(true);
				</#if>
			}

			<@getTestGetterMethods
				javaMethodSignature=javaMethodSignature
				missingGetterJavaMethodParametersMap=missingGetterJavaMethodParametersMap
				testNamePrefix="testGraphQL"
			/>

			@Test
			public void testGraphQL${javaMethodSignature.methodName?cap_first}NotFound() throws Exception {
				<#if javaMethodSignature.javaMethodParameters?size != 0 && properties?keys?seq_contains("id")>
					<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if !stringUtil.equals(javaMethodParameter.parameterName, "siteId") && freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
							${javaMethodParameter.parameterType} irrelevant${javaMethodParameter.parameterName?cap_first} =
							<#if stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Boolean")>
								RandomTestUtil.randomBoolean();
							<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Integer")>
								RandomTestUtil.randomInt();
							<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Long")>
								RandomTestUtil.randomLong();
							<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Double")>
								RandomTestUtil.randomDouble();
							<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.String")>
								<@getQuotedString unquotedString="RandomTestUtil.randomString()" />;
							<#else>
								null;
							</#if>
						</#if>
					</#list>

					Assert.assertEquals(
						"Not Found",
						JSONUtil.getValueAsString(
							invokeGraphQLQuery(
								new GraphQLField(
									"${freeMarkerTool.getGraphQLPropertyName(javaMethodSignature, javaMethodSignatures)}",
									new HashMap<String, Object>() {
										{
											<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
												<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation)>
													<#if stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
														put("siteKey", <@getQuotedString unquotedString="irrelevantGroup.getGroupId()" />);
													<#else>
														put("${javaMethodParameter.parameterName}", irrelevant${javaMethodParameter.parameterName?cap_first});
													</#if>
												</#if>
											</#list>
										}
									},
									getGraphQLFields())),
							"JSONArray/errors", "Object/0", "JSONObject/extensions", "Object/code"));
				<#else>
					Assert.assertTrue(true);
				</#if>
				}
		<#elseif configYAML.generateGraphQL && freeMarkerTool.hasHTTPMethod(javaMethodSignature, "post") && stringUtil.equals(javaMethodSignature.methodName, "postSite" + schemaName) && !freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data")>
			@Test
			public void testGraphQL${javaMethodSignature.methodName?cap_first}() throws Exception {
				${schemaName} random${schemaName} = random${schemaName}();

				${schemaName} ${schemaVarName} = testGraphQL${schemaName}_add${schemaName}(random${schemaName});

				Assert.assertTrue(equals(random${schemaName}, ${schemaVarName}));
			}
		</#if>

		<#if generateTestGraphQLAddMethod>
			protected ${schemaName} testGraphQL${javaMethodSignature.methodName?cap_first}_add${schemaName}() throws Exception {
				return testGraphQL${schemaName}_add${schemaName}();
			}
		</#if>
	</#list>

	<#if generateSearchTestRule>
		@Rule
		public SearchTestRule searchTestRule = new SearchTestRule();
	</#if>

	<#list relatedSchemaNames as relatedSchemaName>
		<#assign
			relatedSchemaProperties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, relatedSchemaName)
			relatedSchemaVarName = freeMarkerTool.getSchemaVarName(relatedSchemaName)
		/>

		<#list javaMethodSignatures as javaMethodSignature>
			<#if freeMarkerTool.hasHTTPMethod(javaMethodSignature, "get") && javaMethodSignature.returnType?ends_with("." + relatedSchemaName)>
				@Test
				public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
					${schemaName} post${schemaName} = testGet${schemaName}_add${schemaName}();

					${relatedSchemaName} post${relatedSchemaName} = test${javaMethodSignature.methodName?cap_first}_add${relatedSchemaName}(post${schemaName}.getId(), random${relatedSchemaName}());

					${relatedSchemaName} get${relatedSchemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(post${schemaName}.getId());

					assertEquals(post${relatedSchemaName}, get${relatedSchemaName});
					assertValid(get${relatedSchemaName});
				}

				protected ${relatedSchemaName} test${javaMethodSignature.methodName?cap_first}_add${relatedSchemaName}(long ${schemaVarName}Id, ${relatedSchemaName} ${relatedSchemaVarName}) throws Exception {
					<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, javaMethodSignature.methodName?replace("get", "post"))>
						return ${schemaVarName}Resource.${javaMethodSignature.methodName?replace("get", "post")}(${schemaVarName}Id, ${relatedSchemaVarName});
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "patch") && javaMethodSignature.returnType?ends_with("." + relatedSchemaName)>
				@Test
				public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
					${schemaName} post${schemaName} = testPatch${schemaName}_add${schemaName}();

					test${javaMethodSignature.methodName?cap_first}_add${relatedSchemaName}(post${schemaName}.getId(), random${relatedSchemaName}());

					${relatedSchemaName} random${relatedSchemaName} = random${relatedSchemaName}();

					${relatedSchemaName} patch${relatedSchemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								post${schemaName}.getId()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, relatedSchemaVarName)>
								random${relatedSchemaName}
							<#else>
								null
							</#if>

							<#sep>, </#sep>
						</#list>
					);

					assertEquals(random${relatedSchemaName}, patch${relatedSchemaName});
					assertValid(patch${relatedSchemaName});
				}

				protected ${relatedSchemaName} test${javaMethodSignature.methodName?cap_first}_add${relatedSchemaName}(long ${schemaVarName}Id, ${relatedSchemaName} ${relatedSchemaVarName}) throws Exception {
					<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, javaMethodSignature.methodName?replace("patch", "post"))>
						return ${schemaVarName}Resource.${javaMethodSignature.methodName?replace("patch", "post")}(${schemaVarName}Id, ${relatedSchemaVarName});
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "post") && javaMethodSignature.returnType?ends_with("." + relatedSchemaName)>
				@Test
				public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
					Assert.assertTrue(true);
				}
			<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "put") && javaMethodSignature.returnType?ends_with("." + relatedSchemaName)>
				@Test
				public void test${javaMethodSignature.methodName?cap_first}() throws Exception {
					${schemaName} post${schemaName} = testPut${schemaName}_add${schemaName}();

					test${javaMethodSignature.methodName?cap_first}_add${relatedSchemaName}(post${schemaName}.getId(), random${relatedSchemaName}());

					${relatedSchemaName} random${relatedSchemaName} = random${relatedSchemaName}();

					${relatedSchemaName} put${relatedSchemaName} = ${schemaVarName}Resource.${javaMethodSignature.methodName}(
						<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
							<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
								post${schemaName}.getId()
							<#elseif stringUtil.equals(javaMethodParameter.parameterName, relatedSchemaVarName)>
								random${relatedSchemaName}
							<#else>
								null
							</#if>

							<#sep>, </#sep>
						</#list>
					);

					assertEquals(random${relatedSchemaName}, put${relatedSchemaName});
					assertValid(put${relatedSchemaName});
				}

				protected ${relatedSchemaName} test${javaMethodSignature.methodName?cap_first}_add${relatedSchemaName}(long ${schemaVarName}Id, ${relatedSchemaName} ${relatedSchemaVarName}) throws Exception {
					<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, javaMethodSignature.methodName?replace("put", "post"))>
						return ${schemaVarName}Resource.${javaMethodSignature.methodName?replace("put", "post")}(${schemaVarName}Id, ${relatedSchemaVarName});
					<#else>
						throw new UnsupportedOperationException("This method needs to be implemented");
					</#if>
				}
			</#if>
		</#list>
	</#list>

	<#if properties?keys?seq_contains("id")>
		<#if freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "postSite" + schemaName)>
			protected void appendGraphQLFieldValue(StringBuilder sb, Object value) throws Exception {
				if (value instanceof Object[]) {
					StringBuilder arraySB = new StringBuilder("[");

					for (Object object : (Object[])value) {
						if (arraySB.length() > 1) {
							arraySB.append(", ");
						}

						arraySB.append("{");

						Class<?> clazz = object.getClass();

						for (java.lang.reflect.Field field : getDeclaredFields(clazz.getSuperclass())) {
							arraySB.append(field.getName());
							arraySB.append(": ");

							appendGraphQLFieldValue(arraySB, field.get(object));

							arraySB.append(", ");
						}

						arraySB.setLength(arraySB.length() - 2);

						arraySB.append("}");
					}

					arraySB.append("]");

					sb.append(arraySB.toString());
				}
				else if (value instanceof String) {
					sb.append("\"");
					sb.append(value);
					sb.append("\"");
				}
				else {
					sb.append(value);
				}
			}

			protected ${schemaName} testGraphQL${schemaName}_add${schemaName}() throws Exception {
				return testGraphQL${schemaName}_add${schemaName}(random${schemaName}());
			}

			protected ${schemaName} testGraphQL${schemaName}_add${schemaName}(${schemaName} ${schemaVarName}) throws Exception {
				JSONDeserializer<${schemaName}> jsonDeserializer = JSONFactoryUtil.createJSONDeserializer();

				StringBuilder sb = new StringBuilder("{");

				for (java.lang.reflect.Field field : getDeclaredFields(${schemaName}.class)) {
					if (!ArrayUtil.contains(getAdditionalAssertFieldNames(), field.getName())) {
						continue;
					}

					if (sb.length() > 1) {
						sb.append(", ");
					}

					sb.append(field.getName());
					sb.append(": ");

					appendGraphQLFieldValue(sb, field.get(${schemaVarName}));
				}

				sb.append("}");

				List<GraphQLField> graphQLFields = getGraphQLFields();

				<#if properties?keys?seq_contains("externalReferenceCode")>
					graphQLFields.add(new GraphQLField("externalReferenceCode"));
				</#if>

				<#if properties?keys?seq_contains("id")>
					graphQLFields.add(new GraphQLField("id"));
				</#if>

				return jsonDeserializer.deserialize(
					JSONUtil.getValueAsString(
						invokeGraphQLMutation(
							new GraphQLField(
								"createSite${schemaName}",
								new HashMap<String, Object>() {
									{
										put("siteKey", <@getQuotedString unquotedString="testGroup.getGroupId()" />);
										put("${schemaVarName}", sb.toString());
									}
								},
								graphQLFields)),
						"JSONObject/data",
						"JSONObject/createSite${schemaName}"),
					${schemaName}.class);
			}
		<#else>
			protected ${schemaName} testGraphQL${schemaName}_add${schemaName}() throws Exception {
				throw new UnsupportedOperationException("This method needs to be implemented");
			}
		</#if>
	</#if>

	protected void assertContains(${schemaClientJavaType} ${schemaVarName}, List<${schemaClientJavaType}> ${schemaVarNames}) {
		boolean contains = false;

		for (${schemaClientJavaType} item : ${schemaVarNames}) {
			if (equals(${schemaVarName}, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(${schemaVarNames} + " does not contain " + ${schemaVarName}, contains);
	}

	protected void assertHttpResponseStatusCode(int expectedHttpResponseStatusCode, HttpInvoker.HttpResponse actualHttpResponse) {
		Assert.assertEquals(expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(${schemaClientJavaType} ${schemaVarName}1, ${schemaClientJavaType} ${schemaVarName}2) {
		Assert.assertTrue(${schemaVarName}1 + " does not equal " + ${schemaVarName}2, equals(${schemaVarName}1, ${schemaVarName}2));
	}

	protected void assertEquals(List<${schemaClientJavaType}> ${schemaVarNames}1, List<${schemaClientJavaType}> ${schemaVarNames}2) {
		Assert.assertEquals(${schemaVarNames}1.size(), ${schemaVarNames}2.size());

		for (int i = 0; i < ${schemaVarNames}1.size(); i++) {
			${schemaClientJavaType} ${schemaVarName}1 = ${schemaVarNames}1.get(i);
			${schemaClientJavaType} ${schemaVarName}2 = ${schemaVarNames}2.get(i);

			assertEquals(${schemaVarName}1, ${schemaVarName}2);
		}
	}

	<#list relatedSchemaNames as relatedSchemaName>
		<#assign
			relatedSchemaProperties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, relatedSchemaName)
			relatedSchemaVarName = freeMarkerTool.getSchemaVarName(relatedSchemaName)
		/>

		protected void assertEquals(${relatedSchemaName} ${relatedSchemaVarName}1, ${relatedSchemaName} ${relatedSchemaVarName}2) {
			Assert.assertTrue(${relatedSchemaVarName}1 + " does not equal " + ${relatedSchemaVarName}2, equals(${relatedSchemaVarName}1, ${relatedSchemaVarName}2));
		}
	</#list>

	protected void assertEqualsIgnoringOrder(List<${schemaClientJavaType}> ${schemaVarNames}1, List<${schemaClientJavaType}> ${schemaVarNames}2) {
		Assert.assertEquals(${schemaVarNames}1.size(), ${schemaVarNames}2.size());

		for (${schemaClientJavaType} ${schemaVarName}1 : ${schemaVarNames}1) {
			boolean contains = false;

			for (${schemaClientJavaType} ${schemaVarName}2 : ${schemaVarNames}2) {
				if (equals(${schemaVarName}1, ${schemaVarName}2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(${schemaVarNames}2 + " does not contain " + ${schemaVarName}1, contains);
		}
	}

	protected void assertValid(${schemaClientJavaType} ${schemaVarName}) throws Exception {
		boolean valid = true;

		<#if properties?keys?seq_contains("dateCreated")>
			if (${schemaVarName}.getDateCreated() == null) {
				valid = false;
			}
		</#if>

		<#if properties?keys?seq_contains("dateModified")>
			if (${schemaVarName}.getDateModified() == null) {
				valid = false;
			}
		</#if>

		<#if properties?keys?seq_contains("id")>
			if (${schemaVarName}.getId() == null) {
				valid = false;
			}
		</#if>

		<#if properties?keys?seq_contains("siteId")>
			<#if generateDepotEntry>
				Group group = testDepotEntry.getGroup();

				if (!Objects.equals(${schemaVarName}.getAssetLibraryKey(), group.getGroupKey()) && !Objects.equals(${schemaVarName}.getSiteId(), testGroup.getGroupId())) {
					valid = false;
				}
			<#else>
				if (!Objects.equals(${schemaVarName}.getSiteId(), testGroup.getGroupId())) {
					valid = false;
				}
			</#if>
		</#if>

		for (String additionalAssertFieldName : getAdditionalAssertFieldNames()) {
			<#list properties?keys as propertyName>
				<#if stringUtil.equals(propertyName, "dateCreated") ||
					 stringUtil.equals(propertyName, "dateModified") ||
					 stringUtil.equals(propertyName, "id") ||
					 stringUtil.equals(propertyName, "siteId")>

					 <#continue>
				</#if>

				if (Objects.equals("${propertyName}", additionalAssertFieldName)) {
					<#assign capitalizedPropertyName = propertyName?cap_first />

					<#if enumSchemas?keys?seq_contains(properties[propertyName])>
						<#assign capitalizedPropertyName = properties[propertyName] />
					</#if>

					if (${schemaVarName}.get${capitalizedPropertyName}() == null) {
						valid = false;
					}

					continue;
				}
			</#list>

			throw new IllegalArgumentException("Invalid additional assert field name " + additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	<#if generateGetMultipartFilesMethod>
		protected void assertValid(${schemaClientJavaType} ${schemaVarName}, Map<String, File> multipartFiles) throws Exception {
			throw new UnsupportedOperationException("This method needs to be implemented");
		}
	</#if>

	protected void assertValid(Page<${schemaClientJavaType}> page) {
		boolean valid = false;

		java.util.Collection<${schemaClientJavaType}> ${schemaVarNames} = page.getItems();

		int size = ${schemaVarNames}.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) && (page.getPageSize() > 0) && (page.getTotalCount() > 0) && (size > 0)) {
			valid = true;
		}

		Assert.assertTrue(valid);
	}

	<#list relatedSchemaNames as relatedSchemaName>
		<#assign
			relatedSchemaProperties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, relatedSchemaName)
			relatedSchemaVarName = freeMarkerTool.getSchemaVarName(relatedSchemaName)
		/>

		protected void assertValid(${configYAML.apiPackagePath}.client.dto.${escapedVersion}.${relatedSchemaName} ${relatedSchemaVarName}) {
			boolean valid = true;

			<#if relatedSchemaProperties?keys?seq_contains("dateCreated")>
				if (${relatedSchemaVarName}.getDateCreated() == null) {
					valid = false;
				}
			</#if>

			<#if relatedSchemaProperties?keys?seq_contains("dateModified")>
				if (${relatedSchemaVarName}.getDateModified() == null) {
					valid = false;
				}
			</#if>

			<#if relatedSchemaProperties?keys?seq_contains("id")>
				if (${relatedSchemaVarName}.getId() == null) {
					valid = false;
				}
			</#if>

			<#if relatedSchemaProperties?keys?seq_contains("siteId")>
				if (!Objects.equals(${relatedSchemaVarName}.getSiteId(), testGroup.getGroupId())) {
					valid = false;
				}
			</#if>

			for (String additionalAssertFieldName : getAdditional${relatedSchemaName}AssertFieldNames()) {
				<#list relatedSchemaProperties?keys as propertyName>
					<#if stringUtil.equals(propertyName, "dateCreated") ||
						 stringUtil.equals(propertyName, "dateModified") ||
						 stringUtil.equals(propertyName, "id") ||
						 stringUtil.equals(propertyName, "siteId")>

						 <#continue>
					</#if>

					if (Objects.equals("${propertyName}", additionalAssertFieldName)) {
						<#assign capitalizedPropertyName = propertyName?cap_first />

						<#if enumSchemas?keys?seq_contains(relatedSchemaProperties[propertyName])>
							<#assign capitalizedPropertyName = relatedSchemaProperties[propertyName] />
						</#if>

						if (${relatedSchemaVarName}.get${capitalizedPropertyName}() == null) {
							valid = false;
						}

						continue;
					}
				</#list>

				throw new IllegalArgumentException("Invalid additional assert field name " + additionalAssertFieldName);
			}

			Assert.assertTrue(valid);
		}
	</#list>

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	<#list relatedSchemaNames as relatedSchemaName>
		protected String[] getAdditional${relatedSchemaName}AssertFieldNames() {
			return new String[0];
		}
	</#list>

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		<#if properties?keys?seq_contains("siteId")>
			graphQLFields.add(new GraphQLField("siteId"));
		</#if>

		<#if freeMarkerTool.getJavaDataType(configYAML, openAPIYAML, schemaName)??>
			for (java.lang.reflect.Field field : getDeclaredFields(${freeMarkerTool.getJavaDataType(configYAML, openAPIYAML, schemaName)}.class)) {
				if (!ArrayUtil.contains(getAdditionalAssertFieldNames(), field.getName())){
					continue;
				}

				graphQLFields.addAll(getGraphQLFields(field));
			}
		</#if>

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(java.lang.reflect.Field... fields) throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField vulcanGraphQLField = field.getAnnotation(com.liferay.portal.vulcan.graphql.annotation.GraphQLField.class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(getDeclaredFields(clazz));

				graphQLFields.add(new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(${schemaClientJavaType} ${schemaVarName}1, ${schemaClientJavaType} ${schemaVarName}2) {
		if (${schemaVarName}1 == ${schemaVarName}2) {
			return true;
		}

		<#if !properties?keys?seq_contains("assetLibraryKey") && properties?keys?seq_contains("siteId")>
			if (!Objects.equals(${schemaVarName}1.getSiteId(), ${schemaVarName}2.getSiteId())) {
				return false;
			}
		</#if>

		for (String additionalAssertFieldName : getAdditionalAssertFieldNames()) {
			<#list properties?keys as propertyName>
				<#if stringUtil.equals(propertyName, "assetLibraryKey") || stringUtil.equals(propertyName, "siteId")>
					 <#continue>
				</#if>

				if (Objects.equals("${propertyName}", additionalAssertFieldName)) {
					<#assign capitalizedPropertyName = propertyName?cap_first />

					<#if enumSchemas?keys?seq_contains(properties[propertyName])>
						<#assign capitalizedPropertyName = properties[propertyName] />
					</#if>

					<#if stringUtil.startsWith(properties[propertyName], "Map<")>
						if (!equals((Map)${schemaVarName}1.get${capitalizedPropertyName}(), (Map)${schemaVarName}2.get${capitalizedPropertyName}())) {
							return false;
						}
					<#else>
						if (!Objects.deepEquals(${schemaVarName}1.get${capitalizedPropertyName}(), ${schemaVarName}2.get${capitalizedPropertyName}())) {
							return false;
						}
					</#if>

					continue;
				}
			</#list>

			throw new IllegalArgumentException("Invalid additional assert field name " + additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(Map<String, Object> map1, Map<String, Object> map2) {
		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals((Map)entry.getValue(), (Map)map2.get(entry.getKey()))) {
						return false;
					}
				}
				else if (!Objects.deepEquals(entry.getValue(), map2.get(entry.getKey()))){
					return false;
				}
			}

			return true;
		}

		return false;
	}

	<#list relatedSchemaNames as relatedSchemaName>
		<#assign
			relatedSchemaProperties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, relatedSchemaName)
			relatedSchemaVarName = freeMarkerTool.getSchemaVarName(relatedSchemaName)
		/>

		protected boolean equals(${relatedSchemaName} ${relatedSchemaVarName}1, ${relatedSchemaName} ${relatedSchemaVarName}2) {
			if (${relatedSchemaVarName}1 == ${relatedSchemaVarName}2) {
				return true;
			}

			for (String additionalAssertFieldName : getAdditional${relatedSchemaName}AssertFieldNames()) {
				<#list relatedSchemaProperties?keys as propertyName>
					if (Objects.equals("${propertyName}", additionalAssertFieldName)) {
						<#assign capitalizedPropertyName = propertyName?cap_first />

						<#if enumSchemas?keys?seq_contains(relatedSchemaProperties[propertyName])>
							<#assign capitalizedPropertyName = relatedSchemaProperties[propertyName] />
						</#if>

						if (!Objects.deepEquals(${relatedSchemaVarName}1.get${capitalizedPropertyName}(), ${relatedSchemaVarName}2.get${capitalizedPropertyName}())) {
							return false;
						}

						continue;
					}
				</#list>

				throw new IllegalArgumentException("Invalid additional assert field name " + additionalAssertFieldName);
			}

			return true;
		}
	</#list>

	protected java.lang.reflect.Field[] getDeclaredFields(Class clazz) throws Exception {
		Stream<java.lang.reflect.Field> stream = Stream.of(ReflectionUtil.getDeclaredFields(clazz));

		return stream.filter(
			field -> !field.isSynthetic()
		).toArray(java.lang.reflect.Field[]::new);
	}

	protected java.util.Collection<EntityField> getEntityFields() throws Exception {
		if (!(_${schemaVarName}Resource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException("Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource = (EntityModelResource)_${schemaVarName}Resource;

		EntityModel entityModel = entityModelResource.getEntityModel(new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap = entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type) throws Exception {
		java.util.Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField -> Objects.equals(entityField.getType(), type) && !ArrayUtil.contains(getIgnoredEntityFieldNames(), entityField.getName())
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(EntityField entityField, String operator, ${schemaClientJavaType} ${schemaVarName}) {
		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		<#list properties?keys as propertyName>
			if (entityFieldName.equals("${propertyName}")) {
				<#if stringUtil.equals(properties[propertyName], "Date")>
					if (operator.equals("between")) {
						sb = new StringBundler();

						sb.append("(");
						sb.append(entityFieldName);
						sb.append(" gt ");
						sb.append(_dateFormat.format(DateUtils.addSeconds(${schemaVarName}.get${propertyName?cap_first}(), -2)));
						sb.append(" and ");
						sb.append(entityFieldName);
						sb.append(" lt ");
						sb.append(_dateFormat.format(DateUtils.addSeconds(${schemaVarName}.get${propertyName?cap_first}(), 2)));
						sb.append(")");
					}
					else {
						sb.append(entityFieldName);

						sb.append(" ");
						sb.append(operator);
						sb.append(" ");

						sb.append(_dateFormat.format(${schemaVarName}.get${propertyName?cap_first}()));
					}

					return sb.toString();
				<#elseif stringUtil.equals(properties[propertyName], "Double")>
					sb.append(String.valueOf(${schemaVarName}.get${propertyName?cap_first}()));

					return sb.toString();
				<#elseif stringUtil.equals(properties[propertyName], "Integer")>
					sb.append(String.valueOf(${schemaVarName}.get${propertyName?cap_first}()));

					return sb.toString();
				<#elseif stringUtil.equals(properties[propertyName], "String")>
					sb.append("'");
					sb.append(String.valueOf(${schemaVarName}.get${propertyName?cap_first}()));
					sb.append("'");

					return sb.toString();
				<#else>
					throw new IllegalArgumentException("Invalid entity field " + entityFieldName);
				</#if>
			}
		</#list>

		throw new IllegalArgumentException("Invalid entity field " + entityFieldName);
	}

	<#if generateGetMultipartFilesMethod>
		protected Map<String, File> getMultipartFiles() throws Exception {
			throw new UnsupportedOperationException("This method needs to be implemented");
		}
	</#if>

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField) throws Exception {
		GraphQLField mutationGraphQLField = new GraphQLField("mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField) throws Exception {
		GraphQLField queryGraphQLField = new GraphQLField("query", graphQLField);

		return JSONFactoryUtil.createJSONObject(invoke(queryGraphQLField.toString()));
	}

	<#if javaDataTypeMap?keys?seq_contains(schemaName)>
		protected ${schemaName} random${schemaName}() throws Exception {
			return new ${schemaName}() {
				{
					<#list properties?keys as propertyName>
						<#if stringUtil.equals(propertyName, "siteId")>
							${propertyName} = testGroup.getGroupId();
						<#elseif stringUtil.equals(properties[propertyName], "Integer")>
							${propertyName} = RandomTestUtil.randomInt();
						<#elseif propertyName?contains("email") && stringUtil.equals(properties[propertyName], "String")>
							${propertyName} = StringUtil.toLowerCase(RandomTestUtil.randomString()) + "@liferay.com";
						<#elseif stringUtil.equals(properties[propertyName], "String")>
							${propertyName} = StringUtil.toLowerCase(RandomTestUtil.randomString());
						<#elseif randomDataTypes?seq_contains(properties[propertyName])>
							${propertyName} = RandomTestUtil.random${properties[propertyName]}();
						<#elseif stringUtil.equals(properties[propertyName], "Date")>
							${propertyName} = RandomTestUtil.nextDate();
						</#if>
					</#list>
				}
			};
		}

		protected ${schemaName} randomIrrelevant${schemaName}() throws Exception {
			${schemaName} randomIrrelevant${schemaName} = random${schemaName}();

			<#if properties?keys?seq_contains("siteId")>
				randomIrrelevant${schemaName}.setSiteId(irrelevantGroup.getGroupId());
			</#if>

			return randomIrrelevant${schemaName};
		}

		protected ${schemaName} randomPatch${schemaName}() throws Exception {
			return random${schemaName}();
		}
	</#if>

	<#list relatedSchemaNames as relatedSchemaName>
		protected ${relatedSchemaName} random${relatedSchemaName}() throws Exception {
			return new ${relatedSchemaName}() {
				{
					<#assign relatedSchemaProperties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, relatedSchemaName) />

					<#list relatedSchemaProperties?keys as propertyName>
						<#if randomDataTypes?seq_contains(relatedSchemaProperties[propertyName])>
							${propertyName} = RandomTestUtil.random${relatedSchemaProperties[propertyName]}();
						<#elseif stringUtil.equals(relatedSchemaProperties[propertyName], "Date")>
							${propertyName} = RandomTestUtil.nextDate();
						</#if>
					</#list>
				}
			};
		}
	</#list>

	protected ${schemaName}Resource ${schemaVarName}Resource;
	protected Group irrelevantGroup;
	protected Company testCompany;

	<#if generateDepotEntry>
		protected DepotEntry testDepotEntry;
	</#if>

	protected Group testGroup;

	protected static class BeanTestUtil {

		public static void copyProperties(Object source, Object target) throws Exception {
			Class<?> sourceClass = _getSuperClass(source.getClass());

			Class<?> targetClass = target.getClass();

			for (java.lang.reflect.Field field : sourceClass.getDeclaredFields()) {
				if (field.isSynthetic()) {
					continue;
				}

				Method getMethod = _getMethod(sourceClass, field.getName(), "get");

				Method setMethod = _getMethod(targetClass, field.getName(), "set", getMethod.getReturnType());

				setMethod.invoke(target, getMethod.invoke(source));
			}
		}

		public static boolean hasProperty(Object bean, String name) {
			Method setMethod = _getMethod(bean.getClass(), "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod != null) {
				return true;
			}

			return false;
		}

		public static void setProperty(Object bean, String name, Object value) throws Exception {
			Class<?> clazz = bean.getClass();

			Method setMethod = _getMethod(clazz, "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod == null) {
				throw new NoSuchMethodException();
			}

			Class<?>[] parameterTypes = setMethod.getParameterTypes();

			setMethod.invoke(bean, _translateValue(parameterTypes[0], value));
		}

		private static Method _getMethod(Class<?> clazz, String name) {
			for (Method method : clazz.getMethods()) {
				if (name.equals(method.getName()) &&
					(method.getParameterCount() == 1) &&
					_parameterTypes.contains(method.getParameterTypes()[0])) {

					return method;
				}
			}

			return null;
		}

		private static Method _getMethod(Class<?> clazz, String fieldName, String prefix, Class<?>... parameterTypes) throws Exception {
			return clazz.getMethod(prefix + StringUtil.upperCaseFirstLetter(fieldName), parameterTypes);
		}

		private static Class<?> _getSuperClass(Class<?> clazz) {
			Class<?> superClass = clazz.getSuperclass();

			if ((superClass == null) || (superClass == Object.class)) {
				return clazz;
			}

			return superClass;
		}

		private static Object _translateValue(Class<?> parameterType, Object value) {
			if ((value instanceof Integer) && parameterType.equals(Long.class)) {
				Integer intValue = (Integer)value;

				return intValue.longValue();
			}

			return value;
		}

		private static final Set<Class<?>> _parameterTypes = new HashSet<>(Arrays.asList(Boolean.class, Date.class, Double.class, Integer.class, Long.class, Map.class, String.class));

	}

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, Map<String, Object> parameterMap, GraphQLField... graphQLFields) {
			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(String key, Map<String, Object> parameterMap, List<GraphQLField> graphQLFields) {
			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
					_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final com.liferay.portal.kernel.log.Log _log = LogFactoryUtil.getLog(Base${schemaName}ResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private ${configYAML.apiPackagePath}.resource.${escapedVersion}.${schemaName}Resource _${schemaVarName}Resource;

}

<#macro getDefaultParameter
	javaMethodParameter
>
	<#if stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Double")>
		0D
	<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Integer")>
		0
	<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.Long")>
		0L
	<#elseif stringUtil.equals(javaMethodParameter.parameterType, "java.lang.String")>
		"-"
	<#else>
		null
	</#if>
</#macro>

<#macro getGetterParameters
	javaMethodSignature
>
	<#list javaMethodSignature.pathJavaMethodParameters as javaMethodParameter>
		<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
			put${schemaName}.getId()
		<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
			put${schemaName}.get${javaMethodParameter.parameterName?cap_first}()
		<#else>
			<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

			test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
		</#if>

		<#sep>, </#sep>
	</#list>
</#macro>

<#macro getPermissionParameter
	javaMethodParameter
	javaMethodSignature
	properties roleName
	schemaVarName
	schemaVarNameId
>
	<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
		 ${schemaVarNameId}
	<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
		 ${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
	<#elseif stringUtil.equals(javaMethodParameter.parameterName, "assetLibraryId")>
		 testDepotEntry.getDepotEntryId()
	<#elseif stringUtil.equals(javaMethodParameter.parameterName, "siteId")>
		testGroup.getGroupId()
	<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
		${schemaVarName}
	<#elseif stringUtil.equals(javaMethodParameter.parameterType, "[Lcom.liferay.portal.vulcan.permission.Permission;")>
		new Permission[] {
			new Permission() {
				{
					setActionIds(new String[] {
						<#nested>
					});
					setRoleName(${roleName});
				}
			}
		}
	<#else>
		null
	</#if>
</#macro>

<#macro getQuotedString
	unquotedString>
	"\"" + ${unquotedString} + "\""
</#macro>

<#macro getPutParameters
	hasMultipartFiles
	javaMethodSignature
	newSchemaVarNamePrefix
	schemaName
	schemaVarName
	schemaVarNamePrefix
>
	<#list javaMethodSignature.javaMethodParameters as javaMethodParameter>
		<#if freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id")>
			${schemaVarNamePrefix}${schemaName}.getId()
		<#elseif freeMarkerTool.isPathParameter(javaMethodParameter, javaMethodSignature.operation) && properties?keys?seq_contains(javaMethodParameter.parameterName)>
			${schemaVarNamePrefix}${schemaName}.get${javaMethodParameter.parameterName?cap_first}()
		<#elseif stringUtil.equals(javaMethodParameter.parameterName, "multipartBody") || stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
			${newSchemaVarNamePrefix}${schemaName}
		<#else>
			<#assign missingGetterJavaMethodParametersMap = missingGetterJavaMethodParametersMap + {javaMethodParameter.parameterName: javaMethodParameter} />

			test${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}()
		</#if>
		<#sep>, </#sep>
	</#list>

	<#if freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data") && !hasMultipartFiles>
		, getMultipartFiles()
	<#elseif freeMarkerTool.hasRequestBodyMediaType(javaMethodSignature, "multipart/form-data") && hasMultipartFiles>
		, multipartFiles
	</#if>
</#macro>

<#macro getTestGetterMethods
	javaMethodSignature
	missingGetterJavaMethodParametersMap
	testNamePrefix
>
	<#list missingGetterJavaMethodParametersMap?values as javaMethodParameter>
		protected ${javaMethodParameter.parameterType} ${testNamePrefix}${javaMethodSignature.methodName?cap_first}_get${javaMethodParameter.parameterName?cap_first}() throws Exception {
			throw new UnsupportedOperationException("This method needs to be implemented");
		}
	</#list>
</#macro>