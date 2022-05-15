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

package com.liferay.portal.odata.internal.filter;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.QueryTerm;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.WildcardQuery;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.ExistsFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.odata.entity.CollectionEntityField;
import com.liferay.portal.odata.entity.ComplexEntityField;
import com.liferay.portal.odata.entity.DateEntityField;
import com.liferay.portal.odata.entity.DateTimeEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.StringEntityField;
import com.liferay.portal.odata.filter.expression.BinaryExpression;
import com.liferay.portal.odata.filter.expression.ExpressionVisitException;
import com.liferay.portal.odata.filter.expression.LambdaFunctionExpression;
import com.liferay.portal.odata.filter.expression.LiteralExpression;
import com.liferay.portal.odata.filter.expression.MemberExpression;
import com.liferay.portal.odata.filter.expression.MethodExpression;
import com.liferay.portal.odata.filter.expression.UnaryExpression;
import com.liferay.portal.odata.internal.filter.expression.BinaryExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.CollectionPropertyExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.ComplexPropertyExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.LambdaFunctionExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.LambdaVariableExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.LiteralExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.MemberExpressionImpl;
import com.liferay.portal.odata.internal.filter.expression.PrimitivePropertyExpressionImpl;
import com.liferay.portal.search.internal.query.NestedFieldQueryHelperImpl;
import com.liferay.portal.search.query.NestedFieldQueryHelper;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Rubén Pulido
 */
public class ExpressionVisitorImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testVisitBinaryExpressionOperationWithAndOperation() {
		TermFilter leftTermFilter = new TermFilter("title", "title1");

		TermFilter rightTermFilter = new TermFilter("title", "title2");

