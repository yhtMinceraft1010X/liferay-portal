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

package com.liferay.dynamic.data.mapping.form.field.type.internal.select.multi.language.option;

import com.liferay.dynamic.data.mapping.form.field.type.internal.select.SelectDDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * @author Rodrigo Paulino
 */
public class
	MultiLanguageOptionSelectDDMFormFieldTemplateContextContributorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_setUpLanguageUtil();
		_setUpResourceBundle();
	}

	@Test
	public void testGetParameters() throws Exception {
		_mockSelectDDMFormFieldTemplateContextContributor(
			HashMapBuilder.<String, Object>put(
				"options",
				ListUtil.fromArray(
					HashMapBuilder.put(
						"label", "Address"
					).put(
						"value", "address"
					).build(),
					HashMapBuilder.put(
						"label", "City"
					).put(
						"value", "city"
					).build())
			).build());

		Map<String, Object> parameters =
			_multiLanguageOptionSelectDDMFormFieldTemplateContextContributor.
				getParameters(
					Mockito.mock(DDMFormField.class),
					Mockito.mock(DDMFormFieldRenderingContext.class));

		Assert.assertEquals(
			ListUtil.fromArray(
				HashMapBuilder.<String, Object>put(
					"label",
					HashMapBuilder.put(
						"en_US", "Address"
					).put(
						"pt_BR", "Endereço"
					).build()
				).put(
					"value", "address"
				).build(),
				HashMapBuilder.<String, Object>put(
					"label",
					HashMapBuilder.put(
						"en_US", "City"
					).put(
						"pt_BR", "Cidade"
					).build()
				).put(
					"value", "city"
				).build()),
			parameters.get("options"));
	}

	private void _mockSelectDDMFormFieldTemplateContextContributor(
		Map<String, Object> optionsMap) {

		SelectDDMFormFieldTemplateContextContributor
			mockSelectDDMFormFieldTemplateContextContributor = Mockito.mock(
				SelectDDMFormFieldTemplateContextContributor.class);

		Mockito.when(
			mockSelectDDMFormFieldTemplateContextContributor.getParameters(
				Matchers.any(DDMFormField.class),
				Matchers.any(DDMFormFieldRenderingContext.class))
		).thenReturn(
			optionsMap
		);

		ReflectionTestUtil.setFieldValue(
			_multiLanguageOptionSelectDDMFormFieldTemplateContextContributor,
			"_selectDDMFormFieldTemplateContextContributor",
			mockSelectDDMFormFieldTemplateContextContributor);
	}

	private void _setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		Mockito.when(
			_language.getAvailableLocales()
		).thenReturn(
			SetUtil.fromArray(LocaleUtil.BRAZIL, LocaleUtil.US)
		);

		Mockito.when(
			_language.getLanguageId(Matchers.eq(LocaleUtil.BRAZIL))
		).thenReturn(
			LocaleUtil.toLanguageId(LocaleUtil.BRAZIL)
		);

		Mockito.when(
			_language.getLanguageId(Matchers.eq(LocaleUtil.US))
		).thenReturn(
			LocaleUtil.toLanguageId(LocaleUtil.US)
		);

		languageUtil.setLanguage(_language);
	}

	private void _setUpResourceBundle() {
		PortalUtil portalUtil = new PortalUtil();

		Portal portal = Mockito.mock(Portal.class);

		portalUtil.setPortal(portal);

		ResourceBundleLoader resourceBundleLoader = Mockito.mock(
			ResourceBundleLoader.class);

		ResourceBundleLoaderUtil.setPortalResourceBundleLoader(
			resourceBundleLoader);

		ResourceBundle resourceBundle1 = Mockito.mock(ResourceBundle.class);

		Mockito.when(
			portal.getResourceBundle(Matchers.eq(LocaleUtil.BRAZIL))
		).thenReturn(
			resourceBundle1
		);

		Mockito.when(
			resourceBundleLoader.loadResourceBundle(
				Matchers.eq(LocaleUtil.BRAZIL))
		).thenReturn(
			resourceBundle1
		);

		Mockito.when(
			resourceBundle1.getLocale()
		).thenReturn(
			LocaleUtil.BRAZIL
		);

		ResourceBundle resourceBundle2 = Mockito.mock(ResourceBundle.class);

		Mockito.when(
			portal.getResourceBundle(Matchers.eq(LocaleUtil.US))
		).thenReturn(
			resourceBundle2
		);

		Mockito.when(
			resourceBundleLoader.loadResourceBundle(Matchers.eq(LocaleUtil.US))
		).thenReturn(
			resourceBundle2
		);

		Mockito.when(
			resourceBundle2.getLocale()
		).thenReturn(
			LocaleUtil.US
		);

		Map<String, String> optionLabelsForBRAZIL = HashMapBuilder.put(
			"address", "Endereço"
		).put(
			"city", "Cidade"
		).put(
			"country", "País"
		).put(
			"postal-code", "CEP"
		).put(
			"state", "Estado"
		).build();

		Map<String, String> optionLabelsForUS = HashMapBuilder.put(
			"address", "Address"
		).put(
			"city", "City"
		).put(
			"country", "Country"
		).put(
			"postal-code", "Postal Code"
		).put(
			"state", "State"
		).build();

		Mockito.when(
			_language.get(
				Matchers.any(ResourceBundle.class), Matchers.anyString())
		).then(
			(Answer<String>)invocationOnMock -> {
				Object[] arguments = invocationOnMock.getArguments();

				ResourceBundle resourceBundle = (ResourceBundle)arguments[0];

				if (resourceBundle == null) {
					return null;
				}

				if (Objects.equals(LocaleUtil.US, resourceBundle.getLocale())) {
					return optionLabelsForUS.get(arguments[1]);
				}

				if (Objects.equals(
						LocaleUtil.BRAZIL, resourceBundle.getLocale())) {

					return optionLabelsForBRAZIL.get(arguments[1]);
				}

				return null;
			}
		);
	}

	private final Language _language = Mockito.mock(Language.class);
	private final
		MultiLanguageOptionSelectDDMFormFieldTemplateContextContributor
			_multiLanguageOptionSelectDDMFormFieldTemplateContextContributor =
				new MultiLanguageOptionSelectDDMFormFieldTemplateContextContributor();

}