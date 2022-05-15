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

package com.liferay.commerce.product.service.impl;

import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.exception.DuplicateCommerceChannelException;
import com.liferay.commerce.product.exception.DuplicateCommerceChannelSiteGroupIdException;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelTable;
import com.liferay.commerce.product.service.base.CommerceChannelLocalServiceBaseImpl;
import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.expression.Expression;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.GroupByStep;
import com.liferay.petra.sql.dsl.query.JoinStep;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Alec Sloan
 * @author Alessio Antonio Rendina
 */
public class CommerceChannelLocalServiceImpl
	extends CommerceChannelLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceChannel addCommerceChannel(
			String externalReferenceCode, long siteGroupId, String name,
			String type, UnicodeProperties typeSettingsUnicodeProperties,
			String commerceCurrencyCode, ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(serviceContext.getUserId());

		CommerceChannel commerceChannel = null;

		if (Validator.isBlank(externalReferenceCode)) {
			externalReferenceCode = null;
		}
		else {
			commerceChannel =
				commerceChannelLocalService.fetchByExternalReferenceCode(
					externalReferenceCode, user.getCompanyId());

			if (commerceChannel != null) {
				throw new DuplicateCommerceChannelException();
			}
		}

		long commerceChannelId = counterLocalService.increment();

		commerceChannel = commerceChannelPersistence.create(commerceChannelId);

		commerceChannel.setExternalReferenceCode(externalReferenceCode);
		commerceChannel.setCompanyId(user.getCompanyId());
		commerceChannel.setUserId(user.getUserId());
		commerceChannel.setUserName(user.getFullName());
		commerceChannel.setSiteGroupId(siteGroupId);
		commerceChannel.setName(name);
		commerceChannel.setType(type);
		commerceChannel.setTypeSettingsProperties(
			typeSettingsUnicodeProperties);
		commerceChannel.setCommerceCurrencyCode(commerceCurrencyCode);
		commerceChannel.setPriceDisplayType(
			CommercePricingConstants.TAX_EXCLUDED_FROM_PRICE);
		commerceChannel.setDiscountsTargetNetPrice(true);

		commerceChannel = commerceChannelPersistence.update(commerceChannel);

		// Group

		Map<Locale, String> nameMap = Collections.singletonMap(
			serviceContext.getLocale(), name);

		_groupLocalService.addGroup(
			user.getUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID,
			CommerceChannel.class.getName(), commerceChannelId,
			GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, null,
			GroupConstants.TYPE_SITE_PRIVATE, false,
			GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, null, false, true,
			null);

		// Resources

		resourceLocalService.addModelResources(commerceChannel, serviceContext);

		return commerceChannel;
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceChannel addOrUpdateCommerceChannel(
			long userId, String externalReferenceCode, long siteGroupId,
			String name, String type,
			UnicodeProperties typeSettingsUnicodeProperties,
			String commerceCurrencyCode, ServiceContext serviceContext)
		throws PortalException {

		CommerceChannel commerceChannel = null;

		if (Validator.isBlank(externalReferenceCode)) {
			externalReferenceCode = null;
		}
		else {
			User user = userLocalService.getUser(userId);

			commerceChannel =
				commerceChannelLocalService.fetchByExternalReferenceCode(
					externalReferenceCode, user.getCompanyId());
		}

		if (commerceChannel == null) {
			return commerceChannelLocalService.addCommerceChannel(
				externalReferenceCode, siteGroupId, name, type,
				typeSettingsUnicodeProperties, commerceCurrencyCode,
				serviceContext);
		}

		return commerceChannelLocalService.updateCommerceChannel(
			commerceChannel.getCommerceChannelId(), siteGroupId, name, type,
			typeSettingsUnicodeProperties, commerceCurrencyCode);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommerceChannel deleteCommerceChannel(
			CommerceChannel commerceChannel)
		throws PortalException {

		// Commerce channel rel

		commerceChannelRelLocalService.deleteCommerceChannelRels(
			commerceChannel.getCommerceChannelId());

		// Resources

		resourceLocalService.deleteResource(
			commerceChannel, ResourceConstants.SCOPE_INDIVIDUAL);

		// Group

		Group group = fetchCommerceChannelGroup(
			commerceChannel.getCommerceChannelId());

		// Commerce channel

		commerceChannel = commerceChannelPersistence.remove(commerceChannel);

		if (group != null) {
			_groupLocalService.deleteGroup(group);
		}

		return commerceChannel;
	}

	@Override
	public CommerceChannel deleteCommerceChannel(long commerceChannelId)
		throws PortalException {

		CommerceChannel commerceChannel =
			commerceChannelPersistence.findByPrimaryKey(commerceChannelId);

		return commerceChannelLocalService.deleteCommerceChannel(
			commerceChannel);
	}

	@Override
	public void deleteCommerceChannels(long companyId) throws PortalException {
		List<CommerceChannel> commerceChannels =
			commerceChannelPersistence.findByCompanyId(companyId);

		for (CommerceChannel commerceChannel : commerceChannels) {
			commerceChannelLocalService.deleteCommerceChannel(commerceChannel);
		}
	}

	@Override
	public CommerceChannel fetchByExternalReferenceCode(
		String externalReferenceCode, long companyId) {

		if (Validator.isBlank(externalReferenceCode)) {
			return null;
		}

		return commerceChannelPersistence.fetchByC_ERC(
			companyId, externalReferenceCode);
	}

	@Override
	public CommerceChannel fetchCommerceChannelBySiteGroupId(long siteGroupId) {
		return commerceChannelPersistence.fetchBySiteGroupId(siteGroupId);
	}

	@Override
	public Group fetchCommerceChannelGroup(long commerceChannelId)
		throws PortalException {

		CommerceChannel commerceChannel =
			commerceChannelLocalService.getCommerceChannel(commerceChannelId);

		return _groupLocalService.fetchGroup(
			commerceChannel.getCompanyId(),
			classNameLocalService.getClassNameId(
				CommerceChannel.class.getName()),
			commerceChannelId);
	}

	@Override
	public CommerceChannel getCommerceChannelByGroupId(long groupId)
		throws PortalException {

		Group group = _groupLocalService.getGroup(groupId);

		return commerceChannelLocalService.getCommerceChannel(
			group.getClassPK());
	}

	@Override
	public CommerceChannel getCommerceChannelByOrderGroupId(long orderGroupId)
		throws PortalException {

		return commerceChannelLocalService.getCommerceChannelByGroupId(
			orderGroupId);
	}

	@Override
	public Group getCommerceChannelGroup(long commerceChannelId)
		throws PortalException {

		Group group = commerceChannelLocalService.fetchCommerceChannelGroup(
			commerceChannelId);

		if (group != null) {
			return group;
		}

		throw new PortalException();
	}

	@Override
	public long getCommerceChannelGroupIdBySiteGroupId(long siteGroupId)
		throws PortalException {

		CommerceChannel commerceChannel =
			commerceChannelPersistence.findBySiteGroupId(siteGroupId);

		Group group = commerceChannelLocalService.getCommerceChannelGroup(
			commerceChannel.getCommerceChannelId());

		return group.getGroupId();
	}

	@Override
	public List<CommerceChannel> getCommerceChannels(long companyId) {
		return commerceChannelPersistence.findByCompanyId(companyId);
	}

	@Override
	public List<CommerceChannel> getCommerceChannels(
			long companyId, String keywords, int start, int end)
		throws PortalException {

		return dslQuery(
			_getGroupByStep(
				DSLQueryFactoryUtil.selectDistinct(
					CommerceChannelTable.INSTANCE
				).from(
					CommerceChannelTable.INSTANCE
				),
				companyId, keywords, CommerceChannelTable.INSTANCE.name
			).limit(
				start, end
			));
	}

	@Override
	public int getCommerceChannelsCount(long companyId, String keywords)
		throws PortalException {

		return dslQueryCount(
			_getGroupByStep(
				DSLQueryFactoryUtil.countDistinct(
					CommerceChannelTable.INSTANCE.commerceChannelId
				).from(
					CommerceChannelTable.INSTANCE
				),
				companyId, keywords, CommerceChannelTable.INSTANCE.name));
	}

	@Override
	public List<CommerceChannel> search(long companyId) throws PortalException {
		return search(
			companyId, StringPool.BLANK, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	@Override
	public List<CommerceChannel> search(
			long companyId, String keywords, int start, int end, Sort sort)
		throws PortalException {

		SearchContext searchContext = buildSearchContext(
			companyId, start, end, sort);

		searchContext.setKeywords(keywords);

		return search(searchContext);
	}

	@Override
	public int searchCommerceChannelsCount(long companyId, String keywords)
		throws PortalException {

		SearchContext searchContext = buildSearchContext(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		searchContext.setKeywords(keywords);

		return searchCommerceChannelsCount(searchContext);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceChannel updateCommerceChannel(
			long commerceChannelId, long siteGroupId, String name, String type,
			UnicodeProperties typeSettingsUnicodeProperties,
			String commerceCurrencyCode)
		throws PortalException {

		CommerceChannel commerceChannel =
			commerceChannelPersistence.findByPrimaryKey(commerceChannelId);

		commerceChannel.setSiteGroupId(siteGroupId);
		commerceChannel.setName(name);
		commerceChannel.setType(type);
		commerceChannel.setTypeSettingsProperties(
			typeSettingsUnicodeProperties);
		commerceChannel.setCommerceCurrencyCode(commerceCurrencyCode);

		return commerceChannelPersistence.update(commerceChannel);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceChannel updateCommerceChannel(
			long commerceChannelId, long siteGroupId, String name, String type,
			UnicodeProperties typeSettingsUnicodeProperties,
			String commerceCurrencyCode, String priceDisplayType,
			boolean discountsTargetNetPrice)
		throws PortalException {

		CommerceChannel commerceChannel =
			commerceChannelPersistence.findByPrimaryKey(commerceChannelId);

		commerceChannel.setSiteGroupId(siteGroupId);
		commerceChannel.setName(name);
		commerceChannel.setType(type);
		commerceChannel.setTypeSettingsProperties(
			typeSettingsUnicodeProperties);
		commerceChannel.setCommerceCurrencyCode(commerceCurrencyCode);
		commerceChannel.setPriceDisplayType(priceDisplayType);
		commerceChannel.setDiscountsTargetNetPrice(discountsTargetNetPrice);

		return commerceChannelPersistence.update(commerceChannel);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceChannel updateCommerceChannelExternalReferenceCode(
			String externalReferenceCode, long commerceChannelId)
		throws PortalException {

		CommerceChannel commerceChannel =
			commerceChannelPersistence.findByPrimaryKey(commerceChannelId);

		commerceChannel.setExternalReferenceCode(externalReferenceCode);

		return commerceChannelPersistence.update(commerceChannel);
	}

	protected SearchContext buildSearchContext(
		long companyId, int start, int end, Sort sort) {

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(companyId);
		searchContext.setEnd(end);

		if (sort != null) {
			searchContext.setSorts(sort);
		}

		searchContext.setStart(start);

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		return searchContext;
	}

	protected List<CommerceChannel> getCommerceChannels(Hits hits)
		throws PortalException {

		List<Document> documents = hits.toList();

		List<CommerceChannel> commerceChannels = new ArrayList<>(
			documents.size());

		for (Document document : documents) {
			long commerceChannelId = GetterUtil.getLong(
				document.get(Field.ENTRY_CLASS_PK));

			CommerceChannel commerceChannel = fetchCommerceChannel(
				commerceChannelId);

			if (commerceChannel == null) {
				commerceChannels = null;

				Indexer<CommerceChannel> indexer =
					IndexerRegistryUtil.getIndexer(CommerceChannel.class);

				long companyId = GetterUtil.getLong(
					document.get(Field.COMPANY_ID));

				indexer.delete(companyId, document.getUID());
			}
			else if (commerceChannels != null) {
				commerceChannels.add(commerceChannel);
			}
		}

		return commerceChannels;
	}

	protected List<CommerceChannel> search(SearchContext searchContext)
		throws PortalException {

		Indexer<CommerceChannel> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(CommerceChannel.class);

		for (int i = 0; i < 10; i++) {
			Hits hits = indexer.search(searchContext, _SELECTED_FIELD_NAMES);

			List<CommerceChannel> commerceChannels = getCommerceChannels(hits);

			if (commerceChannels != null) {
				return commerceChannels;
			}
		}

		throw new SearchException(
			"Unable to fix the search index after 10 attempts");
	}

	protected int searchCommerceChannelsCount(SearchContext searchContext)
		throws PortalException {

		Indexer<CommerceChannel> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(CommerceChannel.class);

		return GetterUtil.getInteger(indexer.searchCount(searchContext));
	}

	protected void validate(long siteGroupId) throws PortalException {
		if (fetchCommerceChannelBySiteGroupId(siteGroupId) != null) {
			throw new DuplicateCommerceChannelSiteGroupIdException();
		}
	}

	private GroupByStep _getGroupByStep(
			JoinStep joinStep, Long companyId, String keywords,
			Expression<String> keywordsPredicateExpression)
		throws PortalException {

		return joinStep.where(
			CommerceChannelTable.INSTANCE.companyId.eq(
				companyId
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

	private static final String[] _SELECTED_FIELD_NAMES = {
		Field.ENTRY_CLASS_PK, Field.COMPANY_ID
	};

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

	@ServiceReference(type = GroupLocalService.class)
	private GroupLocalService _groupLocalService;

}