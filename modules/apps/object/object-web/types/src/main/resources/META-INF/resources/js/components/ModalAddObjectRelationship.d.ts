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

import React from 'react';
interface IProps extends React.HTMLAttributes<HTMLElement> {
	apiURL: string;
	ffOneToManyRelationshipCustomAndNativeObjects: boolean;
	ffOneToOneRelationshipConfigurationEnabled: boolean;
	objectDefinitionId: number;
	observer: any;
	onClose: () => void;
	system: boolean;
}
declare const ModalWithProvider: React.FC<IProps>;
export default ModalWithProvider;
