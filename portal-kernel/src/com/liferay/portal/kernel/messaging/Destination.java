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

package com.liferay.portal.kernel.messaging;

import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
@ProviderType
public interface Destination {

	public boolean addDestinationEventListener(
		DestinationEventListener destinationEventListener);

	public void close();

	public void close(boolean force);

	public void copyDestinationEventListeners(Destination destination);

	public void copyMessageListeners(Destination destination);

	public void destroy();

	public DestinationStatistics getDestinationStatistics();

	public String getDestinationType();

	public int getMessageListenerCount();

	public Set<MessageListener> getMessageListeners();

	public String getName();

	public Set<WebhookEvent> getWebhookEvents();

	public boolean isRegistered();

	public boolean isWebhookCapable(long companyId);

	public void open();

	public boolean register(MessageListener messageListener);

	public boolean register(
		MessageListener messageListener, ClassLoader classLoader);

	public boolean removeDestinationEventListener(
		DestinationEventListener destinationEventListener);

	public void removeDestinationEventListeners();

	public void send(Message message);

	public boolean unregister(MessageListener messageListener);

	public void unregisterMessageListeners();

	public class WebhookEvent {

		public WebhookEvent(String description, String key, String name) {
			_description = description;
			_key = key;
			_name = name;
		}

		public String getDescription() {
			return _description;
		}

		public String getKey() {
			return _key;
		}

		public String getName() {
			return _name;
		}

		private final String _description;
		private final String _key;
		private final String _name;

	}

}