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

package com.liferay.object.admin.rest.client.dto.v1_0;

import com.liferay.object.admin.rest.client.function.UnsafeSupplier;
import com.liferay.object.admin.rest.client.serdes.v1_0.ObjectViewFilterColumnSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ObjectViewFilterColumn implements Cloneable, Serializable {

	public static ObjectViewFilterColumn toDTO(String json) {
		return ObjectViewFilterColumnSerDes.toDTO(json);
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public String getFilterTypeAsString() {
		if (filterType == null) {
			return null;
		}

		return filterType.toString();
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public void setFilterType(
		UnsafeSupplier<FilterType, Exception> filterTypeUnsafeSupplier) {

		try {
			filterType = filterTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected FilterType filterType;

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

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setJson(UnsafeSupplier<String, Exception> jsonUnsafeSupplier) {
		try {
			json = jsonUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String json;

	public String getObjectFieldName() {
		return objectFieldName;
	}

	public void setObjectFieldName(String objectFieldName) {
		this.objectFieldName = objectFieldName;
	}

	public void setObjectFieldName(
		UnsafeSupplier<String, Exception> objectFieldNameUnsafeSupplier) {

		try {
			objectFieldName = objectFieldNameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String objectFieldName;

	public String getValueSummary() {
		return valueSummary;
	}

	public void setValueSummary(String valueSummary) {
		this.valueSummary = valueSummary;
	}

	public void setValueSummary(
		UnsafeSupplier<String, Exception> valueSummaryUnsafeSupplier) {

		try {
			valueSummary = valueSummaryUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String valueSummary;

	@Override
	public ObjectViewFilterColumn clone() throws CloneNotSupportedException {
		return (ObjectViewFilterColumn)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ObjectViewFilterColumn)) {
			return false;
		}

		ObjectViewFilterColumn objectViewFilterColumn =
			(ObjectViewFilterColumn)object;

		return Objects.equals(toString(), objectViewFilterColumn.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ObjectViewFilterColumnSerDes.toJSON(this);
	}

	public static enum FilterType {

		EXCLUDES("excludes"), INCLUDES("includes");

		public static FilterType create(String value) {
			for (FilterType filterType : values()) {
				if (Objects.equals(filterType.getValue(), value) ||
					Objects.equals(filterType.name(), value)) {

					return filterType;
				}
			}

			return null;
		}

		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private FilterType(String value) {
			_value = value;
		}

		private final String _value;

	}

}