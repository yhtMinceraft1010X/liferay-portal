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

import Diagram from '../../../src/main/resources/META-INF/resources/js/DiagramWithAutomapping/Diagram';

import '@testing-library/jest-dom/extend-expect';
import {
	act,
	cleanup,
	fireEvent,
	render,
	waitForElement,
} from '@testing-library/react';
import fetchMock from 'fetch-mock';
import React from 'react';

import '../../../dev/public/js/static-env-utils';
import {mockCommonEndpoints, pinsData, productData} from '../../utilities';

const mappedSequences = new Set(pinsData.map((pin) => pin.sequence));

const svgContent = `
    <svg>
        <g class="sequences">
            <text>first</text>
            <text>second</text>
            <text>third</text>
            <text>fourth</text>
        </g>
    </svg>
`;

const defaultDiagramProps = {
	channelId: 'fake-channel-id',
	datasetDisplayId: 'testDatasetDisplayId',
	diagramId: 'fake-diagram-id',
	imageURL: '/image/test-url.svg',
	pinsCSSSelectors: ['.sequences text'],
	pinsRadius: 1,
	productId: 'fake-product-id',
};

describe('Diagram', () => {
	beforeEach(() => {
		mockCommonEndpoints();

		fetchMock.mock(defaultDiagramProps.imageURL, () => {
			return {
				body: svgContent,
				headers: new Headers({'Content-Type': 'text/html'}),
			};
		});
	});

	afterEach(() => {
		fetchMock.restore();
	});

	describe('SVG Renderer Admin', () => {
		let diagram;

		beforeEach(async () => {
			await act(async () => {
				diagram = await render(
					<Diagram {...defaultDiagramProps} isAdmin={true} />
				);
			});

			await waitForElement(async () =>
				diagram.container.querySelector('.sequences')
			);
		});

		afterEach(() => {
			diagram?.unmount();

			cleanup();
		});

		it('must render a Diagram', () => {
			expect(diagram.container).toBeInTheDocument();
		});

		it('must not show a pin size button when on admin', () => {
			expect(diagram.container).not.toHaveTextContent('pin-size');
		});

		it('must render a SVG image within a component', () => {
			const svg = diagram.container.querySelector('svg .sequences');

			expect(svg).toBeInTheDocument();
		});

		it('must show the pins', () => {
			const pinsNodes = diagram.container.querySelectorAll(
				defaultDiagramProps.pinsCSSSelectors
			);

			expect(pinsNodes.length).toBeGreaterThan(0);
		});

		it('must show a tooltip when a pin is clicked', () => {
			const pinsNodes = diagram.container.querySelectorAll(
				defaultDiagramProps.pinsCSSSelectors
			);

			fireEvent.click(pinsNodes[0]);

			const tooltip = document.querySelector('.diagram-tooltip');

			expect(tooltip).toBeInTheDocument();
		});

		it('must show consistent data within the tooltip', () => {
			const pinsNodes = diagram.container.querySelectorAll(
				defaultDiagramProps.pinsCSSSelectors
			);

			pinsNodes.forEach((pinNode, index) => {
				fireEvent.click(pinNode);

				const tooltip = document.querySelector(
					'.diagram-admin-tooltip'
				);

				expect(tooltip).toBeInTheDocument();

				const sequenceInput = document.getElementById('sequenceInput');
				const typeInput = document.getElementById('typeInput');
				const quantityInput = document.getElementById('quantityInput');

				expect(sequenceInput.value).toBe(pinNode.textContent);

				if (mappedSequences.has(pinNode.textContent)) {
					expect(typeInput.value).toBe(
						pinsData[index].mappedProduct.type
					);

					if (pinsData[index].mappedProduct.type === 'diagram') {
						expect(quantityInput).not.toBeInTheDocument();
					}
					else {
						expect(Number(quantityInput.value)).toBe(
							Number(pinsData[index].mappedProduct.quantity)
						);
					}
				}
				else {
					expect(typeInput.value).toBe('sku');
					expect(quantityInput.value).toBe('1');
				}
			});
		});
	});

	describe('SVG Renderer Frontstore', () => {
		let diagram;

		beforeEach(async () => {
			await act(async () => {
				diagram = await render(
					<Diagram {...defaultDiagramProps} isAdmin={false} />
				);
			});

			await waitForElement(async () =>
				diagram.container.querySelector('.sequences')
			);
		});

		afterEach(() => {
			diagram?.unmount();
		});

		it('must not show a pin size button when on the frontstore', async () => {
			expect(diagram.container).not.toHaveTextContent('pin-size');
		});

		it('must show the pins', async () => {
			const pinsNodes = diagram.container.querySelectorAll(
				defaultDiagramProps.pinsCSSSelectors
			);

			expect(pinsNodes.length).toBeGreaterThan(0);
		});

		it('must do nothing when an unmapped pin is clicked', () => {
			const unselectablePins = diagram.container.querySelectorAll(
				defaultDiagramProps.pinsCSSSelectors + `:not(.pin)`
			);

			unselectablePins.forEach((unselectablePin) => {
				fireEvent.click(unselectablePin);

				const tooltip = document.querySelector(
					'.diagram-storefront-tooltip'
				);

				expect(tooltip).not.toBeInTheDocument();
			});
		});

		it('must show consistent data within the tooltip', async () => {
			const pinNodes = diagram.container.querySelectorAll('.pin');

			expect(pinNodes.length).toBe(3);

			fireEvent.click(pinNodes[0]);

			await waitForElement(async () =>
				document.querySelector('.diagram-storefront-tooltip')
			);

			const tooltip = document.querySelector(
				'.diagram-storefront-tooltip'
			);

			const image = tooltip.querySelector('.sticker-img');
			const link = tooltip.querySelector('a');
			const title = tooltip.querySelector('h4');

			expect(image.alt).toBe(productData.name);
			expect(image.src).toContain(productData.urlImage);
			expect(link.href).toContain(
				productData.urls[Liferay.ThemeDisplay.getLanguageId()]
			);
			expect(title.textContent).toBe(productData.name);
		});
	});
});
