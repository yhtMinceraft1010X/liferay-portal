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

package com.liferay.portal.setup;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.FullNameGenerator;
import com.liferay.portal.kernel.security.auth.FullNameGeneratorFactory;
import com.liferay.portal.kernel.security.auth.ScreenNameGenerator;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.CountryServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RegionServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.auth.ScreenNameGeneratorFactory;
import com.liferay.portal.util.PropsValues;

import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Shinn Lok
 */
public class SetupWizardSampleDataUtil {

	public static void addSampleData(long companyId) throws Exception {
		addSampleData(
			companyId, PropsValues.COMPANY_DEFAULT_NAME,
			PropsValues.DEFAULT_ADMIN_FIRST_NAME,
			PropsValues.DEFAULT_ADMIN_LAST_NAME,
			PropsValues.ADMIN_EMAIL_FROM_ADDRESS, false);
	}

	public static void addSampleData(
			long companyId, String companyName, String adminUserFirstName,
			String adminUserLastName, String adminUserEmailAddress,
			boolean resetPassword)
		throws Exception {

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		if (_log.isInfoEnabled()) {
			_log.info("Adding sample data");
		}

		Company company = CompanyLocalServiceUtil.getCompanyById(companyId);

		User defaultUser = company.getDefaultUser();

		company = updateCompany(
			company, companyName,
			LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
			defaultUser.getTimeZoneId());

		User adminUser = updateAdminUser(
			company, LocaleUtil.getDefault(),
			LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
			adminUserEmailAddress, adminUserFirstName, adminUserLastName,
			resetPassword);

		Organization organization =
			OrganizationLocalServiceUtil.addOrganization(
				defaultUser.getUserId(),
				OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID,
				company.getLegalName(), true);

		GroupLocalServiceUtil.updateFriendlyURL(
			organization.getGroupId(), "/main");

		Layout extranetLayout = LayoutLocalServiceUtil.addLayout(
			defaultUser.getUserId(), organization.getGroupId(), false,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			company.getLegalName() + " Extranet", null, null,
			LayoutConstants.TYPE_PORTLET, false, "/extranet",
			new ServiceContext());

		LayoutLocalServiceUtil.updateLayout(
			extranetLayout.getGroupId(), false, extranetLayout.getLayoutId(),
			extranetLayout.getTypeSettings());

		Layout intranetLayout = LayoutLocalServiceUtil.addLayout(
			defaultUser.getUserId(), organization.getGroupId(), true,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			company.getLegalName() + " Intranet", null, null,
			LayoutConstants.TYPE_PORTLET, false, "/intranet",
			new ServiceContext());

		LayoutLocalServiceUtil.updateLayout(
			intranetLayout.getGroupId(), true, intranetLayout.getLayoutId(),
			intranetLayout.getTypeSettings());

		OrganizationLocalServiceUtil.addUserOrganization(
			adminUser.getUserId(), organization);

		addOrganizations(companyName, defaultUser, organization);

		if (_log.isInfoEnabled()) {
			_log.info("Finished adding data in " + stopWatch.getTime() + " ms");
		}
	}

