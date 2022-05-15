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

import {Disposable, buildFragment} from 'frontend-js-web';

class Surface extends Disposable {

	/**
	 * Surface class representing the references to elements on the page that
	 * can potentially be updated by <code>App</code>.
	 * @param {string} id
	 */
	constructor(id) {
		super();

		if (!id) {
			throw new Error(
				'Surface element id not specified. A surface element requires a valid id.'
			);
		}

		/**
		 * Holds the active child element.
		 * @type {Element}
		 * @default null
		 * @protected
		 */
		this.activeChild = null;

		/**
		 * Holds the default child element.
		 * @type {Element}
		 * @default null
		 * @protected
		 */
		this.defaultChild = null;

		/**
		 * Holds the element with the specified surface id, if not found creates a
		 * new element with the specified id.
		 * @type {Element}
		 * @default null
		 * @protected
		 */
		this.element = null;

		/**
		 * Holds the surface id.
		 * @type {String}
		 * @default null
		 * @protected
		 */
		this.id = id;

		/**
		 * Holds the default transitionFn for the surfaces.
		 * @param {?Element=} from The visible surface element.
		 * @param {?Element=} to The surface element to be flipped.
		 * @default null
		 */
		this.transitionFn = null;

		this.defaultChild = this.getChild(Surface.DEFAULT);
		this.maybeWrapContentAsDefault_();
		this.activeChild = this.defaultChild;
	}

	/**
	 * Adds screen content to a surface. If content hasn't been passed, see if
	 * an element exists in the DOM that matches the id. By convention, the
	 * element should already be nested in the right element and should have an
	 * id that is a concatentation of the surface id + '-' + the screen id.
	 * @param {!string} screenId The screen id the content belongs too.
	 * @param {?string|Element=} opt_content The string content or element to
	 *     add be added as surface content.
	 * @return {Element}
	 */
	addContent(screenId, opt_content) {
		const fragment =
			typeof opt_content === 'string'
				? buildFragment(opt_content)
				: opt_content;

		Liferay.DOMTaskRunner.runTasks(fragment);

		let child = this.defaultChild;

		if (fragment) {
			child = this.getChild(screenId);

			if (child) {
				while (child.firstChild) {
					child.removeChild(child.firstChild);
				}
			}
			else {
				child = this.createChild(screenId);

				this.transition(child, null);
			}

			child.appendChild(fragment);
		}

		const element = this.getElement();

		if (element && child) {
			element.appendChild(child);
		}

		return child;
	}

	/**
	 * Creates child node for the surface.
	 * @param {!string} screenId The screen id.
	 * @return {Element}
	 */
	createChild(screenId) {
		var child = document.createElement('div');
		child.setAttribute('id', this.makeId_(screenId));

		return child;
	}

	/**
	 * Gets child node of the surface.
	 * @param {!string} screenId The screen id.
	 * @return {?Element}
	 */
	getChild(screenId) {
		return document.getElementById(this.makeId_(screenId));
	}

	/**
	 * Gets the surface element from element, and sets it to the el property of
	 * the current instance.
	 * <code>this.element</code> will be used.
	 * @return {?Element} The current surface element.
	 */
	getElement() {
		if (this.element) {
			return this.element;
		}
		this.element = document.getElementById(this.id);

		return this.element;
	}

	/**
	 * Gets the surface id.
	 * @return {String}
	 */
	getId() {
		return this.id;
	}

	/**
	 * Gets the surface transition function.
	 * See <code>Surface.defaultTransition</code>.
	 * @return {?Function=} The transition function.
	 */
	getTransitionFn() {
		return this.transitionFn;
	}

	/**
	 * Makes the id for the element that holds content for a screen.
	 * @param {!string} screenId The screen id the content belongs too.
	 * @return {String}
	 * @private
	 */
	makeId_(screenId) {
		return this.id + '-' + screenId;
	}

