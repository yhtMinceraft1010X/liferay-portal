/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.headless.commerce.punchout.internal.resource.v1_0;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.account.service.CommerceAccountUserRelLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.punchout.configuration.PunchOutConfiguration;
import com.liferay.commerce.punchout.constants.PunchOutConstants;
import com.liferay.commerce.punchout.oauth2.provider.PunchOutAccessTokenProvider;
import com.liferay.commerce.punchout.oauth2.provider.model.PunchOutAccessToken;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.headless.commerce.punchout.dto.v1_0.Cart;
import com.liferay.headless.commerce.punchout.dto.v1_0.CartItem;
import com.liferay.headless.commerce.punchout.dto.v1_0.Group;
import com.liferay.headless.commerce.punchout.dto.v1_0.PunchOutSession;
import com.liferay.headless.commerce.punchout.dto.v1_0.User;
import com.liferay.headless.commerce.punchout.helper.PunchOutContext;
import com.liferay.headless.commerce.punchout.helper.PunchOutSessionContributor;
import com.liferay.headless.commerce.punchout.resource.v1_0.PunchOutSessionResource;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URLEncoder;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Jaclyn Ong
 */
@Component(
	enabled = false,
	properties = "OSGI-INF/liferay/rest/v1_0/punch-out-session.properties",
	scope = ServiceScope.PROTOTYPE, service = PunchOutSessionResource.class
)
public class PunchOutSessionResourceImpl
	extends BasePunchOutSessionResourceImpl {

	@Override
	public PunchOutSession postPunchOutSessionRequest(
			PunchOutSession punchOutSession)
		throws Exception {

		com.liferay.portal.kernel.model.Group buyerGroup = _fetchGroup(
			punchOutSession.getBuyerGroup());

		if (buyerGroup == null) {
			throw new NoSuchGroupException();
		}

		CommerceChannel commerceChannel = _fetchChannel(
			buyerGroup.getGroupId());

		if (!_punchOutEnabled(commerceChannel.getGroupId())) {
			_log.error(
				"Punch out request not allowed. Punch out is disabled for " +
					"channel " + commerceChannel.getName());

			throw new ForbiddenException("Punch out not allowed for channel");
		}

		User buyerUser = punchOutSession.getBuyerUser();

		com.liferay.portal.kernel.model.User buyerLiferayUser =
			_fetchOrCreateBuyerUser(buyerUser, buyerGroup.getGroupId());

		if (buyerLiferayUser == null) {
			_log.error(
				"Buyer user not found or failed to be created (user email: " +
					buyerUser.getEmail() + ")");

			throw new InternalServerErrorException(
				"Buyer not found or failed to be created");
		}

		if (!_userBelongsToGroup(buyerGroup.getGroupId(), buyerLiferayUser)) {
			_log.error(
				"Buyer user does not belong to group (user email: " +
					buyerUser.getEmail() + ")");

			throw new BadRequestException(
				"Buyer user does not belong to group");
		}

		CommerceAccount businessCommerceAccount = _fetchBusinessCommerceAccount(
			punchOutSession.getBuyerAccountReferenceCode());

		if (businessCommerceAccount == null) {
			_log.error(
				"Business commerce account not found with external reference" +
					"code: " + punchOutSession.getBuyerAccountReferenceCode());

			throw new BadRequestException(
				"Business commerce account not found");
		}

		_addBuyerUserToAccount(
			businessCommerceAccount, buyerLiferayUser.getUserId(),
			buyerGroup.getGroupId());

		String punchOutSessionType = punchOutSession.getPunchOutSessionType();

		Cart cart = punchOutSession.getCart();

		String commerceOrderUuid = null;

		CommerceOrder editCartCommerceOrder = null;

		if (StringUtil.equalsIgnoreCase(
				punchOutSessionType, _EDIT_REQUEST_TYPE) ||
			StringUtil.equalsIgnoreCase(
				punchOutSessionType, _INSPECT_REQUEST_TYPE)) {

			if (!_userBelongsToCart(
					buyerLiferayUser.getUserId(), cart.getId())) {

				_log.error(
					"Buyer user does not belong to cart (cart ID: " +
						cart.getId() + ")");

				throw new BadRequestException(
					"Buyer user does not belong to cart");
			}

			_mergeCartItems(punchOutSession.getCart(), buyerGroup.getGroupId());

			editCartCommerceOrder =
				_commerceOrderLocalService.fetchCommerceOrder(cart.getId());

			commerceOrderUuid = editCartCommerceOrder.getUuid();
		}

		String punchOutStartURL = _getPunchOutStartURL(
			commerceChannel.getGroupId());

		PunchOutContext punchOutContext = new PunchOutContext(
			businessCommerceAccount, buyerGroup, buyerLiferayUser,
			commerceChannel, editCartCommerceOrder, punchOutSession);

		PunchOutAccessToken punchOutAccessToken =
			_punchOutAccessTokenProvider.generatePunchOutAccessToken(
				buyerGroup.getGroupId(),
				businessCommerceAccount.getCommerceAccountId(),
				cart.getCurrencyCode(), buyerLiferayUser.getEmailAddress(),
				commerceOrderUuid,
				_punchOutSessionContributor.getPunchOutSessionAttributes(
					punchOutContext));

		String tokenString = Base64.encodeToURL(punchOutAccessToken.getToken());

		punchOutStartURL +=
			StringPool.QUESTION + _PUNCH_OUT_ACCESS_TOKEN_PARAMETER +
				URLEncoder.encode(tokenString, "UTF-8");

		cart.setChannelId(commerceChannel.getCommerceChannelId());

		punchOutSession.setCart(cart);

		punchOutSession.setPunchOutStartURL(punchOutStartURL);

		return punchOutSession;
	}

	@Reference(unbind = "-")
	protected void setConfigurationProvider(
		ConfigurationProvider configurationProvider) {

		_configurationProvider = configurationProvider;
	}

	private com.liferay.portal.kernel.model.User _addBuyerUser(
			long companyId, long groupId, String email, String firstName,
			String middleName, String lastName)
		throws Exception {

		if (Validator.isBlank(firstName) && Validator.isBlank(lastName)) {
			_log.error("Buyer user first and last name are required");

			throw new BadRequestException(
				"Buyer user first and last name are required");
		}

		_checkAllowUserCreation(companyId, email);

		long creatorUserId = 0;
		boolean autoPassword = true;
		String password1 = StringPool.BLANK;
		String password2 = StringPool.BLANK;
		boolean autoScreenName = true;
		String screenName = StringPool.BLANK;
		Locale locale = LocaleUtil.getDefault();
		long prefixId = 0;
		long suffixId = 0;
		int birthdayMonth = Calendar.JANUARY;
		int birthdayDay = 1;
		int birthdayYear = 1970;
		String jobTitle = StringPool.BLANK;
		long[] organizationIds = null;
		long[] userGroupIds = null;
		boolean sendEmail = true;
		long[] roleIds = null;

		com.liferay.portal.kernel.model.User user = _userLocalService.addUser(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, email, locale, firstName, middleName,
			lastName, prefixId, suffixId, false, birthdayMonth, birthdayDay,
			birthdayYear, jobTitle, new long[] {groupId}, organizationIds,
			roleIds, userGroupIds, sendEmail,
			_serviceContextHelper.getServiceContext(groupId));

		user = _userLocalService.updateLastLogin(
			user.getUserId(), user.getLoginIP());

		return _userLocalService.updatePasswordReset(user.getUserId(), false);
	}

	private void _addBuyerUserToAccount(
			CommerceAccount commerceAccount, long userId, long groupId)
		throws Exception {

		Role role = _roleLocalService.fetchRole(
			contextCompany.getCompanyId(),
			PunchOutConstants.ROLE_NAME_ACCOUNT_PUNCH_OUT);

		if (role == null) {
			String logMessage =
				PunchOutConstants.ROLE_NAME_ACCOUNT_PUNCH_OUT +
					" role not found";

			_log.error(logMessage);

			throw new InternalServerErrorException(logMessage);
		}

		_commerceAccountUserRelLocalService.addCommerceAccountUserRels(
			commerceAccount.getCommerceAccountId(), new long[] {userId}, null,
			new long[] {role.getRoleId()},
			_serviceContextHelper.getServiceContext(groupId));
	}

	private void _checkAllowUserCreation(long companyId, String email)
		throws Exception {

		Company company = _companyLocalService.getCompany(companyId);

		if (!company.isStrangers()) {
			_log.error(
				"Strangers are disabled for company (company ID: " + companyId +
					")");

			throw new InternalServerErrorException();
		}

		if (company.hasCompanyMx(email) && !company.isStrangersWithMx()) {
			throw new UserEmailAddressException.MustNotUseCompanyMx(email);
		}
	}

	private CommerceAccount _fetchBusinessCommerceAccount(
		String externalReferenceCode) {

		return _commerceAccountLocalService.fetchByExternalReferenceCode(
			contextCompany.getCompanyId(), externalReferenceCode);
	}

	private CommerceChannel _fetchChannel(long groupId) {
		return _commerceChannelLocalService.fetchCommerceChannelBySiteGroupId(
			groupId);
	}

	private com.liferay.portal.kernel.model.Group _fetchGroup(Group group) {
		return _groupLocalService.fetchGroup(
			contextCompany.getCompanyId(), group.getName());
	}

	private com.liferay.portal.kernel.model.User _fetchOrCreateBuyerUser(
			User user, long groupId)
		throws Exception {

		if (Validator.isBlank(user.getEmail())) {
			_log.error("Buyer user email is required");

			throw new BadRequestException("Buyer user email is required");
		}

		com.liferay.portal.kernel.model.User liferayUser =
			_userLocalService.fetchUserByEmailAddress(
				contextCompany.getCompanyId(), user.getEmail());

		if (liferayUser != null) {
			return liferayUser;
		}

		return _addBuyerUser(
			contextCompany.getCompanyId(), groupId, user.getEmail(),
			user.getFirstName(), user.getMiddleName(), user.getLastName());
	}

	private PunchOutConfiguration _getPunchOutConfiguration(
		long commerceChannelGroupId) {

		try {
			return _configurationProvider.getConfiguration(
				PunchOutConfiguration.class,
				new GroupServiceSettingsLocator(
					commerceChannelGroupId, PunchOutConstants.SERVICE_NAME));
		}
		catch (ConfigurationException configurationException) {
			_log.error(
				"Unable to get punch out configuration",
				configurationException);
		}

		return null;
	}

	private String _getPunchOutStartURL(long commerceChannelGroupId) {
		PunchOutConfiguration punchOutConfiguration = _getPunchOutConfiguration(
			commerceChannelGroupId);

		if (punchOutConfiguration == null) {
			return null;
		}

		return punchOutConfiguration.punchOutStartURL();
	}

	private void _mergeCartItems(Cart cart, long groupId) throws Exception {
		CommerceOrder commerceOrder =
			_commerceOrderLocalService.fetchCommerceOrder(cart.getId());

		if (commerceOrder == null) {
			return;
		}

		CartItem[] cartItems = cart.getCartItems();

		List<CommerceOrderItem> commerceOrderItems =
			_commerceOrderItemLocalService.getCommerceOrderItems(
				commerceOrder.getCommerceOrderId(), -1, -1);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceOrder.getGroupId(),
			contextUser.getUserId(), commerceOrder.getCommerceOrderId(),
			commerceOrder.getCommerceAccountId());

		for (CartItem cartItem : cartItems) {
			if (!commerceOrderItems.isEmpty()) {
				boolean found = false;

				for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
					if (cartItem.getId() ==
							commerceOrderItem.getCommerceOrderItemId()) {

						found = true;

						_commerceOrderItemLocalService.updateCommerceOrderItem(
							commerceOrderItem.getCommerceOrderItemId(),
							cartItem.getQuantity(), commerceContext,
							_serviceContextHelper.getServiceContext(groupId));

						break;
					}
				}

				if (found) {
					continue;
				}
			}

			_commerceOrderItemLocalService.addCommerceOrderItem(
				commerceOrder.getCommerceOrderId(), cartItem.getSkuId(), null,
				cartItem.getQuantity(), cartItem.getShippedQuantity(),
				commerceContext,
				_serviceContextHelper.getServiceContext(groupId));
		}

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			if (cartItems.length > 0) {
				boolean found = false;

				for (CartItem cartItem : cartItems) {
					if (cartItem.getId() ==
							commerceOrderItem.getCommerceOrderItemId()) {

						found = true;

						break;
					}
				}

				if (found) {
					continue;
				}
			}

			_commerceOrderItemLocalService.deleteCommerceOrderItem(
				commerceOrderItem.getCommerceOrderItemId());
		}
	}

	private boolean _punchOutEnabled(long commerceChannelGroupId) {
		PunchOutConfiguration punchOutConfiguration = _getPunchOutConfiguration(
			commerceChannelGroupId);

		if (punchOutConfiguration == null) {
			return false;
		}

		return punchOutConfiguration.enabled();
	}

	private boolean _userBelongsToCart(long userId, long cartId) {
		CommerceOrder commerceOrder =
			_commerceOrderLocalService.fetchCommerceOrder(cartId);

		if (userId == commerceOrder.getUserId()) {
			return true;
		}

		return false;
	}

	private boolean _userBelongsToGroup(
		long groupId, com.liferay.portal.kernel.model.User user) {

		return ArrayUtil.contains(user.getGroupIds(), groupId);
	}

	private static final String _EDIT_REQUEST_TYPE = "edit";

	private static final String _INSPECT_REQUEST_TYPE = "inspect";

	private static final String _PUNCH_OUT_ACCESS_TOKEN_PARAMETER =
		"punchOutAccessToken=";

	private static final Log _log = LogFactoryUtil.getLog(
		PunchOutSessionResourceImpl.class);

	@Reference
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Reference
	private CommerceAccountUserRelLocalService
		_commerceAccountUserRelLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	private ConfigurationProvider _configurationProvider;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private PunchOutAccessTokenProvider _punchOutAccessTokenProvider;

	@Reference
	private PunchOutSessionContributor _punchOutSessionContributor;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

	@Reference
	private UserLocalService _userLocalService;

}