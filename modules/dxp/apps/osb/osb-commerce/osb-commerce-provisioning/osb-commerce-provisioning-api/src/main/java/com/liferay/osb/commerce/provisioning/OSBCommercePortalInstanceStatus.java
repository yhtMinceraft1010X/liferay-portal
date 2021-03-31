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

package com.liferay.osb.commerce.provisioning;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * @author Ivica Cardic
 */
public enum OSBCommercePortalInstanceStatus {

	ACTIVE(WorkflowConstants.STATUS_APPROVED), CANCELLED(1),
	FAILED(WorkflowConstants.STATUS_INCOMPLETE),
	IN_PROGRESS(WorkflowConstants.STATUS_PENDING);

	public static OSBCommercePortalInstanceStatus parse(String statusString) {
		if (statusString == null) {
			return null;
		}

		int status = GetterUtil.getInteger(statusString);

		if (status == OSBCommercePortalInstanceStatus.ACTIVE.getStatus()) {
			return OSBCommercePortalInstanceStatus.ACTIVE;
		}
		else if (status ==
					OSBCommercePortalInstanceStatus.CANCELLED.getStatus()) {

			return OSBCommercePortalInstanceStatus.CANCELLED;
		}
		else {
			return OSBCommercePortalInstanceStatus.IN_PROGRESS;
		}
	}

	public int getStatus() {
		return _status;
	}

	private OSBCommercePortalInstanceStatus(int status) {
		_status = status;
	}

	private final int _status;

}