		BooleanFilter booleanFilter =
			(BooleanFilter)
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.AND, leftTermFilter,
					rightTermFilter);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getMustBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 2, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause1 = booleanClauses.get(0);

		Assert.assertEquals(leftTermFilter, queryBooleanClause1.getClause());
		Assert.assertEquals(
			BooleanClauseOccur.MUST,
			queryBooleanClause1.getBooleanClauseOccur());

		BooleanClause<Filter> queryBooleanClause2 = booleanClauses.get(1);

		Assert.assertEquals(rightTermFilter, queryBooleanClause2.getClause());
		Assert.assertEquals(
			BooleanClauseOccur.MUST,
			queryBooleanClause2.getBooleanClauseOccur());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperationWithEqualOperation() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.EQ, entityField, value);

		TermQuery termQuery = (TermQuery)queryFilter.getQuery();

		QueryTerm queryTerm = termQuery.getQueryTerm();

		Assert.assertEquals(entityField.getName(), queryTerm.getField());
		Assert.assertEquals(value, queryTerm.getValue());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithEqualOperationAndNullValue() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		BooleanFilter booleanFilter =
			(BooleanFilter)
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, entityField, null);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getMustNotBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 1, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause = booleanClauses.get(0);

		QueryFilter queryFilter = (QueryFilter)queryBooleanClause.getClause();

		WildcardQuery wildcardQuery = (WildcardQuery)queryFilter.getQuery();

		QueryTerm queryTerm = wildcardQuery.getQueryTerm();

		Assert.assertEquals(entityField.getName(), queryTerm.getField());
		Assert.assertEquals("*", queryTerm.getValue());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithEqualOperationAndNullValueForDateField() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("date");

		BooleanFilter booleanFilter =
			(BooleanFilter)
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, entityField, null);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getMustNotBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 1, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause = booleanClauses.get(0);

		ExistsFilter existsFilter =
			(ExistsFilter)queryBooleanClause.getClause();

		Assert.assertEquals(entityField.getName(), existsFilter.getField());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithEqualOperationAndNullValueForDateTimeField() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("dateTime");

		BooleanFilter booleanFilter =
			(BooleanFilter)
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.EQ, entityField, null);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getMustNotBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 1, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause = booleanClauses.get(0);

		ExistsFilter existsFilter =
			(ExistsFilter)queryBooleanClause.getClause();

		Assert.assertEquals(entityField.getName(), existsFilter.getField());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperationWithGreaterEqualOperation() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.GE, entityField, value);

		TermRangeQuery termRangeQuery = (TermRangeQuery)queryFilter.getQuery();

		Assert.assertEquals(entityField.getName(), termRangeQuery.getField());
		Assert.assertEquals(value, termRangeQuery.getLowerTerm());
		Assert.assertTrue(termRangeQuery.includesLower());
		Assert.assertNull(termRangeQuery.getUpperTerm());
		Assert.assertTrue(termRangeQuery.includesUpper());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperationWithGreaterOperation() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.GT, entityField, value);

		TermRangeQuery termRangeQuery = (TermRangeQuery)queryFilter.getQuery();

		Assert.assertEquals(entityField.getName(), termRangeQuery.getField());
		Assert.assertEquals(value, termRangeQuery.getLowerTerm());
		Assert.assertFalse(termRangeQuery.includesLower());
		Assert.assertNull(termRangeQuery.getUpperTerm());
		Assert.assertTrue(termRangeQuery.includesUpper());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithGreaterOperationAndNullValue() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		AbstractThrowableAssert exception = Assertions.assertThatThrownBy(
			() -> _expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.GT, entityFieldsMap.get("title"),
				null)
		).isInstanceOf(
			UnsupportedOperationException.class
		);

		exception.hasMessage(
			"Unsupported method _getGTFilter with null values");
	}

	@Test
	public void testVisitBinaryExpressionOperationWithGreaterOrEqualOperationAndNullValue() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		AbstractThrowableAssert exception = Assertions.assertThatThrownBy(
			() -> _expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.GE, entityFieldsMap.get("title"),
				null)
		).isInstanceOf(
			UnsupportedOperationException.class
		);

		exception.hasMessage(
			"Unsupported method _getGEFilter with null values");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperationWithLowerEqualOperation() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.LE, entityField, value);

		TermRangeQuery termRangeQuery = (TermRangeQuery)queryFilter.getQuery();

		Assert.assertEquals(entityField.getName(), termRangeQuery.getField());
		Assert.assertNull(value, termRangeQuery.getLowerTerm());
		Assert.assertFalse(termRangeQuery.includesLower());
		Assert.assertEquals(value, termRangeQuery.getUpperTerm());
		Assert.assertTrue(termRangeQuery.includesUpper());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisitBinaryExpressionOperationWithLowerOperation() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.LT, entityField, value);

		TermRangeQuery termRangeQuery = (TermRangeQuery)queryFilter.getQuery();

		Assert.assertEquals(entityField.getName(), termRangeQuery.getField());
		Assert.assertEquals(value, termRangeQuery.getUpperTerm());
		Assert.assertNull(termRangeQuery.getLowerTerm());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithLowerOperationAndNullValue() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		AbstractThrowableAssert exception = Assertions.assertThatThrownBy(
			() -> _expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.LT, entityFieldsMap.get("title"),
				null)
		).isInstanceOf(
			UnsupportedOperationException.class
		);

		exception.hasMessage(
			"Unsupported method _getLTFilter with null values");
	}

	@Test
	public void testVisitBinaryExpressionOperationWithLowerOrEqualOperationAndNullValue() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		AbstractThrowableAssert exception = Assertions.assertThatThrownBy(
			() -> _expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.LE, entityFieldsMap.get("title"),
				null)
		).isInstanceOf(
			UnsupportedOperationException.class
		);

		exception.hasMessage(
			"Unsupported method _getLEFilter with null values");
	}

	@Test
	public void testVisitBinaryExpressionOperationWithNotEqualOperation() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		BooleanFilter booleanFilter =
			(BooleanFilter)
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.NE, entityField, value);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getMustNotBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 1, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause = booleanClauses.get(0);

		Assert.assertEquals(
			BooleanClauseOccur.MUST_NOT,
			queryBooleanClause.getBooleanClauseOccur());

		QueryFilter queryFilter = (QueryFilter)queryBooleanClause.getClause();

		TermQuery termQuery = (TermQuery)queryFilter.getQuery();

		QueryTerm queryTerm = termQuery.getQueryTerm();

		Assert.assertEquals(entityField.getName(), queryTerm.getField());
		Assert.assertEquals(value, queryTerm.getValue());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithNotEqualOperationAndNullValue() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.NE, entityField, null);

		WildcardQuery wildcardQuery = (WildcardQuery)queryFilter.getQuery();

		QueryTerm queryTerm = wildcardQuery.getQueryTerm();

		Assert.assertEquals(entityField.getName(), queryTerm.getField());
		Assert.assertEquals("*", queryTerm.getValue());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithNotEqualOperationAndNullValueForDateField() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("date");

		ExistsFilter existsFilter =
			(ExistsFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.NE, entityField, null);

		Assert.assertEquals(entityField.getName(), existsFilter.getField());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithNotEqualOperationAndNullValueForDateTimeField() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("dateTime");

		ExistsFilter existsFilter =
			(ExistsFilter)_expressionVisitorImpl.visitBinaryExpressionOperation(
				BinaryExpression.Operation.NE, entityField, null);

		Assert.assertEquals(entityField.getName(), existsFilter.getField());
	}

	@Test
	public void testVisitBinaryExpressionOperationWithOrOperation() {
		TermFilter leftTermFilter = new TermFilter("title", "title1");

		TermFilter rightTermFilter = new TermFilter("title", "title2");

		BooleanFilter booleanFilter =
			(BooleanFilter)
				_expressionVisitorImpl.visitBinaryExpressionOperation(
					BinaryExpression.Operation.OR, leftTermFilter,
					rightTermFilter);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getShouldBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 2, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause1 = booleanClauses.get(0);

		Assert.assertEquals(leftTermFilter, queryBooleanClause1.getClause());
		Assert.assertEquals(
			BooleanClauseOccur.SHOULD,
			queryBooleanClause1.getBooleanClauseOccur());

		BooleanClause<Filter> queryBooleanClause2 = booleanClauses.get(1);

		Assert.assertEquals(rightTermFilter, queryBooleanClause2.getClause());
		Assert.assertEquals(
			BooleanClauseOccur.SHOULD,
			queryBooleanClause2.getBooleanClauseOccur());
	}

	@Test
	public void testVisitDateISO8601LiteralExpression() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"2012-05-29T09:13:28Z", LiteralExpression.Type.DATE_TIME);

		Assert.assertEquals(
			"20120529091328",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitDateISOLiteralExpression() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"2012-05-29T11:58:16+00:00", LiteralExpression.Type.DATE_TIME);

		Assert.assertEquals(
			"20120529115816",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitDateUTCLiteralExpression() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"2012-05-29", LiteralExpression.Type.DATE);

		Assert.assertEquals(
			"20120529000000",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitLambdaFunctionExpressionAny()
		throws ExpressionVisitException {

		LambdaFunctionExpression lambdaFunctionExpression =
			new LambdaFunctionExpressionImpl(
				LambdaFunctionExpression.Type.ANY, "k",
				new BinaryExpressionImpl(
					new MemberExpressionImpl(
						new LambdaVariableExpressionImpl("k")),
					BinaryExpression.Operation.EQ,
					new LiteralExpressionImpl(
						"keyword1", LiteralExpression.Type.STRING)));

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		CollectionEntityField collectionEntityField =
			(CollectionEntityField)entityFieldsMap.get("keywords");

		ExpressionVisitorImpl expressionVisitorImpl = new ExpressionVisitorImpl(
			new SimpleDateFormat("yyyyMMddHHmmss"), LocaleUtil.getDefault(),
			new EntityModel() {

				@Override
				public Map<String, EntityField> getEntityFieldsMap() {
					return Collections.singletonMap(
						"k", collectionEntityField.getEntityField());
				}

				@Override
				public String getName() {
					return collectionEntityField.getName();
				}

			},
			nestedFieldQueryHelper);

		QueryFilter queryFilter =
			(QueryFilter)expressionVisitorImpl.visitLambdaFunctionExpression(
				lambdaFunctionExpression.getType(),
				lambdaFunctionExpression.getVariableName(),
				lambdaFunctionExpression.getExpression());

		TermQuery termQuery = (TermQuery)queryFilter.getQuery();

		QueryTerm queryTerm = termQuery.getQueryTerm();

		Assert.assertNotNull(queryTerm);
		Assert.assertEquals("keywords.raw", queryTerm.getField());
		Assert.assertEquals("keyword1", queryTerm.getValue());
	}

	@Test
	public void testVisitMemberExpressionComplexField()
		throws ExpressionVisitException {

		MemberExpression memberExpression = new MemberExpressionImpl(
			new ComplexPropertyExpressionImpl(
				"values", new PrimitivePropertyExpressionImpl("value1")));

		EntityField entityField =
			(EntityField)_expressionVisitorImpl.visitMemberExpression(
				memberExpression);

		Assert.assertNotNull(entityField);
		Assert.assertEquals("value1", entityField.getName());
		Assert.assertEquals(EntityField.Type.STRING, entityField.getType());
	}

	@Test
	public void testVisitMemberExpressionLambdaAnyOnCollectionField()
		throws ExpressionVisitException {

		MemberExpression memberExpression = new MemberExpressionImpl(
			new CollectionPropertyExpressionImpl(
				new PrimitivePropertyExpressionImpl("keywords"),
				new LambdaFunctionExpressionImpl(
					LambdaFunctionExpression.Type.ANY, "k",
					new BinaryExpressionImpl(
						new MemberExpressionImpl(
							new LambdaVariableExpressionImpl("k")),
						BinaryExpression.Operation.EQ,
						new LiteralExpressionImpl(
							"'keyword1'", LiteralExpression.Type.STRING)))));

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitMemberExpression(
				memberExpression);

		TermQuery termQuery = (TermQuery)queryFilter.getQuery();

		QueryTerm queryTerm = termQuery.getQueryTerm();

		Assert.assertNotNull(queryTerm);
		Assert.assertEquals("keywords.raw", queryTerm.getField());
		Assert.assertEquals("keyword1", queryTerm.getValue());
	}

	@Test
	public void testVisitMemberExpressionStringEntityField()
		throws ExpressionVisitException {

		MemberExpression memberExpression = new MemberExpressionImpl(
			new PrimitivePropertyExpressionImpl("title"));

		EntityField entityField =
			(EntityField)_expressionVisitorImpl.visitMemberExpression(
				memberExpression);

		Assert.assertNotNull(entityField);
		Assert.assertEquals("title", entityField.getName());
		Assert.assertEquals(EntityField.Type.STRING, entityField.getType());
	}

	@Test
	public void testVisitMemberExpressionStringEntityFieldInLambda()
		throws ExpressionVisitException {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField1 = entityFieldsMap.get("keywords");

		ExpressionVisitorImpl expressionVisitorImpl = new ExpressionVisitorImpl(
			new SimpleDateFormat("yyyyMMddHHmmss"), LocaleUtil.getDefault(),
			new EntityModel() {

				@Override
				public Map<String, EntityField> getEntityFieldsMap() {
					return Collections.singletonMap("k", entityField1);
				}

				@Override
				public String getName() {
					return entityField1.getName();
				}

			},
			nestedFieldQueryHelper);

		MemberExpression memberExpression = new MemberExpressionImpl(
			new LambdaVariableExpressionImpl("k"));

		EntityField entityField2 =
			(EntityField)expressionVisitorImpl.visitMemberExpression(
				memberExpression);

		Assert.assertNotNull(entityField2);
		Assert.assertEquals("keywords", entityField2.getName());
		Assert.assertEquals(
			EntityField.Type.COLLECTION, entityField2.getType());
	}

	@Test
	public void testVisitMethodExpressionWithStartsWith() {
		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get("title");

		String value = "title1";

		QueryFilter queryFilter =
			(QueryFilter)_expressionVisitorImpl.visitMethodExpression(
				Arrays.asList(Arrays.array(entityField, value)),
				MethodExpression.Type.STARTS_WITH);

		WildcardQuery wildcardQuery = (WildcardQuery)queryFilter.getQuery();

		QueryTerm queryTerm = wildcardQuery.getQueryTerm();

		Assert.assertEquals(entityField.getName(), queryTerm.getField());
		Assert.assertEquals(value + "*", queryTerm.getValue());
	}

	@Test
	public void testVisitStringLiteralExpressionWithDoubleSingleQuotes() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'L''Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"l'oreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitStringLiteralExpressionWithMultipleDoubleSingleQuotes() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'L''Oreal and L''Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"l'oreal and l'oreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitStringLiteralExpressionWithOneSingleQuote() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'L'Oreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"l'oreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitStringLiteralExpressionWithSurroundingSingleQuotes() {
		LiteralExpression literalExpression = new LiteralExpressionImpl(
			"'LOreal'", LiteralExpression.Type.STRING);

		Assert.assertEquals(
			"loreal",
			_expressionVisitorImpl.visitLiteralExpression(literalExpression));
	}

	@Test
	public void testVisitUnaryExpressionOperation() {
		TermFilter termFilter = new TermFilter("title", "title1");

		BooleanFilter booleanFilter =
			(BooleanFilter)_expressionVisitorImpl.visitUnaryExpressionOperation(
				UnaryExpression.Operation.NOT, termFilter);

		Assert.assertTrue(booleanFilter.hasClauses());

		List<BooleanClause<Filter>> booleanClauses =
			booleanFilter.getMustNotBooleanClauses();

		Assert.assertEquals(
			booleanClauses.toString(), 1, booleanClauses.size());

		BooleanClause<Filter> queryBooleanClause = booleanClauses.get(0);

		Assert.assertEquals(termFilter, queryBooleanClause.getClause());
		Assert.assertEquals(
			BooleanClauseOccur.MUST_NOT,
			queryBooleanClause.getBooleanClauseOccur());
	}

	protected static final NestedFieldQueryHelper nestedFieldQueryHelper =
		new NestedFieldQueryHelperImpl();

	private static final EntityModel _entityModel = new EntityModel() {

		@Override
		public Map<String, EntityField> getEntityFieldsMap() {
			return Stream.of(
				new CollectionEntityField(
					new StringEntityField(
						"keywords", locale -> "keywords.raw")),
				new ComplexEntityField(
					"values",
					Stream.of(
						new StringEntityField("value1", locale -> "value1")
					).collect(
						Collectors.toList()
					)),
				new DateEntityField("date", locale -> "date", locale -> "date"),
				new DateTimeEntityField(
					"dateTime", locale -> "dateTime", locale -> "dateTime"),
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

	private static final ExpressionVisitorImpl _expressionVisitorImpl =
		new ExpressionVisitorImpl(
			new SimpleDateFormat("yyyyMMddHHmmss"), LocaleUtil.getDefault(),
			_entityModel, nestedFieldQueryHelper);

}