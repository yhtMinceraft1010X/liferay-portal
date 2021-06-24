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

const ALWAYS_SOLID = 'always-solid';
const BANNER = 'banner';
const IS_OPEN = 'is-open';
const IS_SOLID = 'is-solid';
const MAX_LEVEL = 5;
const PARALLAX_CONTAINER = 'parallax';
const SIGN_IN = 'sign-in';
const SIGN_IN_BTN = 'sign-in-btn';
const SIGN_IN_WRAPPER = 'sign-in-wrapper';
const SOLID_AFTER_PIXELS = 56;

const banner = window.document.querySelector(`#${BANNER}`);
const scrollingElement = window.document.querySelector(
	`.${PARALLAX_CONTAINER}`
);
const signInElement = window.document.querySelector(`.${SIGN_IN}`);
const signInBtn = window.document.querySelector(`.${SIGN_IN_BTN}`);

const CHECKOUT_PATH_MATCHER = '/checkout';

function isButton(target) {
	if (target.classList.contains(SIGN_IN_BTN)) {
		return true;
	}

	let parentElement = target.parentElement;

	for (let i = 0; i < MAX_LEVEL; i++) {
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

function toggleSignIn(event) {
	if (isButton(event.target)) {
		const wrapper = window.document.querySelector(`.${SIGN_IN_WRAPPER}`);

		wrapper.classList.toggle(IS_OPEN, !isOpen(wrapper));
	}
}

function solidifyBanner(event) {
	const scrollTop = event.target.scrollTop,
		isSolid = scrollTop > SOLID_AFTER_PIXELS;

	banner.classList.toggle(IS_SOLID, isSolid);
}

function HeaderHandler() {
	if (banner && !banner.classList.contains(ALWAYS_SOLID)) {
		scrollingElement.addEventListener('scroll', solidifyBanner);
	}

	if (signInBtn) {
		signInBtn.addEventListener('click', toggleSignIn);
	}

	if (window.location.href.includes(CHECKOUT_PATH_MATCHER)) {
		signInElement.classList.add('invisible');
	}
}

export default HeaderHandler;
