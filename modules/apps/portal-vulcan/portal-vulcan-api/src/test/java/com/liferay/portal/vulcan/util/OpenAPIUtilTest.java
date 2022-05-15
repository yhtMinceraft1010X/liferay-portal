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

package com.liferay.portal.vulcan.util;

import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.vulcan.batch.engine.Field;
import com.liferay.portal.vulcan.yaml.openapi.Components;
import com.liferay.portal.vulcan.yaml.openapi.Content;
import com.liferay.portal.vulcan.yaml.openapi.Get;
import com.liferay.portal.vulcan.yaml.openapi.OpenAPIYAML;
import com.liferay.portal.vulcan.yaml.openapi.Parameter;
import com.liferay.portal.vulcan.yaml.openapi.PathItem;
import com.liferay.portal.vulcan.yaml.openapi.Post;
import com.liferay.portal.vulcan.yaml.openapi.Response;
import com.liferay.portal.vulcan.yaml.openapi.ResponseCode;
import com.liferay.portal.vulcan.yaml.openapi.Schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Javier de Arcos
 */
public class OpenAPIUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		Schema schema = new Schema() {
			{
				setPropertySchemas(
					HashMapBuilder.<String, Schema>put(
						"readOnlyPropertySchema",
						new Schema() {
							{
								setDescription("Read only property");
								setReadOnly(true);
								setType("string");
							}
						}
					).put(
						"requiredPropertySchema",
						new Schema() {
							{
								setDescription("Required property");
								setType("string");
							}
						}
					).put(
						"writeOnlyPropertySchema",
						new Schema() {
							{
								setDescription("Write only property");
								setType("integer");
								setWriteOnly(true);
							}
						}
					).build());
				setRequiredPropertySchemaNames(
					Collections.singletonList("requiredPropertySchema"));
			}
		};

		_openAPIYAML.setComponents(
			new Components() {
				{
					setSchemas(Collections.singletonMap("Test", schema));
				}
			});

		Response getResponse = new Response();

		getResponse.setContent(
			Collections.singletonMap(
				"application/json",
				new Content() {
					{
						setSchema(
							new Schema() {
								{
									setReference(
										"#/components/schema/PageTest");
								}
							});
					}
				}));

		Parameter ignoredParameter = new Parameter() {
			{
				setIn("path");
				setName("ignoredId");
			}
		};

		Response ignoredResponse = new Response();

		ignoredResponse.setContent(
			Collections.singletonMap(
				"application/json",
				new Content() {
					{
						setSchema(new Schema());
					}
				}));

		Response postResponse = new Response();

		postResponse.setContent(
			Collections.singletonMap(
				"application/json",
				new Content() {
					{
						setSchema(
							new Schema() {
								{
									setReference("#/components/schema/Test");
								}
							});
					}
				}));

		_openAPIYAML.setPathItems(
			HashMapBuilder.<String, PathItem>put(
				"/{ignoredId}/tests",
				() -> new PathItem() {
					{
						setGet(
							new Get() {
								{
									setParameters(
										Collections.singletonList(
											ignoredParameter));
									setResponses(
										Collections.singletonMap(
											new ResponseCode("200"),
											ignoredResponse));
								}
							});
						setPost(
							new Post() {
								{
									setParameters(
										Collections.singletonList(
											ignoredParameter));
								}
							});
					}
				}
			).put(
				"/{scopeId}/tests",
				() -> new PathItem() {
					{
						setGet(
							new Get() {
								{
									setParameters(
										Collections.singletonList(
											new Parameter() {
												{
													setIn("path");
													setName("scopeId");
												}
											}));
									setResponses(
										Collections.singletonMap(
											new ResponseCode("200"),
											getResponse));
								}
							});
						setPost(
							new Post() {
								{
									setParameters(
										Arrays.asList(
											new Parameter() {
												{
													setIn("query");
													setName("query");
												}
											},
											new Parameter() {
												{
													setIn("path");
													setName("scopeId");
												}
											}));
									setResponses(
										Collections.singletonMap(
											new ResponseCode("default"),
											postResponse));
								}
							});
					}
				}
			).build());
	}

	@Test
	public void testGetCreateEntityScopes() {
		List<String> createEntityScopes = OpenAPIUtil.getCreateEntityScopes(
			"NonExistent", _openAPIYAML);

		Assert.assertTrue(createEntityScopes.isEmpty());

		createEntityScopes = OpenAPIUtil.getCreateEntityScopes(
			"Test", _openAPIYAML);

		Assert.assertEquals(
			Collections.singletonList("scope"), createEntityScopes);
	}

	@Test
	public void testGetDTOFields() {
		Map<String, Field> fields = OpenAPIUtil.getDTOEntityFields(
			"NonExistent", _openAPIYAML);

		Assert.assertTrue(fields.isEmpty());

		fields = OpenAPIUtil.getDTOEntityFields("Test", _openAPIYAML);

		Assert.assertEquals(fields.toString(), 3, fields.size());

		_assertField(
			Field.AccessType.READ, "Read only property",
			"readOnlyPropertySchema", fields.get("readOnlyPropertySchema"),
			false, "string");
		_assertField(
			Field.AccessType.READWRITE, "Required property",
			"requiredPropertySchema", fields.get("requiredPropertySchema"),
			true, "string");
		_assertField(
			Field.AccessType.WRITE, "Write only property",
			"writeOnlyPropertySchema", fields.get("writeOnlyPropertySchema"),
			false, "integer");
	}

	@Test
	public void testGetReadEntityScopes() {
		List<String> readEntityScopes = OpenAPIUtil.getReadEntityScopes(
			"NonExistent", _openAPIYAML);

		Assert.assertTrue(readEntityScopes.isEmpty());

		readEntityScopes = OpenAPIUtil.getReadEntityScopes(
			"Test", _openAPIYAML);

		Assert.assertEquals(
			Collections.singletonList("scope"), readEntityScopes);
	}

	private void _assertField(
		Field.AccessType accessType, String description, String name,
		Field readOnlyPropertySchema, boolean required, String type) {

		Assert.assertEquals(accessType, readOnlyPropertySchema.getAccessType());
		Assert.assertEquals(
			description, readOnlyPropertySchema.getDescription());
		Assert.assertEquals(name, readOnlyPropertySchema.getName());
		Assert.assertEquals(type, readOnlyPropertySchema.getType());
		Assert.assertEquals(required, readOnlyPropertySchema.isRequired());
	}

	private final OpenAPIYAML _openAPIYAML = new OpenAPIYAML();

}