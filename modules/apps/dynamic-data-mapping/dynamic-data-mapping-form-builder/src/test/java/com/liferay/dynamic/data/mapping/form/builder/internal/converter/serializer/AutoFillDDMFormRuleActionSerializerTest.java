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

package com.liferay.dynamic.data.mapping.form.builder.internal.converter.serializer;

import com.liferay.dynamic.data.mapping.form.builder.internal.converter.model.action.AutoFillDDMFormRuleAction;
import com.liferay.dynamic.data.mapping.spi.converter.serializer.SPIDDMFormRuleSerializerContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Leonardo Barros
 */
public class AutoFillDDMFormRuleActionSerializerTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testSerialize() {
		_mockGetDDMDataProviderInstanceUUID();

		_mockGetInputParametersMapper("field1");

		_mockGetOutputParametersMapper("field2", "field3");

		AutoFillDDMFormRuleActionSerializer
			autoFillDDMFormRuleActionSerializer =
				new AutoFillDDMFormRuleActionSerializer(
					_autoFillDDMFormRuleAction);

		String result = autoFillDDMFormRuleActionSerializer.serialize(
			_spiDDMFormRuleSerializerContext);

		Assert.assertEquals(
			"call('0', 'key1=field1', 'field2=key1;field3=key2')", result);
	}

	@Test
	public void testSerializeWithEmptyInputParameter() {
		_mockGetInputParametersMapper(StringPool.BLANK);

		_mockGetOutputParametersMapper("field2", "field3");

		AutoFillDDMFormRuleActionSerializer
			autoFillDDMFormRuleActionSerializer =
				new AutoFillDDMFormRuleActionSerializer(
					_autoFillDDMFormRuleAction);

		String result = autoFillDDMFormRuleActionSerializer.serialize(
			_spiDDMFormRuleSerializerContext);

		Assert.assertNull(result);
	}

	@Test
	public void testSerializeWithEmptyOutputParameter() {
		_mockGetInputParametersMapper("field1");

		_mockGetOutputParametersMapper("field2", StringPool.BLANK);

		AutoFillDDMFormRuleActionSerializer
			autoFillDDMFormRuleActionSerializer =
				new AutoFillDDMFormRuleActionSerializer(
					_autoFillDDMFormRuleAction);

		String result = autoFillDDMFormRuleActionSerializer.serialize(
			_spiDDMFormRuleSerializerContext);

		Assert.assertNull(result);
	}

	private void _mockGetDDMDataProviderInstanceUUID() {
		Mockito.when(
			_autoFillDDMFormRuleAction.getDDMDataProviderInstanceUUID()
		).thenReturn(
			"0"
		);
	}

	private void _mockGetInputParametersMapper(String fieldName) {
		Mockito.when(
			_autoFillDDMFormRuleAction.getInputParametersMapper()
		).thenReturn(
			HashMapBuilder.put(
				"key1", fieldName
			).build()
		);
	}

	private void _mockGetOutputParametersMapper(
		String fieldName1, String fieldName2) {

		Mockito.when(
			_autoFillDDMFormRuleAction.getOutputParametersMapper()
		).thenReturn(
			HashMapBuilder.put(
				"key1", fieldName1
			).put(
				"key2", fieldName2
			).build()
		);
	}

	private final AutoFillDDMFormRuleAction _autoFillDDMFormRuleAction =
		Mockito.mock(AutoFillDDMFormRuleAction.class);
	private final SPIDDMFormRuleSerializerContext
		_spiDDMFormRuleSerializerContext = Mockito.mock(
			SPIDDMFormRuleSerializerContext.class);

}