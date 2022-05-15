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

package com.liferay.portal.security.audit.storage.internal;

import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.security.audit.AuditEvent;
import com.liferay.portal.security.audit.AuditEventManager;
import com.liferay.portal.security.audit.storage.service.AuditEventLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Greenwald
 */
@Component(immediate = true, service = AuditEventManager.class)
public class AuditEventManagerImpl implements AuditEventManager {

	@Override
	public AuditEvent addAuditEvent(AuditMessage auditMessage) {
		return _createAuditEvent(
			_auditEventLocalService.addAuditEvent(auditMessage));
	}

	@Override
	public AuditEvent fetchAuditEvent(long auditEventId) {
		return _createAuditEvent(
			_auditEventLocalService.fetchAuditEvent(auditEventId));
	}

	@Override
	public List<AuditEvent> getAuditEvents(
		long companyId, int start, int end,
		OrderByComparator
			<com.liferay.portal.security.audit.storage.model.AuditEvent>
				orderByComparator) {

		return _translate(
			_auditEventLocalService.getAuditEvents(
				companyId, start, end, orderByComparator));
	}

	@Override
	public List<AuditEvent> getAuditEvents(
		long companyId, long userId, String userName, Date createDateGT,
		Date createDateLT, String eventType, String className, String classPK,
		String clientHost, String clientIP, String serverName, int serverPort,
		String sessionID, boolean andSearch, int start, int end,
		OrderByComparator
			<com.liferay.portal.security.audit.storage.model.AuditEvent>
				orderByComparator) {

		List<com.liferay.portal.security.audit.storage.model.AuditEvent>
			auditEvents = _auditEventLocalService.getAuditEvents(
				companyId, userId, userName, createDateGT, createDateLT,
				eventType, className, classPK, clientHost, clientIP, serverName,
				serverPort, sessionID, andSearch, start, end,
				orderByComparator);

		return _translate(auditEvents);
	}

	@Override
	public int getAuditEventsCount(long companyId) {
		return _auditEventLocalService.getAuditEventsCount(companyId);
	}

	@Override
	public int getAuditEventsCount(
		long companyId, long userId, String userName, Date createDateGT,
		Date createDateLT, String eventType, String className, String classPK,
		String clientHost, String clientIP, String serverName, int serverPort,
		String sessionID, boolean andSearch) {

		return _auditEventLocalService.getAuditEventsCount(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch);
	}

	private AuditEvent _createAuditEvent(
		com.liferay.portal.security.audit.storage.model.AuditEvent
			auditEventModel) {

		return AuditEventAutoEscapeBeanHandler.createProxy(auditEventModel);
	}

	private List<AuditEvent> _translate(
		List<com.liferay.portal.security.audit.storage.model.AuditEvent>
			auditEventModels) {

		if (auditEventModels.isEmpty()) {
			return Collections.emptyList();
		}

		List<AuditEvent> auditEvents = new ArrayList<>(auditEventModels.size());

		for (com.liferay.portal.security.audit.storage.model.AuditEvent
				auditEventModel : auditEventModels) {

			auditEvents.add(_createAuditEvent(auditEventModel));
		}

		return auditEvents;
	}

	@Reference
	private AuditEventLocalService _auditEventLocalService;

}