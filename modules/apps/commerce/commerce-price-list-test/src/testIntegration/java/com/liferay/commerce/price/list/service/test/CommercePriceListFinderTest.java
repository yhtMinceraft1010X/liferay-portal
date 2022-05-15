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

package com.liferay.commerce.price.list.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelLocalServiceUtil;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.price.list.constants.CommercePriceListConstants;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceListAccountRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListChannelRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListCommerceAccountGroupRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.price.list.test.util.CommercePriceListTestUtil;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import org.frutilla.FrutillaRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Riccardo Alberti
 */
@RunWith(Arquillian.class)
public class CommercePriceListFinderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addUser();

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency(
			_group.getCompanyId());

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_group.getCompanyId(), _group.getGroupId(), _user.getUserId());

		_commerceAccount =
			_commerceAccountLocalService.getPersonalCommerceAccount(
				_user.getUserId());

		_commerceAccountGroup =
			_commerceAccountGroupLocalService.addCommerceAccountGroup(
				_group.getCompanyId(), RandomTestUtil.randomString(), 0, false,
				null, _serviceContext);

		CommerceAccountGroupCommerceAccountRelLocalServiceUtil.
			addCommerceAccountGroupCommerceAccountRel(
				_commerceAccountGroup.getCommerceAccountGroupId(),
				_commerceAccount.getCommerceAccountId(), _serviceContext);

		_commerceCatalog = CommerceTestUtil.addCommerceCatalog(
			_user.getCompanyId(), _user.getGroupId(), _user.getUserId(),
			_commerceCurrency.getCode());

		_commerceChannel = CommerceTestUtil.addCommerceChannel(
			_group.getGroupId(), _commerceCurrency.getCode());
	}

	@After
	public void tearDown() throws Exception {
		_commercePriceListLocalService.deleteCommercePriceLists(
			_group.getCompanyId());
	}

	@Test
	public void testRetrieveAccountAndChannelPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has an account and a channel as qualifier i " +
				"shall be able to retrieve it"
		).given(
			"A catalog with a price list qualified for an account and a channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account and channel"
		);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addAccountAndChannelPriceList(
				_commerceCatalog.getGroupId(),
				_commerceAccount.getCommerceAccountId(),
				_commerceChannel.getCommerceChannelId(), _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountAndChannelId(
					_commerceCatalog.getGroupId(),
					_commerceAccount.getCommerceAccountId(),
					_commerceChannel.getCommerceChannelId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNotNull(
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId()));
		Assert.assertNotNull(
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveAccountGroupsAndChannelPriceList()
		throws Exception {

		frutillaRule.scenario(
			"When a price list has account groups and a channel as qualifier " +
				"i shall be able to retrieve it"
		).given(
			"A catalog with a price list qualified for a account groups and " +
				"a channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account groups and channel"
		);

		long[] commerceAccountGroupIds =
			_commerceAccountHelper.getCommerceAccountGroupIds(
				_commerceAccount.getCommerceAccountId());

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addAccountGroupAndChannelPriceList(
				_commerceCatalog.getGroupId(), commerceAccountGroupIds,
				_commerceChannel.getCommerceChannelId(), _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountGroupsAndChannelId(
					_commerceCatalog.getGroupId(), commerceAccountGroupIds,
					_commerceChannel.getCommerceChannelId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			Assert.assertNotNull(
				_commercePriceListCommerceAccountGroupRelLocalService.
					fetchCommercePriceListCommerceAccountGroupRel(
						retrievedPriceList.getCommercePriceListId(),
						commerceAccountGroupId));
		}

		Assert.assertNotNull(
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveAccountGroupsPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has an account group as qualifier i shall be " +
				"able to retrieve it"
		).given(
			"A catalog with a price list qualified for an account group"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account group"
		);

		long[] commerceAccountGroupIds =
			_commerceAccountHelper.getCommerceAccountGroupIds(
				_commerceAccount.getCommerceAccountId());

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addAccountGroupPriceList(
				_commerceCatalog.getGroupId(), commerceAccountGroupIds, _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountGroupIds(
					_commerceCatalog.getGroupId(), commerceAccountGroupIds,
					_TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNotNull(
			_commercePriceListCommerceAccountGroupRelLocalService.
				fetchCommercePriceListCommerceAccountGroupRel(
					retrievedPriceList.getCommercePriceListId(),
					_commerceAccountGroup.getCommerceAccountGroupId()));
	}

	@Test
	public void testRetrieveAccountPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has an account as qualifier i shall be able " +
				"to retrieve it"
		).given(
			"A catalog with a price list qualified for an account"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account"
		);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addAccountPriceList(
				_commerceCatalog.getGroupId(),
				_commerceAccount.getCommerceAccountId(), _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByAccountId(
				_commerceCatalog.getGroupId(),
				_commerceAccount.getCommerceAccountId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNotNull(
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveChannelPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has a channel as qualifier i shall be able to " +
				"retrieve it"
		).given(
			"A catalog with a price list qualified for an channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the channel"
		);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addChannelPriceList(
				_commerceCatalog.getGroupId(),
				_commerceChannel.getCommerceChannelId(), _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByChannelId(
				_commerceCatalog.getGroupId(),
				_commerceChannel.getCommerceChannelId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNotNull(
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveGuestAccountAndChannelPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has a guest account and a channel as " +
				"qualifier i shall be able to retrieve it"
		).given(
			"A catalog with a price list qualified for a guest account and a " +
				"channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account and channel"
		);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addAccountAndChannelPriceList(
				_commerceCatalog.getGroupId(),
				CommerceAccountConstants.ACCOUNT_ID_GUEST,
				_commerceChannel.getCommerceChannelId(), _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountAndChannelId(
					_commerceCatalog.getGroupId(),
					CommerceAccountConstants.ACCOUNT_ID_GUEST,
					_commerceChannel.getCommerceChannelId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNotNull(
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					CommerceAccountConstants.ACCOUNT_ID_GUEST,
					retrievedPriceList.getCommercePriceListId()));
		Assert.assertNotNull(
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveGuestAccountPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has a guest account as qualifier i shall be " +
				"able to retrieve it"
		).given(
			"A catalog with a price list qualified for the guest account"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account"
		);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addAccountPriceList(
				_commerceCatalog.getGroupId(),
				CommerceAccountConstants.ACCOUNT_ID_GUEST, _TYPE);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByAccountId(
				_commerceCatalog.getGroupId(),
				CommerceAccountConstants.ACCOUNT_ID_GUEST, _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNotNull(
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					CommerceAccountConstants.ACCOUNT_ID_GUEST,
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveUnqualifiedPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has no qualifiers i shall be able to retrieve it"
		).given(
			"A catalog with a price list with no qualifiers"
		).when(
			"The price list is discovered"
		).then(
			"The price list has no qualifiers"
		);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				_commerceCatalog.getGroupId(), false, _TYPE, 1.0);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByUnqualified(
				_commerceCatalog.getGroupId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertNull(
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId()));

		long[] commerceAccountGroupIds =
			_commerceAccountHelper.getCommerceAccountGroupIds(
				_commerceAccount.getCommerceAccountId());

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			Assert.assertNull(
				_commercePriceListCommerceAccountGroupRelLocalService.
					fetchCommercePriceListCommerceAccountGroupRel(
						retrievedPriceList.getCommercePriceListId(),
						commerceAccountGroupId));
		}

		Assert.assertNull(
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Test
	public void testRetrieveUnqualifiedPriceListOverCatalogBasePriceList()
		throws Exception {

		frutillaRule.scenario(
			"A price list with no qualifier shall be selected over the " +
				"catalog base price list if they have the same priority"
		).given(
			"A catalog with a base price list and price list with no " +
				"qualifiers with same priority"
		).when(
			"The price list is discovered"
		).then(
			"The price list has no qualifiers and it is not the base price list"
		);

		CommercePriceList baseCommercePriceList =
			_commercePriceListLocalService.fetchCatalogBaseCommercePriceList(
				_commerceCatalog.getGroupId());

		Assert.assertEquals(
			_commerceCatalog.getGroupId(), baseCommercePriceList.getGroupId());
		Assert.assertTrue(baseCommercePriceList.isCatalogBasePriceList());

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				_commerceCatalog.getGroupId(), false, _TYPE, 1.0);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByUnqualified(
				_commerceCatalog.getGroupId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());
		Assert.assertFalse(retrievedPriceList.isCatalogBasePriceList());
		Assert.assertNull(
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId()));

		long[] commerceAccountGroupIds =
			_commerceAccountHelper.getCommerceAccountGroupIds(
				_commerceAccount.getCommerceAccountId());

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			Assert.assertNull(
				_commercePriceListCommerceAccountGroupRelLocalService.
					fetchCommercePriceListCommerceAccountGroupRel(
						retrievedPriceList.getCommercePriceListId(),
						commerceAccountGroupId));
		}

		Assert.assertNull(
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId()));
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

	private static final String _TYPE =
		CommercePriceListConstants.TYPE_PRICE_LIST;

	private static User _user;

	private CommerceAccount _commerceAccount;
	private CommerceAccountGroup _commerceAccountGroup;

	@Inject
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Inject
	private CommerceAccountHelper _commerceAccountHelper;

	@Inject
	private CommerceAccountLocalService _commerceAccountLocalService;

	private CommerceCatalog _commerceCatalog;
	private CommerceChannel _commerceChannel;
	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommercePriceListAccountRelLocalService
		_commercePriceListAccountRelLocalService;

	@Inject
	private CommercePriceListChannelRelLocalService
		_commercePriceListChannelRelLocalService;

	@Inject
	private CommercePriceListCommerceAccountGroupRelLocalService
		_commercePriceListCommerceAccountGroupRelLocalService;

	@Inject
	private CommercePriceListLocalService _commercePriceListLocalService;

	@Inject
	private CPDefinitionLocalService _cpDefinitionLocalService;

	private Group _group;
	private ServiceContext _serviceContext;

}