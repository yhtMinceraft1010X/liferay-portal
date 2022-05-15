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

package com.liferay.headless.commerce.machine.learning.internal.batch.engine.v1_0;

import com.liferay.batch.engine.BatchEngineTaskItemDelegate;
import com.liferay.batch.engine.pagination.Page;
import com.liferay.batch.engine.pagination.Pagination;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.headless.commerce.machine.learning.dto.v1_0.Order;
import com.liferay.headless.commerce.machine.learning.internal.dto.v1_0.converter.OrderDTOConverter;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;

import java.io.Serializable;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	enabled = false, immediate = true,
	property = "batch.engine.task.item.delegate.name=" + OrderBatchEngineTaskItemDelegate.KEY,
	service = BatchEngineTaskItemDelegate.class
)
public class OrderBatchEngineTaskItemDelegate
	extends BaseBatchEngineTaskItemDelegate<Order> {

	public static final String KEY = "analytics-order";

	@Override
	public Class<Order> getItemClass() {
		return Order.class;
	}

	@Override
	public Page<Order> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		return search(
			_orderDTOConverter, CommerceOrder.class.getName(), filter,
			pagination, sorts, search);
	}

	@Reference
	private OrderDTOConverter _orderDTOConverter;

}