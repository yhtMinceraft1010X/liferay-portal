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

import '../../types';
import {Loader} from '@googlemaps/js-api-loader';

/**
 * @description Load google global variable asynchronously
 * @returns {void} Google Maps Autocomplete Instance
 */
const setup = (GOOGLE_API) => {
	try {
		const googleMapsLoader = new Loader({
			apiKey: GOOGLE_API,
			libraries: ['places'],
		});

		googleMapsLoader.load();
	}
	catch (error) {
		console.warn(error);
	}
};

/**
 * @param {HTMLElement} input - Pass HTML element
 * @returns {any} Google Maps Autocomplete Instance
 */
const autocomplete = (input) => {
	const google = window.google;

	if (!google) {
		throw new Error(
			'google is not defined. Please check the Google Maps API key within System Settings and ensure a valid API key is entered'
		);
	}

	if (!input) {
		throw new Error(
			'No HTMLElement was found as input. Ensure a valid HTMLElement reference is passed!'
		);
	}

	// Prevent crashes if the user hits enter in a autocomplete search

	google.maps.event.addDomListener(input, 'keydown', (event) => {
		if (event.keyCode === 13 || event.key === 'Enter') {
			event.preventDefault();
		}
	});

	return new google.maps.places.Autocomplete(input, {
		componentRestrictions: {country: 'us'},
		fields: ['address_components'],
	});
};

/**
 * @returns {any} Google Maps InfoWindow Instance
 */
const InfoWindow = () => {
	const google = window.google;

	if (!google) {
		throw new Error(
			'google is not defined. Please check the Google Maps API key within System Settings and ensure a valid API key is entered'
		);
	}

	return new google.maps.InfoWindow();
};

/**
 * @param {any} autocomplete - Google Maps Autocomplete Instance
 * @returns {Address} Normalized Address Object
 */
const getAutocompletePlaces = (autocomplete) => {
	const place = autocomplete.getPlace();

	return _adaptGoogleMapsAddressIntoAddress(place.address_components);
};

/**
 * @param {Array} addressComponents - Google Maps Address Component
 * @returns {Address} Normalized Address Object
 */
const _adaptGoogleMapsAddressIntoAddress = (addressComponents) => {
	const address = {
		city: '',
		country: '',
		state: '',
		street: '',
		streetNumber: '',
		zip: '',
	};

	addressComponents.forEach(({long_name, short_name, types}) => {
		switch (types[0]) {
			case 'street_number':
				address.streetNumber = long_name;
				break;

			case 'route':
				address.street = long_name;
				break;

			case 'locality':
				address.city = long_name;
				break;

			case 'administrative_area_level_1':
				address.state = short_name;
				break;

			case 'country':
				address.country = long_name;
				break;

			case 'postal_code':
				address.zip = short_name;
				break;

			default:
				break;
		}
	});

	return address;
};

export const GoogleMapsService = {
	InfoWindow,
	autocomplete,
	getAutocompletePlaces,
	setup,
};
