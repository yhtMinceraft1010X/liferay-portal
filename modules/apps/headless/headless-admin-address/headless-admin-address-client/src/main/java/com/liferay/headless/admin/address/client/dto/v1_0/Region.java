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

package com.liferay.headless.admin.address.client.dto.v1_0;

import com.liferay.headless.admin.address.client.function.UnsafeSupplier;
import com.liferay.headless.admin.address.client.serdes.v1_0.RegionSerDes;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Drew Brokke
 * @generated
 */
@Generated("")
public class Region implements Cloneable, Serializable {

	public static Region toDTO(String json) {
		return RegionSerDes.toDTO(json);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setActive(
		UnsafeSupplier<Boolean, Exception> activeUnsafeSupplier) {

		try {
			active = activeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean active;

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public void setCountryId(
		UnsafeSupplier<Long, Exception> countryIdUnsafeSupplier) {

		try {
			countryId = countryIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long countryId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String name;

	public Double getPosition() {
		return position;
	}

	public void setPosition(Double position) {
		this.position = position;
	}

	public void setPosition(
		UnsafeSupplier<Double, Exception> positionUnsafeSupplier) {

		try {
			position = positionUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double position;

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public void setRegionCode(
		UnsafeSupplier<String, Exception> regionCodeUnsafeSupplier) {

		try {
			regionCode = regionCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String regionCode;

	public Map<String, String> getTitle_i18n() {
		return title_i18n;
	}

	public void setTitle_i18n(Map<String, String> title_i18n) {
		this.title_i18n = title_i18n;
	}

	public void setTitle_i18n(
		UnsafeSupplier<Map<String, String>, Exception>
			title_i18nUnsafeSupplier) {

		try {
			title_i18n = title_i18nUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, String> title_i18n;

	@Override
	public Region clone() throws CloneNotSupportedException {
		return (Region)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Region)) {
			return false;
		}

		Region region = (Region)object;

		return Objects.equals(toString(), region.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return RegionSerDes.toJSON(this);
	}

}