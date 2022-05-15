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

package com.liferay.commerce.shipping.engine.fixed.web.internal.model;

/**
 * @author Marco Leo
 */
public class ShippingFixedOption {

	public ShippingFixedOption(
		String description, String name, double priority,
		long shippingFixedOptionId) {

		_description = description;
		_name = name;
		_priority = priority;
		_shippingFixedOptionId = shippingFixedOptionId;
	}

	public String getDescription() {
		return _description;
	}

	public String getName() {
		return _name;
	}

	public double getPriority() {
		return _priority;
	}

	public long getShippingFixedOptionId() {
		return _shippingFixedOptionId;
	}

	private final String _description;
	private final String _name;
	private final double _priority;
	private final long _shippingFixedOptionId;

}