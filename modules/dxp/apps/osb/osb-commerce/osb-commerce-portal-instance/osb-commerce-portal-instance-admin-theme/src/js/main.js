/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

AUI().ready(() => {
	const ALWAYS_SOLID = 'always-solid',
		BANNER = 'banner',
		IS_OPEN = 'is-open',
		IS_SOLID = 'is-solid',
		MAX_LEVEL = 5,
		SIGN_IN_BTN = 'sign-in-btn',
		SIGN_IN_WRAPPER = 'sign-in-wrapper',
		SOLID_AFTER_PIXELS = 56;

	const banner = window.document.querySelector('#' + BANNER),
		signInBtn = window.document.querySelector('.' + SIGN_IN_BTN);

	function isButton(target) {
		if (target.classList.contains(SIGN_IN_BTN)) {
			return true;
		}

		var parentElement = target.parentElement;

		for (var i = 0; i < MAX_LEVEL; i++) {
			if (parentElement.classList.contains(SIGN_IN_BTN)) {
				return true;
			}

			parentElement = parentElement.parentElement;
		}

		return false;
	}

	function isOpen(element) {
		return element.classList.contains(IS_OPEN);
	}

	function toggleSignIn(e) {
		if (isButton(e.target)) {
			const wrapper = window.document.querySelector(
				'.' + SIGN_IN_WRAPPER
			);

			wrapper.classList.toggle(IS_OPEN, !isOpen(wrapper));
		}
	}

	function solidifyBanner(e) {
		const scrollTop = e.target.scrollingElement.scrollTop,
			isSolid = scrollTop > SOLID_AFTER_PIXELS;

		banner.classList.toggle(IS_SOLID, isSolid);
	}

	if (banner && !banner.classList.contains(ALWAYS_SOLID)) {
		window.addEventListener('scroll', solidifyBanner);
	}

	if (signInBtn) {
		signInBtn.addEventListener('click', toggleSignIn);
	}
});
