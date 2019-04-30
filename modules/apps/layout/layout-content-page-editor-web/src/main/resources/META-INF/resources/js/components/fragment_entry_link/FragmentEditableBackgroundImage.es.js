import {Config} from 'metal-state';
import Component from 'metal-component';
import {Store} from '../../store/store.es';

import FragmentEditableFieldTooltip from './FragmentEditableFieldTooltip.es';
import getConnectedComponent from '../../store/ConnectedComponent.es';

/**
 * FragmentEditableBackgroundImage
 */
class FragmentEditableBackgroundImage extends Component {

	/**
	 * Returns the list of buttons to be shown inside the tooltip.
	 * @param {boolean} showMapping
	 * @return {Array<{id: string, label: string}>}
	 * @review
	 */
	static getButtons(showMapping) {
		const buttons = [
			{
				icon: 'pencil',
				id: 'select',
				label: Liferay.Language.get('select-background')
			}
		];

		if (showMapping) {
			buttons.push(
				{
					icon: 'bolt',
					id: 'map',
					label: Liferay.Language.get('map-background')
				}
			);
		}

		return buttons;
	}
	
	/**
	 * @inheritDoc
	 * @review
	 */
	created() {
		this._handleClick = this._handleClick.bind(this);
		this._handleOutsideTooltipClick = this._handleOutsideTooltipClick.bind(this);

		this.element.addEventListener('click', this._handleClick);
	}

	/**
	 * @inheritDoc
	 * @review
	 */
	disposed() {
		this.element.removeEventListener('click', this._handleClick);
		this._disposeTooltip();
	}

	/**
	 * @private
	 * @review
	 */
	_disposeTooltip() {
		if (this._tooltip) {
			this._tooltip.dispose();
			this._tooltip = null;
		}
	}

	/**
	 * @private
	 * @review
	 */
	_handleClick() {
		if (this._tooltip) {
			this._disposeTooltip();
		}
		else {
			this._tooltip = new FragmentEditableFieldTooltip(
				{
					alignElement: this.element,
					buttons: FragmentEditableBackgroundImage.getButtons(
						this.showMapping
					),
					store: this.store
				}
			);

			this._tooltip.on('outsideTooltipClick', this._handleOutsideTooltipClick);
		}
	}

	/**
	 * @private
	 * @review
	 */
	_handleOutsideTooltipClick() {
		this._disposeTooltip();
	}

}

/**
 * State definition.
 * @static
 * @type {!Object}
 */
FragmentEditableBackgroundImage.STATE = {

	/**
	 * If <code>true</code>, the mapping is activated.
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableBackgroundImage
	 * @type {!boolean}
	 */
	showMapping: Config.bool().required(),

	/**
	 * Store instance.
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableBackgroundImage
	 * @type {Store}
	 */
	store: Config.instanceOf(Store)

};

const ConnectedFragmentEditableBackgroundImage = getConnectedComponent(
	FragmentEditableBackgroundImage,
	[
		'defaultLanguageId',
		'defaultSegmentsExperienceId',
		'imageSelectorURL',
		'languageId',
		'portletNamespace',
		'segmentsExperienceId'
	]
);

export {ConnectedFragmentEditableBackgroundImage, FragmentEditableBackgroundImage};
export default ConnectedFragmentEditableBackgroundImage;