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

package com.liferay.segments.web.internal.odata;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.ComplexEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.IntegerEntityField;
import com.liferay.portal.odata.entity.StringEntityField;
import com.liferay.portal.odata.filter.expression.BinaryExpression;
import com.liferay.portal.odata.filter.expression.ComplexPropertyExpression;
import com.liferay.portal.odata.filter.expression.Expression;
import com.liferay.portal.odata.filter.expression.ExpressionVisitException;
import com.liferay.portal.odata.filter.expression.ExpressionVisitor;
import com.liferay.portal.odata.filter.expression.ListExpression;
import com.liferay.portal.odata.filter.expression.LiteralExpression;
import com.liferay.portal.odata.filter.expression.MemberExpression;
import com.liferay.portal.odata.filter.expression.MethodExpression;
import com.liferay.portal.odata.filter.expression.PrimitivePropertyExpression;
import com.liferay.portal.odata.filter.expression.PropertyExpression;
import com.liferay.portal.odata.filter.expression.UnaryExpression;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Cristina González
 */
public class ExpressionVisitorImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		_expressionVisitorImpl = new ExpressionVisitorImpl(0, _entityModel);
	}

	@Test
	public void testVisitBinaryExpressionOperationWithAndOperation()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		JSONObject jsonObject =
			(JSONObject)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.AND,
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, entityFieldsMap.get("title"),
					"title1"),
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.LT, entityFieldsMap.get("id"),
					"2"));

		Assert.assertEquals("and", jsonObject.getString("conjunctionName"));
		Assert.assertEquals("group_1", jsonObject.getString("groupId"));

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(
			JSONUtil.putAll(
				JSONUtil.put(
					"operatorName", "eq"
				).put(
					"propertyName", "title"
				).put(
					"value", "title1"
				),
				JSONUtil.put(
					"operatorName", "lt"
				).put(
					"propertyName", "id"
				).put(
					"value", "2"
				)
			).toString(),
			itemsJSONArray.toString());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithComplexEntityField()
		throws ExpressionVisitException {

		BinaryExpression binaryExpression = new BinaryExpression() {

			@Override
			public <T> T accept(ExpressionVisitor<T> expressionVisitor)
				throws ExpressionVisitException {

				Expression leftOperationExpression =
					getLeftOperationExpression();

				Expression rightOperationExpression =
					getRightOperationExpression();

				return expressionVisitor.visitBinaryExpressionOperation(
					getOperation(),
					leftOperationExpression.accept(expressionVisitor),
					rightOperationExpression.accept(expressionVisitor));
			}

			@Override
			public Expression getLeftOperationExpression() {
				return new MemberExpression() {

					@Override
					public <T> T accept(ExpressionVisitor<T> expressionVisitor)
						throws ExpressionVisitException {

						return expressionVisitor.visitMemberExpression(this);
					}

					public Expression getExpression() {
						return new ComplexPropertyExpression() {

							@Override
							public <T> T accept(
									ExpressionVisitor<T> expressionVisitor)
								throws ExpressionVisitException {

								return expressionVisitor.
									visitComplexPropertyExpression(this);
							}

							@Override
							public String getName() {
								return "complexField";
							}

							@Override
							public PropertyExpression getPropertyExpression() {
								return new PrimitivePropertyExpression() {

									@Override
									public <T> T accept(
											ExpressionVisitor<T>
												expressionVisitor)
										throws ExpressionVisitException {

										return expressionVisitor.
											visitPrimitivePropertyExpression(
												this);
									}

									@Override
									public String getName() {
										return "fieldInsideComplexField";
									}

								};
							}

						};
					}

				};
			}

			@Override
			public Operation getOperation() {
				return Operation.EQ;
			}

			@Override
			public Expression getRightOperationExpression() {
				return new LiteralExpression() {

					@Override
					public <T> T accept(ExpressionVisitor<T> expressionVisitor)
						throws ExpressionVisitException {

						return expressionVisitor.visitLiteralExpression(this);
					}

					@Override
					public String getText() {
						return "complexFieldValue1";
					}

					@Override
					public Type getType() {
						return LiteralExpression.Type.STRING;
					}

				};
			}

		};

		JSONObject jsonObject = (JSONObject)binaryExpression.accept(
			_expressionVisitorImpl);

		Assert.assertEquals(
			JSONUtil.put(
				"operatorName", "eq"
			).put(
				"propertyName", "complexField/fieldInsideComplexField"
			).put(
				"value", "complexFieldValue1"
			).toString(),
			jsonObject.toString());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithEqualOperation()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		JSONObject jsonObject =
			(JSONObject)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.EQ, entityFieldsMap.get("title"),
				"title1");

		Assert.assertEquals(
			JSONUtil.put(
				"operatorName",
				StringUtil.toLowerCase(BinaryExpression.Operation.EQ.toString())
			).put(
				"propertyName", "title"
			).put(
				"value", "title1"
			).toString(),
			jsonObject.toString());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithSameTitleNestedOperations()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		JSONObject jsonObject =
			(JSONObject)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.OR,
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, entityFieldsMap.get("title"),
					"title1"),
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.AND,
					_expressionVisitorImpl.visitBinaryExpressionOperation(
						BinaryExpression.Operation.EQ,
						entityFieldsMap.get("title"), "title1"),
					_expressionVisitorImpl.visitBinaryExpressionOperation(
						BinaryExpression.Operation.EQ,
						entityFieldsMap.get("title"), "title1")));

		Assert.assertEquals("or", jsonObject.getString("conjunctionName"));
		Assert.assertEquals("group_2", jsonObject.getString("groupId"));

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(
			JSONUtil.putAll(
				JSONUtil.put(
					"operatorName", "eq"
				).put(
					"propertyName", "title"
				).put(
					"value", "title1"
				),
				JSONUtil.put(
					"conjunctionName", "and"
				).put(
					"groupId", "group_1"
				).put(
					"items",
					JSONUtil.putAll(
						JSONUtil.put(
							"operatorName", "eq"
						).put(
							"propertyName", "title"
						).put(
							"value", "title1"
						),
						JSONUtil.put(
							"operatorName", "eq"
						).put(
							"propertyName", "title"
						).put(
							"value", "title1"
						))
				)
			).toString(),
			itemsJSONArray.toString());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithSameTitleUnnestedOperations()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		JSONObject jsonObject =
			(JSONObject)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.AND,
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.AND,
					_expressionVisitorImpl.visitBinaryExpressionOperation(
						BinaryExpression.Operation.EQ,
						entityFieldsMap.get("title"), "title1"),
					_expressionVisitorImpl.visitBinaryExpressionOperation(
						BinaryExpression.Operation.EQ,
						entityFieldsMap.get("title"), "title1")),
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, entityFieldsMap.get("title"),
					"title1"));

		Assert.assertEquals("and", jsonObject.getString("conjunctionName"));
		Assert.assertEquals("group_2", jsonObject.getString("groupId"));

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(
			JSONUtil.putAll(
				JSONUtil.put(
					"operatorName", "eq"
				).put(
					"propertyName", "title"
				).put(
					"value", "title1"
				),
				JSONUtil.put(
					"operatorName", "eq"
				).put(
					"propertyName", "title"
				).put(
					"value", "title1"
				),
				JSONUtil.put(
					"operatorName", "eq"
				).put(
					"propertyName", "title"
				).put(
					"value", "title1"
				)
			).toString(),
			itemsJSONArray.toString());
	}

	@Test
	public void testVisitListExpressionOperation()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		ListExpression listExpression = new ListExpression() {

			public <T> T accept(ExpressionVisitor<T> expressionVisitor)
				throws ExpressionVisitException {

				List<Object> objects = Arrays.asList("title1", "title2");

				return expressionVisitor.visitListExpressionOperation(
					Operation.IN, (T)entityFieldsMap.get("title"),
					(List<T>)objects);
			}

			@Override
			public Expression getLeftOperationExpression() {
				return null;
			}

			@Override
			public Operation getOperation() {
				return null;
			}

			@Override
			public List<Expression> getRightOperationExpressions() {
				return null;
			}

		};

		JSONObject jsonObject = (JSONObject)listExpression.accept(
			_expressionVisitorImpl);

		Assert.assertEquals(
			JSONUtil.put(
				"operatorName",
				StringUtil.toLowerCase(ListExpression.Operation.IN.toString())
			).put(
				"propertyName", "title"
			).put(
				"value", JSONUtil.putAll("title1", "title2")
			).toString(),
			jsonObject.toString());
	}

	@Test
	public void testVisitMethodExpressionWithContains()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		JSONObject jsonObject =
			(JSONObject)_expressionVisitorImpl.visitMethodExpression(
				Arrays.asList(entityFieldsMap.get("title"), "title1"),
				MethodExpression.Type.CONTAINS);

		Assert.assertEquals(
			JSONUtil.put(
				"operatorName",
				StringUtil.toLowerCase(
					MethodExpression.Type.CONTAINS.toString())
			).put(
				"propertyName", "title"
			).put(
				"value", "title1"
			).toString(),
			jsonObject.toString());
	}

	@Test
	public void testVisitUnaryExpressionOperation()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		JSONObject jsonObject =
			_expressionVisitorImpl.visitUnaryExpressionOperation(
				UnaryExpression.Operation.NOT,
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.GE, entityFieldsMap.get("id"),
					"4"));

		Assert.assertEquals(
			JSONUtil.put(
				"operatorName",
				StringUtil.toLowerCase(
					UnaryExpression.Operation.NOT + "-" +
						BinaryExpression.Operation.GE.toString())
			).put(
				"propertyName", "id"
			).put(
				"value", "4"
			).toString(),
			jsonObject.toString());
	}

	private static final EntityModel _entityModel = new EntityModel() {

		@Override
		public Map<String, EntityField> getEntityFieldsMap() {
			return Stream.of(
				new ComplexEntityField(
					"complexField",
					Collections.singletonList(
						new StringEntityField(
							"fieldInsideComplexField",
							locale -> "fieldInsideComplexFieldInternal"))),
				new IntegerEntityField("id", locale -> "id"),
				new StringEntityField("title", locale -> "title")
			).collect(
				Collectors.toMap(EntityField::getName, Function.identity())
			);
		}

		@Override
		public String getName() {
			return "SomeEntityName";
		}

	};

	private ExpressionVisitorImpl _expressionVisitorImpl;

}