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

package com.liferay.custom.elements.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the CustomElementsPortletDescriptor service. Represents a row in the &quot;CustomElementsPortletDesc&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see CustomElementsPortletDescriptorModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.custom.elements.model.impl.CustomElementsPortletDescriptorImpl"
)
@ProviderType
public interface CustomElementsPortletDescriptor
	extends CustomElementsPortletDescriptorModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.custom.elements.model.impl.CustomElementsPortletDescriptorImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<CustomElementsPortletDescriptor, Long>
		CUSTOM_ELEMENTS_PORTLET_DESCRIPTOR_ID_ACCESSOR =
			new Accessor<CustomElementsPortletDescriptor, Long>() {

				@Override
				public Long get(
					CustomElementsPortletDescriptor
						customElementsPortletDescriptor) {

					return customElementsPortletDescriptor.
						getCustomElementsPortletDescriptorId();
				}

				@Override
				public Class<Long> getAttributeClass() {
					return Long.class;
				}

				@Override
				public Class<CustomElementsPortletDescriptor> getTypeClass() {
					return CustomElementsPortletDescriptor.class;
				}

			};

	public String getPortletName();

}