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

package com.liferay.search.experiences.web.internal.search.index;

/**
 * @author Petteri Karttunen
 */
public class FieldMappingInfo {

	public FieldMappingInfo(int languageIdIndex, String name, String type) {
		_languageIdIndex = languageIdIndex;
		_name = name;
		_type = type;
	}

	public FieldMappingInfo(String name, String type) {
		_name = name;
		_type = type;
	}

	public int getLanguageIdIndex() {
		return _languageIdIndex;
	}

	public String getName() {
		return _name;
	}

	public String getType() {
		return _type;
	}

	private int _languageIdIndex;
	private final String _name;
	private final String _type;

}