	public static User updateAdminUser(
			Company company, Locale locale, String languageId,
			String emailAddress, String firstName, String lastName,
			boolean resetPassword)
		throws PortalException {

		ScreenNameGenerator screenNameGenerator =
			ScreenNameGeneratorFactory.getInstance();

		String screenName = GetterUtil.getString(
			PropsValues.DEFAULT_ADMIN_EMAIL_ADDRESS_PREFIX, "test");

		try {
			screenName = screenNameGenerator.generate(0, 0, emailAddress);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		User adminUser = UserLocalServiceUtil.fetchUserByEmailAddress(
			company.getCompanyId(), emailAddress);

		if (adminUser != null) {
			FullNameGenerator fullNameGenerator =
				FullNameGeneratorFactory.getInstance();

			String fullName = fullNameGenerator.getFullName(
				firstName, null, lastName);

			String greeting = LanguageUtil.format(
				locale, "welcome-x", fullName, false);

			Contact contact = adminUser.getContact();

			Calendar birthdayCal = CalendarFactoryUtil.getCalendar();

			birthdayCal.setTime(contact.getBirthday());

			int birthdayMonth = birthdayCal.get(Calendar.MONTH);
			int birthdayDay = birthdayCal.get(Calendar.DAY_OF_MONTH);
			int birthdayYear = birthdayCal.get(Calendar.YEAR);

			adminUser = UserLocalServiceUtil.updateUser(
				adminUser.getUserId(), StringPool.BLANK, StringPool.BLANK,
				StringPool.BLANK, false, adminUser.getReminderQueryQuestion(),
				adminUser.getReminderQueryAnswer(), screenName, emailAddress,
				false, null, languageId, adminUser.getTimeZoneId(), greeting,
				adminUser.getComments(), firstName, adminUser.getMiddleName(),
				lastName, contact.getPrefixId(), contact.getSuffixId(),
				contact.isMale(), birthdayMonth, birthdayDay, birthdayYear,
				contact.getSmsSn(), contact.getFacebookSn(),
				contact.getJabberSn(), contact.getSkypeSn(),
				contact.getTwitterSn(), contact.getJobTitle(), null, null, null,
				null, null, new ServiceContext());
		}
		else {
			UserLocalServiceUtil.addDefaultAdminUser(
				company.getCompanyId(), screenName, emailAddress, locale,
				firstName, StringPool.BLANK, lastName);

			adminUser = UserLocalServiceUtil.getUserByEmailAddress(
				company.getCompanyId(), emailAddress);

			String defaultAdminEmailAddress =
				PropsValues.DEFAULT_ADMIN_EMAIL_ADDRESS_PREFIX + "@" +
					PropsValues.COMPANY_DEFAULT_WEB_ID;

			if (!emailAddress.equals(defaultAdminEmailAddress)) {
				User user = UserLocalServiceUtil.fetchUserByEmailAddress(
					company.getCompanyId(), defaultAdminEmailAddress);

				if (user != null) {
					UserLocalServiceUtil.updateStatus(
						user.getUserId(), WorkflowConstants.STATUS_INACTIVE,
						new ServiceContext());
				}
			}
		}

		return UserLocalServiceUtil.updatePasswordReset(
			adminUser.getUserId(), resetPassword);
	}

	public static Company updateCompany(
			Company company, String companyName, String languageId,
			String timeZoneId)
		throws Exception {

		company.setName(companyName);
		company.setLegalName(companyName + ", Inc.");

		company = CompanyLocalServiceUtil.updateCompany(company);

		CompanyLocalServiceUtil.updateDisplay(
			company.getCompanyId(), languageId, timeZoneId);

		return company;
	}

	protected static void addOrganizations(
			String companyName, User defaultUser,
			Organization parentOrganization)
		throws Exception {

		for (Object[] organizationArray : _ORGANIZATION_ARRAYS) {
			String name = companyName + organizationArray[0];
			String a3 = (String)organizationArray[1];
			String regionCode = (String)organizationArray[2];

			Country country = CountryServiceUtil.getCountryByA3(a3);

			long countryId = country.getCountryId();

			long regionId = 0;

			if (regionCode != null) {
				Region region = RegionServiceUtil.getRegion(
					countryId, regionCode);

				regionId = region.getRegionId();
			}

			String type = (String)organizationArray[3];

			Organization organization =
				OrganizationLocalServiceUtil.addOrganization(
					defaultUser.getUserId(),
					parentOrganization.getOrganizationId(), name, type,
					regionId, countryId,
					ListTypeConstants.ORGANIZATION_STATUS_DEFAULT,
					StringPool.BLANK, true, null);

			GroupLocalServiceUtil.updateFriendlyURL(
				organization.getGroupId(),
				FriendlyURLNormalizerUtil.normalize(
					StringPool.SLASH + organizationArray[0]));

			if (organizationArray.length <= 4) {
				continue;
			}

			String organizationPrefix = (String)organizationArray[4];

			long[] groupIds = {organization.getGroupId()};
			long[] organizationIds = {
				parentOrganization.getOrganizationId(),
				organization.getOrganizationId()
			};

			for (int i = 1; i <= 10; i++) {
				StringBundler sb = new StringBundler(5);

				String defaultUserEmailAddress = defaultUser.getEmailAddress();

				String[] defaultUserEmailAddressParts =
					defaultUserEmailAddress.split(StringPool.AT);

				sb.append(defaultUserEmailAddressParts[0]);

				sb.append(StringPool.PERIOD);
				sb.append(organizationPrefix);
				sb.append(StringPool.PERIOD);
				sb.append(i);

				String screenName = sb.toString();

				String emailAddress =
					screenName + StringPool.AT +
						defaultUserEmailAddressParts[1];

				String lastName = organizationPrefix + StringPool.SPACE + i;

				User user = UserLocalServiceUtil.addUser(
					0, defaultUser.getCompanyId(), false, "test", "test", false,
					screenName, emailAddress, LocaleUtil.getDefault(), "Test",
					null, lastName, 0, 0, true, Calendar.JANUARY, 1, 1970, null,
					groupIds, organizationIds, null, null, false,
					new ServiceContext());

				user.setPasswordReset(false);
				user.setAgreedToTermsOfUse(true);

				UserLocalServiceUtil.updateUser(user);
			}
		}
	}

	private static final Object[][] _ORGANIZATION_ARRAYS = {
		{
			"Chicago", "USA", "IL", OrganizationConstants.TYPE_ORGANIZATION,
			"ORD"
		},
		{"Consulting", "USA", "CA", OrganizationConstants.TYPE_ORGANIZATION},
		{"Dalian", "CHN", null, OrganizationConstants.TYPE_ORGANIZATION, "DLC"},
		{"Engineering", "USA", "CA", OrganizationConstants.TYPE_ORGANIZATION},
		{
			"Frankfurt", "DEU", null, OrganizationConstants.TYPE_ORGANIZATION,
			"FRA"
		},
		{
			"Hong Kong", "CHN", null, OrganizationConstants.TYPE_ORGANIZATION,
			"HKG"
		},
		{
			"Kuala Lumpur", "MYS", null,
			OrganizationConstants.TYPE_ORGANIZATION, "KUL"
		},
		{
			"Los Angeles", "USA", "CA", OrganizationConstants.TYPE_ORGANIZATION,
			"LAX"
		},
		{"Madrid", "ESP", null, OrganizationConstants.TYPE_ORGANIZATION, "MAD"},
		{"Marketing", "USA", "CA", OrganizationConstants.TYPE_ORGANIZATION},
		{
			"New York", "USA", "NY", OrganizationConstants.TYPE_ORGANIZATION,
			"NYC"
		},
		{
			"Saint Paulo", "BRA", null, OrganizationConstants.TYPE_ORGANIZATION,
			"GRU"
		},
		{"Sales", "USA", "CA", OrganizationConstants.TYPE_ORGANIZATION},
		{
			"San Francisco", "USA", "CA",
			OrganizationConstants.TYPE_ORGANIZATION, "SFO"
		},
		{"Support", "USA", "CA", OrganizationConstants.TYPE_ORGANIZATION}
	};

	private static final Log _log = LogFactoryUtil.getLog(
		SetupWizardSampleDataUtil.class);

}