	/**
	 * If default child is missing, wraps surface content as default child. If
	 * surface have static content, make sure to place a
	 * <code>surfaceId-default</code> element inside surface, only contents
	 * inside the default child will be replaced by navigation.
	 */
	maybeWrapContentAsDefault_() {
		var element = this.getElement();
		if (element && !this.defaultChild) {
			var childNodesToWrap = [];

			element.childNodes.forEach((childNode) => {

				/*
				 * Some child nodes must be kept out of the Senna surface.
				 *
				 * We don't have any good mechanism to do it and we don't want
				 * to put anything in place given all this is more or less
				 * legacy.
				 *
				 * Thus, we simply skip some nodes that are known to us and
				 * leave them as direct children of <body>, outside the default
				 * Senna surface.
				 *
				 * See LPS-151462 for more information.
				 */
				if (childNode.classList) {
					if (
						childNode.classList.contains('yui3-dd-proxy') ||
						childNode.classList.contains('yui3-dd-shim')
					) {
						return;
					}
				}

				childNodesToWrap.push(childNode);
			});

			var fragment = document.createDocumentFragment();

			childNodesToWrap.forEach((childNode) => {
				fragment.appendChild(childNode);
			});

			this.defaultChild = this.addContent(Surface.DEFAULT, fragment);
			this.transition(null, this.defaultChild);
		}
	}

	/**
	 * Sets the surface id.
	 * @param {!string} id
	 */
	setId(id) {
		this.id = id;
	}

	/**
	 * Sets the surface transition function.
	 * See <code>Surface.defaultTransition</code>.
	 * @param {?Function=} transitionFn The transition function.
	 */
	setTransitionFn(transitionFn) {
		this.transitionFn = transitionFn;
	}

	/**
	 * Shows screen content from a surface.
	 * @param {String} screenId The screen id to show.
	 * @return {Promise} Pauses the navigation until it is resolved.
	 */
	show(screenId) {
		var from = this.activeChild;
		var to = this.getChild(screenId);
		if (!to) {
			to = this.defaultChild;
		}
		this.activeChild = to;

		return this.transition(from, to).finally(() => {
			if (from && from !== to) {
				from.remove();
			}
		});
	}

	/**
	 * Removes screen content from a surface.
	 * @param {!string} screenId The screen id to remove.
	 */
	remove(screenId) {
		var child = this.getChild(screenId);
		if (child) {
			child.remove();
		}
	}

	/**
	 * @return {String}
	 */
	toString() {
		return this.id;
	}

	/**
	 * Invokes the transition function specified on <code>transition</code> attribute.
	 * @param {?Element=} from
	 * @param {?Element=} to
	 * @return {?Promise=} This can return a promise, which will pause the
	 *     navigation until it is resolved.
	 */
	transition(from, to) {
		var transitionFn = this.transitionFn || Surface.defaultTransition;

		return Promise.resolve(transitionFn.call(this, from, to));
	}
}

/**
 * Holds the default surface name. Elements on the page must contain a child
 * element containing the default content, this element must be as following:
 *
 * Example:
 * <code>
 *   <div id="mysurface">
 *     <div id="mysurface-default">Default surface content.</div>
 *   </div>
 * </code>
 *
 * The default content is relevant for the initial page content. When a
 * screen doesn't provide content for the surface the default content is
 * restored into the page.
 *
 * @type {!String}
 * @default default
 * @static
 */
Surface.DEFAULT = 'default';

/**
 * Holds the default transition for all surfaces. Each surface could have its
 * own transition.
 *
 * Example:
 *
 * <code>
 * surface.setTransitionFn(function(from, to) {
 *   if (from) {
 *     from.style.display = 'none';
 *     from.classList.remove('flipped');
 *   }
 *   if (to) {
 *     to.style.display = 'block';
 *     to.classList.add('flipped');
 *   }
 *   return null;
 * });
 * </code>
 *
 * @param {?Element=} from The visible surface element.
 * @param {?Element=} to The surface element to be flipped.
 * @static
 */
Surface.defaultTransition = function (from, to) {
	if (from) {
		from.style.display = 'none';
		from.classList.remove('flipped');
	}
	if (to) {
		to.style.display = 'block';
		to.classList.add('flipped');
	}
};

export default Surface;
