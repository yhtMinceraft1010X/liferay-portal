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

package com.liferay.search.experiences.rest.client.dto.v1_0;

import com.liferay.search.experiences.rest.client.function.UnsafeSupplier;
import com.liferay.search.experiences.rest.client.serdes.v1_0.AggregationsSerDes;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
public class Aggregations implements Cloneable, Serializable {

	public static Aggregations toDTO(String json) {
		return AggregationsSerDes.toDTO(json);
	}

	public Map<String, Aggregations> getAggs() {
		return aggs;
	}

	public void setAggs(Map<String, Aggregations> aggs) {
		this.aggs = aggs;
	}

	public void setAggs(
		UnsafeSupplier<Map<String, Aggregations>, Exception>
			aggsUnsafeSupplier) {

		try {
			aggs = aggsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, Aggregations> aggs;

	public Aggregations getAvg() {
		return avg;
	}

	public void setAvg(Aggregations avg) {
		this.avg = avg;
	}

	public void setAvg(
		UnsafeSupplier<Aggregations, Exception> avgUnsafeSupplier) {

		try {
			avg = avgUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Aggregations avg;

	public Cardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}

	public void setCardinality(
		UnsafeSupplier<Cardinality, Exception> cardinalityUnsafeSupplier) {

		try {
			cardinality = cardinalityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Cardinality cardinality;

	@Override
	public Aggregations clone() throws CloneNotSupportedException {
		return (Aggregations)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Aggregations)) {
			return false;
		}

		Aggregations aggregations = (Aggregations)object;

		return Objects.equals(toString(), aggregations.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return AggregationsSerDes.toJSON(this);
	}

}