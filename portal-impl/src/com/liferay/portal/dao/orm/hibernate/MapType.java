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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.Serializable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

/**
 * @author Cristina González
 */
public class MapType implements Serializable, UserType {

	@Override
	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	@Override
	public Object deepCopy(Object object) {
		return object;
	}

	@Override
	public Serializable disassemble(Object value) {
		return (Serializable)value;
	}

	@Override
	public boolean equals(Object x, Object y) {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(Object x) {
		return x.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(
			ResultSet resultSet, String[] names,
			SharedSessionContractImplementor sharedSessionContractImplementor,
			Object owner)
		throws SQLException {

		String json = (String)StandardBasicTypes.STRING.nullSafeGet(
			resultSet, names, sharedSessionContractImplementor, owner);

		try {
			return _jsonFactory.deserialize(json);
		}
		catch (Exception exception) {
			_log.error("Unable to process JSON " + json, exception);

			return Collections.emptyMap();
		}
	}

	@Override
	public void nullSafeSet(
			PreparedStatement preparedStatement, Object target, int index,
			SharedSessionContractImplementor sharedSessionContractImplementor)
		throws SQLException {

		String json = _jsonFactory.serialize(target);

		StandardBasicTypes.STRING.nullSafeSet(
			preparedStatement, json, index, sharedSessionContractImplementor);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) {
		return original;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class<Map> returnedClass() {
		return Map.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] {StandardBasicTypes.STRING.sqlType()};
	}

	private static final Log _log = LogFactoryUtil.getLog(MapType.class);

	private static final JSONFactory _jsonFactory = new JSONFactoryImpl();

}