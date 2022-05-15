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

package com.liferay.commerce.shipping.engine.fixed.service.impl;

import com.liferay.commerce.model.CommerceOrderType;
import com.liferay.commerce.model.CommerceOrderTypeTable;
import com.liferay.commerce.shipping.engine.fixed.exception.DuplicateCommerceShippingFixedOptionQualifierException;
import com.liferay.commerce.shipping.engine.fixed.model.CommerceShippingFixedOptionQualifier;
import com.liferay.commerce.shipping.engine.fixed.model.CommerceShippingFixedOptionQualifierTable;
import com.liferay.commerce.shipping.engine.fixed.model.CommerceShippingFixedOptionTable;
import com.liferay.commerce.shipping.engine.fixed.service.base.CommerceShippingFixedOptionQualifierLocalServiceBaseImpl;
import com.liferay.commerce.term.model.CommerceTermEntry;
import com.liferay.commerce.term.model.CommerceTermEntryTable;
import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.Table;
import com.liferay.petra.sql.dsl.expression.Expression;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.FromStep;
import com.liferay.petra.sql.dsl.query.GroupByStep;
import com.liferay.petra.sql.dsl.query.JoinStep;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.List;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceShippingFixedOptionQualifierLocalServiceImpl
	extends CommerceShippingFixedOptionQualifierLocalServiceBaseImpl {

	@Override
	public CommerceShippingFixedOptionQualifier
			addCommerceShippingFixedOptionQualifier(
				long userId, String className, long classPK,
				long commerceShippingFixedOptionId)
		throws PortalException {

		long classNameId = classNameLocalService.getClassNameId(className);

		_validate(classNameId, classPK, commerceShippingFixedOptionId);

		CommerceShippingFixedOptionQualifier
			commerceShippingFixedOptionQualifier =
				commerceShippingFixedOptionQualifierPersistence.create(
					counterLocalService.increment());

		User user = userLocalService.getUser(userId);

		commerceShippingFixedOptionQualifier.setCompanyId(user.getCompanyId());
		commerceShippingFixedOptionQualifier.setUserId(user.getUserId());
		commerceShippingFixedOptionQualifier.setUserName(user.getFullName());

		commerceShippingFixedOptionQualifier.setClassNameId(classNameId);
		commerceShippingFixedOptionQualifier.setClassPK(classPK);
		commerceShippingFixedOptionQualifier.setCommerceShippingFixedOptionId(
			commerceShippingFixedOptionId);

		return commerceShippingFixedOptionQualifierPersistence.update(
			commerceShippingFixedOptionQualifier);
	}

	@Override
	public void deleteCommerceShippingFixedOptionQualifiers(
			long commerceShippingFixedOptionId)
		throws PortalException {

		List<CommerceShippingFixedOptionQualifier>
			commerceShippingFixedOptionQualifiers =
				commerceShippingFixedOptionQualifierPersistence.
					findByCommerceShippingFixedOptionId(
						commerceShippingFixedOptionId);

		for (CommerceShippingFixedOptionQualifier
				commerceShippingFixedOptionQualifier :
					commerceShippingFixedOptionQualifiers) {

			commerceShippingFixedOptionQualifierLocalService.
				deleteCommerceShippingFixedOptionQualifier(
					commerceShippingFixedOptionQualifier);
		}
	}

	@Override
	public void deleteCommerceShippingFixedOptionQualifiers(
			String className, long commerceShippingFixedOptionId)
		throws PortalException {

		List<CommerceShippingFixedOptionQualifier>
			commerceShippingFixedOptionQualifiers =
				commerceShippingFixedOptionQualifierPersistence.findByC_C(
					classNameLocalService.getClassNameId(className),
					commerceShippingFixedOptionId);

		for (CommerceShippingFixedOptionQualifier
				commerceShippingFixedOptionQualifier :
					commerceShippingFixedOptionQualifiers) {

			commerceShippingFixedOptionQualifierLocalService.
				deleteCommerceShippingFixedOptionQualifier(
					commerceShippingFixedOptionQualifier);
		}
	}

	@Override
	public CommerceShippingFixedOptionQualifier
		fetchCommerceShippingFixedOptionQualifier(
			String className, long classPK,
			long commerceShippingFixedOptionId) {

		return commerceShippingFixedOptionQualifierPersistence.fetchByC_C_C(
			classNameLocalService.getClassNameId(className), classPK,
			commerceShippingFixedOptionId);
	}

	@Override
	public List<CommerceShippingFixedOptionQualifier>
		getCommerceOrderTypeCommerceShippingFixedOptionQualifiers(
			long commerceShippingFixedOptionId, String keywords, int start,
			int end) {

		return dslQuery(
			_getGroupByStep(
				DSLQueryFactoryUtil.selectDistinct(
					CommerceShippingFixedOptionQualifierTable.INSTANCE),
				CommerceOrderTypeTable.INSTANCE,
				CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId.eq(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.classPK),
				commerceShippingFixedOptionId,
				CommerceOrderType.class.getName(), keywords,
				CommerceOrderTypeTable.INSTANCE.name
			).limit(
				start, end
			));
	}

	@Override
	public int getCommerceOrderTypeCommerceShippingFixedOptionQualifiersCount(
		long commerceShippingFixedOptionId, String keywords) {

		return dslQueryCount(
			_getGroupByStep(
				DSLQueryFactoryUtil.countDistinct(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.
						commerceShippingFixedOptionQualifierId),
				CommerceOrderTypeTable.INSTANCE,
				CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId.eq(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.classPK),
				commerceShippingFixedOptionId,
				CommerceOrderType.class.getName(), keywords,
				CommerceOrderTypeTable.INSTANCE.name));
	}

	@Override
	public List<CommerceShippingFixedOptionQualifier>
		getCommerceShippingFixedOptionQualifiers(
			long commerceShippingFixedOptionId) {

		return commerceShippingFixedOptionQualifierPersistence.
			findByCommerceShippingFixedOptionId(commerceShippingFixedOptionId);
	}

	@Override
	public List<CommerceShippingFixedOptionQualifier>
		getCommerceShippingFixedOptionQualifiers(
			long commerceShippingFixedOptionId, int start, int end,
			OrderByComparator<CommerceShippingFixedOptionQualifier>
				orderByComparator) {

		return commerceShippingFixedOptionQualifierPersistence.
			findByCommerceShippingFixedOptionId(
				commerceShippingFixedOptionId, start, end, orderByComparator);
	}

	@Override
	public List<CommerceShippingFixedOptionQualifier>
		getCommerceShippingFixedOptionQualifiers(
			String className, long commerceShippingFixedOptionId) {

		return commerceShippingFixedOptionQualifierPersistence.findByC_C(
			classNameLocalService.getClassNameId(className),
			commerceShippingFixedOptionId);
	}

	@Override
	public int getCommerceShippingFixedOptionQualifiersCount(
		long commerceShippingFixedOptionId) {

		return commerceShippingFixedOptionQualifierPersistence.
			countByCommerceShippingFixedOptionId(commerceShippingFixedOptionId);
	}

	@Override
	public List<CommerceShippingFixedOptionQualifier>
		getCommerceTermEntryCommerceShippingFixedOptionQualifiers(
			long commerceShippingFixedOptionId, String keywords, int start,
			int end) {

		return dslQuery(
			_getGroupByStep(
				DSLQueryFactoryUtil.selectDistinct(
					CommerceShippingFixedOptionQualifierTable.INSTANCE),
				CommerceTermEntryTable.INSTANCE,
				CommerceTermEntryTable.INSTANCE.commerceTermEntryId.eq(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.classPK),
				commerceShippingFixedOptionId,
				CommerceTermEntry.class.getName(), keywords,
				CommerceTermEntryTable.INSTANCE.name
			).limit(
				start, end
			));
	}

	@Override
	public int getCommerceTermEntryCommerceShippingFixedOptionQualifiersCount(
		long commerceShippingFixedOptionId, String keywords) {

		return dslQueryCount(
			_getGroupByStep(
				DSLQueryFactoryUtil.countDistinct(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.
						commerceShippingFixedOptionQualifierId),
				CommerceTermEntryTable.INSTANCE,
				CommerceTermEntryTable.INSTANCE.commerceTermEntryId.eq(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.classPK),
				commerceShippingFixedOptionId,
				CommerceTermEntry.class.getName(), keywords,
				CommerceTermEntryTable.INSTANCE.name));
	}

	private GroupByStep _getGroupByStep(
		FromStep fromStep, Table innerJoinTable, Predicate innerJoinPredicate,
		Long commerceShippingFixedOptionId, String className, String keywords,
		Expression<String> keywordsPredicateExpression) {

		JoinStep joinStep = fromStep.from(
			CommerceShippingFixedOptionQualifierTable.INSTANCE
		).innerJoinON(
			CommerceShippingFixedOptionTable.INSTANCE,
			CommerceShippingFixedOptionTable.INSTANCE.
				commerceShippingFixedOptionId.eq(
					CommerceShippingFixedOptionQualifierTable.INSTANCE.
						commerceShippingFixedOptionId)
		).innerJoinON(
			innerJoinTable, innerJoinPredicate
		);

		return joinStep.where(
			() ->
				CommerceShippingFixedOptionQualifierTable.INSTANCE.
					commerceShippingFixedOptionId.eq(
						commerceShippingFixedOptionId
					).and(
						CommerceShippingFixedOptionQualifierTable.INSTANCE.
							classNameId.eq(
								classNameLocalService.getClassNameId(className))
					).and(
						() -> {
							if (Validator.isNotNull(keywords)) {
								return Predicate.withParentheses(
									_customSQL.getKeywordsPredicate(
										DSLFunctionFactoryUtil.lower(
											keywordsPredicateExpression),
										_customSQL.keywords(keywords, true)));
							}

							return null;
						}
					));
	}

	private void _validate(
			long classNameId, long classPK, long commerceShippingFixedOptionId)
		throws PortalException {

		CommerceShippingFixedOptionQualifier
			commerceShippingFixedOptionQualifier =
				commerceShippingFixedOptionQualifierPersistence.fetchByC_C_C(
					classNameId, classPK, commerceShippingFixedOptionId);

		if (commerceShippingFixedOptionQualifier != null) {
			throw new DuplicateCommerceShippingFixedOptionQualifierException();
		}
	}

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

}