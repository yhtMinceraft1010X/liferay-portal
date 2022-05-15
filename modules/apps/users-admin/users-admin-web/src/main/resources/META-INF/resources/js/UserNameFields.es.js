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

import {PortletBase, createPortletURL} from 'frontend-js-web';
import {Config} from 'metal-state';

/**
 * Handles actions to display user name field for a given locale.
 */
class UserNameFields extends PortletBase {

	/**
	 * @inheritDoc
	 */
	attached() {
		this.languageIdSelectNode.addEventListener(
			'change',
			this._handleSelectChange
		);
	}

	/**
	 * @inheritDoc
	 */
	created() {
		this._handleSelectChange = this._handleSelectChange.bind(this);

		this._formDataCache = {};
		this._maxLengthsCache = {};

		this._loadingAnimationMarkupText = `<div class="loading-animation" id="${this.portletNamespace}loadingUserNameFields"></div>`;
	}

	/**
	 * @inheritDoc
	 */
	detached() {
		super.detached();

		this.languageIdSelectNode.removeEventListener(
			'change',
			this._handleSelectChange
		);
	}

	/**
	 * Updates the user name fields to display the appropriate fields for the
	 * given locale.
	 *
	 * @param {string} languageId The language id used when retrieving the user
	 * name fields.
	 */
	updateUserNameFields(languageId) {
		this._setUp();

		this._getURL(languageId)
			.then(fetch)
			.then((response) => response.text())
			.then(this._insertUserNameFields.bind(this))
			.then(this._cleanUp.bind(this))
			.catch(this._handleError.bind(this));
	}

	/**
	 * Caches the values and maxLength attribute values from the current
	 * user name fields.
	 *
	 * @protected
	 */
	_cacheData() {
		const formData = new FormData(this.formNode);

		if (!formData.forEach) {
			return;
		}

		formData.forEach((value, name) => {
			const field = this.userNameFieldsNode.querySelector('#' + name);

			if (field) {
				this._formDataCache[name] = value;

				if (field.hasAttribute('maxLength')) {
					this._maxLengthsCache[name] = field.getAttribute(
						'maxLength'
					);
				}
			}
		});
	}

	/**
	 * Inserts a loading indicator before the user name fields and hide
	 * the user name fields.
	 *
	 * @protected
	 */
	_createLoadingIndicator() {
		this.userNameFieldsNode.insertAdjacentHTML(
			'beforebegin',
			this._loadingAnimationMarkupText
		);

		this.userNameFieldsNode.classList.add('hide');
	}

	_cleanUp() {
		this._removeLoadingIndicator();

		this._populateData();
	}

	/**
	 * Returns a promise containing the URL to be used to retrieve the user
	 * name fields.
	 *
	 * @param {string} languageId The language id to be set on the URL.
	 * @protected
	 * @return {Promise} A promise to be resolved with the constructed URL
	 */
	_getURL(languageId) {
		return new Promise((resolve) => {
			const url = createPortletURL(this.baseURL, {
				languageId,
			});

			resolve(url);
		});
	}

	/**
	 * Logs any error in the promise chain and removes the loading indicator.
	 *
	 * @param {Error} error The error object
	 * @protected
	 */
	_handleError(error) {
		console.error(error);

		this._removeLoadingIndicator();
	}

	/**
	 * Handles the change event when selecting a new language.
	 *
	 * @param {Event} event The event object.
	 * @protected
	 */
	_handleSelectChange(event) {
		this.updateUserNameFields(event.currentTarget.value);
	}

	/**
	 * Replaces the HTML of the user name fields with the given HTML.
	 *
	 * @param {string} markupText The markup text used to create and insert the
	 * new user name fields.
	 * @protected
	 */
	_insertUserNameFields(markupText) {
		const temp = document.implementation.createHTMLDocument('');

		temp.body.innerHTML = markupText;

		const newUserNameFields = temp.getElementById(
			`${this.portletNamespace}userNameFields`
		);

		if (newUserNameFields) {
			this.userNameFieldsNode.innerHTML = newUserNameFields.innerHTML;
		}
	}

	/**
	 * Sets the values and maxLength attributes of the current user name fields
	 * with the data cached in this._cacheData.
	 *
	 * @protected
	 */
	_populateData() {
		const entries = Object.entries(this._formDataCache);

		entries.forEach(([name, value]) => {
			const newField = this.userNameFieldsNode.querySelector('#' + name);

			if (newField) {
				newField.value = value;

				if (Object.hasOwnProperty.call(this._maxLengthsCache, name)) {
					newField.setAttribute(
						'maxLength',
						this._maxLengthsCache[name]
					);
				}
			}
		});
	}

	/**
	 * Removes the loading indicator and shows the user name
	 * fields.
	 *
	 * @protected
	 */
	_removeLoadingIndicator() {
		const loadingUserNameFields = this.one('#loadingUserNameFields');

		if (loadingUserNameFields) {
			loadingUserNameFields.remove();
		}

		this.userNameFieldsNode.classList.remove('hide');
	}

	_setElement(selector) {
		if (typeof selector === 'string') {
			return this.one(selector);
		}

		return selector;
	}

	/**
	 * Stores the current user name fields data and creates the loading
	 * indicator
	 *
	 * @protected
	 */
	_setUp() {
		this._cacheData();

		this._createLoadingIndicator();
	}
}

UserNameFields.STATE = {

	/**
	 * Uri to return the user name data.
	 * @instance
	 * @memberof UserNameFields
	 * @type {String}
	 */
	baseURL: Config.required().string().writeOnce(),

	/**
	 * Form node.
	 * @instance
	 * @memberof UserNameFields
	 * @type {String}
	 */
	formNode: Config.required().setter('_setElement').writeOnce(),

	/**
	 * Language id select field.
	 * @instance
	 * @memberof UserNameFields
	 * @type {String}
	 */
	languageIdSelectNode: Config.required().setter('_setElement').writeOnce(),

	/**
	 * HTML element containing the user name fields.
	 * @instance
	 * @memberof UserNameFields
	 * @type {String}
	 */
	userNameFieldsNode: Config.required().setter('_setElement').writeOnce(),
};

export default UserNameFields;
