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

package com.liferay.commerce.payment.internal.upgrade.v1_0_1;

import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Marco Leo
 */
public class CommercePaymentMethodGroupRelUpgradeProcess
	extends UpgradeProcess {

	public CommercePaymentMethodGroupRelUpgradeProcess(
		ClassNameLocalService classNameLocalService,
		GroupLocalService groupLocalService) {

		_classNameLocalService = classNameLocalService;
		_groupLocalService = groupLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		try (Statement s = connection.createStatement();

			ResultSet resultSet = s.executeQuery(
				"select CPaymentMethodGroupRelId, groupId from " +
					"CommercePaymentMethodGroupRel")) {

			PreparedStatement preparedStatement = null;

			while (resultSet.next()) {
				long groupId = resultSet.getLong("groupId");

				long commerceChannelGroupId =
					_getCommerceChannelGroupIdBySiteGroupId(groupId);

				if (commerceChannelGroupId == 0) {
					continue;
				}

				long cPaymentMethodGroupRelId = resultSet.getLong(
					"CPaymentMethodGroupRelId");

				preparedStatement = connection.prepareStatement(
					"update CommercePaymentMethodGroupRel set groupId = ? " +
						"where CPaymentMethodGroupRelId = ?");

				preparedStatement.setLong(1, commerceChannelGroupId);
				preparedStatement.setLong(2, cPaymentMethodGroupRelId);

				preparedStatement.executeUpdate();
			}
		}
	}

	private long _getCommerceChannelGroupIdBySiteGroupId(long groupId)
		throws SQLException {

		long companyId = 0;
		long commerceChannelId = 0;

		String sql =
			"select * from CommerceChannel where siteGroupId = " + groupId;

		try (Statement s = connection.createStatement()) {
			s.setMaxRows(1);

			try (ResultSet resultSet = s.executeQuery(sql)) {
				if (resultSet.next()) {
					companyId = resultSet.getLong("companyId");
					commerceChannelId = resultSet.getLong("commerceChannelId");
				}
			}
		}

		Group group = _groupLocalService.fetchGroup(
			companyId,
			_classNameLocalService.getClassNameId(
				CommerceChannel.class.getName()),
			commerceChannelId);

		if (group != null) {
			return group.getGroupId();
		}

		return 0;
	}

	private final ClassNameLocalService _classNameLocalService;
	private final GroupLocalService _groupLocalService;

}