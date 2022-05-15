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

package com.liferay.portal.kernel.search;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Brian Wing Shun Chan
 * @author Bruno Farache
 */
public class DocumentImpl implements Document {

	public static boolean isSortableTextField(String name) {
		return _defaultSortableTextFields.contains(name);
	}

	@Override
	public void add(Field field) {
		_fields.put(field.getName(), field);
	}

	@Override
	public void addDate(String name, Date value) {
		if (value == null) {
			return;
		}

		addDate(name, new Date[] {value});
	}

	@Override
	public void addDate(String name, Date[] values) {
		if (values == null) {
			return;
		}

		String[] datesString = new String[values.length];
		Long[] datesTime = new Long[values.length];

		for (int i = 0; i < values.length; i++) {
			Format dateFormat = _getDateFormat();

			datesString[i] = dateFormat.format(values[i]);

			datesTime[i] = values[i].getTime();
		}

		createSortableNumericField(name, false, datesTime);

		Field field = createField(name, datesString);

		field.setDates(values);
	}

	@Override
	public void addDateSortable(String name, Date value) {
		if (value == null) {
			return;
		}

		addDateSortable(name, new Date[] {value});
	}

	@Override
	public void addDateSortable(String name, Date[] values) {
		if (values == null) {
			return;
		}

		String[] datesString = new String[values.length];
		Long[] datesTime = new Long[values.length];

		for (int i = 0; i < values.length; i++) {
			Format dateFormat = _getDateFormat();

			datesString[i] = dateFormat.format(values[i]);

			datesTime[i] = values[i].getTime();
		}

		createSortableNumericField(name, true, datesTime);

		addKeyword(name, datesString);
	}

	@Override
	public void addFile(String name, byte[] bytes, String fileExt) {
		InputStream inputStream = new UnsyncByteArrayInputStream(bytes);

		addFile(name, inputStream, fileExt);
	}

	@Override
	public void addFile(String name, File file, String fileExt)
		throws IOException {

		InputStream inputStream = new FileInputStream(file);

		addFile(name, inputStream, fileExt);
	}

	@Override
	public void addFile(String name, InputStream inputStream, String fileExt) {
		addText(name, FileUtil.extractText(inputStream));
	}

	@Override
	public void addFile(
		String name, InputStream inputStream, String fileExt,
		int maxStringLength) {

		addText(name, FileUtil.extractText(inputStream, maxStringLength));
	}

	@Override
	public void addGeoLocation(double latitude, double longitude) {
		addGeoLocation(Field.GEO_LOCATION, latitude, longitude);
	}

	@Override
	public void addGeoLocation(String name, double latitude, double longitude) {
		Field field = new Field(name);

		field.setGeoLocationPoint(new GeoLocationPoint(latitude, longitude));

		add(field);
	}

	@Override
	public void addKeyword(String name, boolean value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, Boolean value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, boolean[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, Boolean[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, double value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, Double value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, double[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, Double[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, float value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, Float value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, float[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, Float[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, int value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, int[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, Integer value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, Integer[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, long value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, Long value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, long[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, Long[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, short value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, Short value) {
		addKeyword(name, String.valueOf(value));
	}

	@Override
	public void addKeyword(String name, short[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, Short[] values) {
		if (values == null) {
			return;
		}

		addKeyword(name, ArrayUtil.toStringArray(values));
	}

	@Override
	public void addKeyword(String name, String value) {
		addKeyword(name, value, false);
	}

	@Override
	public void addKeyword(String name, String value, boolean lowerCase) {
		if (Validator.isNull(value)) {
			return;
		}

		createKeywordField(name, value, lowerCase);

		createSortableKeywordField(name, value);
	}

	@Override
	public void addKeyword(String name, String[] values) {
		if (values == null) {
			return;
		}

		createField(name, values);
	}

	@Override
	public void addKeywordSortable(String name, Boolean value) {
		addKeywordSortable(name, String.valueOf(value));
	}

	@Override
	public void addKeywordSortable(String name, Boolean[] values) {
		if (values == null) {
			return;
		}

		String[] valuesString = ArrayUtil.toStringArray(values);

		createField(name, valuesString);

		_createSortableTextField(name, true, valuesString);
	}

	@Override
	public void addKeywordSortable(String name, String value) {
		if (Validator.isNull(value)) {
			return;
		}

		createKeywordField(name, value, false);

		_createSortableTextField(name, true, value);
	}

	@Override
	public void addKeywordSortable(String name, String[] values) {
		if (values == null) {
			return;
		}

		Stream<String> valuesStream = Arrays.stream(values);

		String[] filteredValues = valuesStream.filter(
			value -> Validator.isNotNull(value)
		).toArray(
			String[]::new
		);

		if (ArrayUtil.isEmpty(filteredValues)) {
			return;
		}

		createField(name, filteredValues);

		_createSortableTextField(name, true, filteredValues);
	}

	@Override
	public void addLocalizedKeyword(String name, Map<Locale, String> values) {
		addLocalizedKeyword(name, values, false);
	}

	@Override
	public void addLocalizedKeyword(
		String name, Map<Locale, String> values, boolean lowerCase) {

		if ((values == null) || values.isEmpty()) {
			return;
		}

		if (lowerCase) {
			Map<Locale, String> lowerCaseValues = new HashMap<>();

			for (Map.Entry<Locale, String> entry : values.entrySet()) {
				String value = GetterUtil.getString(entry.getValue());

				lowerCaseValues.put(
					entry.getKey(), StringUtil.toLowerCase(value));
			}

			values = lowerCaseValues;
		}

		createField(name, values);
	}

	@Override
	public void addLocalizedKeyword(
		String name, Map<Locale, String> values, boolean lowerCase,
		boolean sortable) {

		if ((values == null) || values.isEmpty()) {
			return;
		}

		if (lowerCase) {
			Map<Locale, String> lowerCaseValues = new HashMap<>();

			for (Map.Entry<Locale, String> entry : values.entrySet()) {
				String value = GetterUtil.getString(entry.getValue());

				lowerCaseValues.put(
					entry.getKey(), StringUtil.toLowerCase(value));
			}

			values = lowerCaseValues;
		}

		createField(name, values, sortable);
	}

	@Override
	public void addLocalizedText(String name, Map<Locale, String> values) {
		if ((values == null) || values.isEmpty()) {
			return;
		}

		Field field = createField(name, values);

		field.setTokenized(true);
	}

	@Override
	public void addNumber(String name, BigDecimal value) {
		createNumberField(name, value);
	}

	@Override
	public void addNumber(String name, BigDecimal[] values) {
		createNumberField(name, values);
	}

	@Override
	public void addNumber(String name, double value) {
		createNumberField(name, Double.valueOf(value));
	}

	@Override
	public void addNumber(String name, Double value) {
		createNumberField(name, value);
	}

	@Override
	public void addNumber(String name, double[] values) {
		if (values == null) {
			return;
		}

		createNumberField(name, ArrayUtil.toArray(values));
	}

	@Override
	public void addNumber(String name, Double[] values) {
		createNumberField(name, values);
	}

	@Override
	public void addNumber(String name, float value) {
		createNumberField(name, Float.valueOf(value));
	}

	@Override
	public void addNumber(String name, Float value) {
		createNumberField(name, value);
	}

	@Override
	public void addNumber(String name, float[] values) {
		if (values == null) {
			return;
		}

		createNumberField(name, ArrayUtil.toArray(values));
	}

	@Override
	public void addNumber(String name, Float[] values) {
		createNumberField(name, values);
	}

	@Override
	public void addNumber(String name, int value) {
		createNumberField(name, Integer.valueOf(value));
	}

	@Override
	public void addNumber(String name, int[] values) {
		if (values == null) {
			return;
		}

		createNumberField(name, ArrayUtil.toArray(values));
	}

	@Override
	public void addNumber(String name, Integer value) {
		createNumberField(name, value);
	}

	@Override
	public void addNumber(String name, Integer[] values) {
		createNumberField(name, values);
	}

	@Override
	public void addNumber(String name, long value) {
		createNumberField(name, Long.valueOf(value));
	}

	@Override
	public void addNumber(String name, Long value) {
		createNumberField(name, value);
	}

	@Override
	public void addNumber(String name, long[] values) {
		if (values == null) {
			return;
		}

		createNumberField(name, ArrayUtil.toArray(values));
	}

	@Override
	public void addNumber(String name, Long[] values) {
		createNumberField(name, values);
	}

	@Override
	public void addNumber(String name, String value) {
		createNumberField(name, Long.valueOf(value));
	}

	@Override
	public void addNumber(String name, String[] values) {
		if (values == null) {
			return;
		}

		Long[] longs = new Long[values.length];

		for (int i = 0; i < values.length; i++) {
			longs[i] = Long.valueOf(values[i]);
		}

		createNumberField(name, longs);
	}

	@Override
	public void addNumberSortable(String name, BigDecimal value) {
		createNumberFieldWithTypedSortable(name, value);
	}

	@Override
	public void addNumberSortable(String name, BigDecimal[] values) {
		createNumberFieldWithTypedSortable(name, values);
	}

	@Override
	public void addNumberSortable(String name, Double value) {
		createNumberFieldWithTypedSortable(name, value);
	}

	@Override
	public void addNumberSortable(String name, Double[] values) {
		createNumberFieldWithTypedSortable(name, values);
	}

	@Override
	public void addNumberSortable(String name, Float value) {
		createNumberFieldWithTypedSortable(name, value);
	}

	@Override
	public void addNumberSortable(String name, Float[] values) {
		createNumberFieldWithTypedSortable(name, values);
	}

	@Override
	public void addNumberSortable(String name, Integer value) {
		createNumberFieldWithTypedSortable(name, value);
	}

	@Override
	public void addNumberSortable(String name, Integer[] values) {
		createNumberFieldWithTypedSortable(name, values);
	}

	@Override
	public void addNumberSortable(String name, Long value) {
		createNumberFieldWithTypedSortable(name, value);
	}

	@Override
	public void addNumberSortable(String name, Long[] values) {
		createNumberFieldWithTypedSortable(name, values);
	}

	@Override
	public void addText(String name, String value) {
		if (Validator.isNull(value)) {
			return;
		}

		Field field = createField(name, value);

		field.setTokenized(true);

		createSortableKeywordField(name, value);
	}

	@Override
	public void addText(String name, String[] values) {
		if (values == null) {
			return;
		}

		Field field = createField(name, values);

		field.setTokenized(true);

		createSortableKeywordField(name, values);
	}

	@Override
	public void addTextSortable(String name, String value) {
		if (Validator.isNull(value)) {
			return;
		}

		Field field = createField(name, value);

		field.setTokenized(true);

		_createSortableTextField(name, true, value);
	}

	@Override
	public void addTextSortable(String name, String[] values) {
		if (values == null) {
			return;
		}

		Field field = createField(name, values);

		field.setTokenized(true);

		_createSortableTextField(name, true, values);
	}

	@Override
	public void addUID(String portletId, long field1) {
		addUID(portletId, String.valueOf(field1));
	}

	@Override
	public void addUID(String portletId, long field1, String field2) {
		addUID(portletId, String.valueOf(field1), field2);
	}

	@Override
	public void addUID(String portletId, Long field1) {
		addUID(portletId, field1.longValue());
	}

	@Override
	public void addUID(String portletId, Long field1, String field2) {
		addUID(portletId, field1.longValue(), field2);
	}

	@Override
	public void addUID(String portletId, String field1) {
		addUID(portletId, field1, null);
	}

	@Override
	public void addUID(String portletId, String field1, String field2) {
		addUID(portletId, field1, field2, null);
	}

	@Override
	public void addUID(
		String portletId, String field1, String field2, String field3) {

		addUID(portletId, field1, field2, field3, null);
	}

	@Override
	public void addUID(
		String portletId, String field1, String field2, String field3,
		String field4) {

		addKeyword(
			Field.UID, Field.getUID(portletId, field1, field2, field3, field4));
	}

	@Override
	public Object clone() {
		DocumentImpl documentImpl = new DocumentImpl();

		documentImpl.setSortableTextFields(_sortableTextFields);

		return documentImpl;
	}

	@Override
	public String get(Locale locale, String name) {
		if (locale == null) {
			return get(name);
		}

		Field field = getField(Field.getLocalizedName(locale, name));

		if (field == null) {
			field = getField(name);
		}

		if (field == null) {
			return StringPool.BLANK;
		}

		return field.getValue();
	}

	@Override
	public String get(Locale locale, String name, String defaultName) {
		if (locale == null) {
			return get(name, defaultName);
		}

		Field field = getField(Field.getLocalizedName(locale, name));

		if (field == null) {
			field = getField(Field.getLocalizedName(locale, defaultName));
		}

		if (field == null) {
			return StringPool.BLANK;
		}

		return field.getValue();
	}

	@Override
	public String get(String name) {
		Field field = getField(name);

		if (field == null) {
			return StringPool.BLANK;
		}

		return field.getValue();
	}

	@Override
	public String get(String name, String defaultName) {
		Field field = getField(name);

		if (field == null) {
			return get(defaultName);
		}

		return field.getValue();
	}

	@Override
	public Date getDate(String name) throws ParseException {
		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			_INDEX_DATE_FORMAT_PATTERN);

		return dateFormat.parse(get(name));
	}

	@Override
	public Field getField(String name) {
		return doGetField(name, false);
	}

	@Override
	public Map<String, Field> getFields() {
		return _fields;
	}

	@Override
	public String getPortletId() {
		String uid = getUID();

		int pos = uid.indexOf(_UID_PORTLET);

		return uid.substring(0, pos);
	}

	@Override
	public String getUID() {
		Field field = getField(Field.UID);

		if (field == null) {
			throw new RuntimeException("UID is not set");
		}

		return field.getValue();
	}

	@Override
	public String[] getValues(String name) {
		Field field = getField(name);

		if (field == null) {
			return new String[] {StringPool.BLANK};
		}

		return field.getValues();
	}

	@Override
	public boolean hasField(String name) {
		if (_fields.containsKey(name)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isDocumentSortableTextField(String name) {
		return _sortableTextFields.contains(name);
	}

	@Override
	public void remove(String name) {
		_fields.remove(name);
	}

	public void setFields(Map<String, Field> fields) {
		_fields = fields;
	}

	@Override
	public void setSortableTextFields(String[] sortableTextFields) {
		_sortableTextFields = SetUtil.fromArray(sortableTextFields);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5 * _fields.size());

		toString(sb, _fields.values());

		return sb.toString();
	}

	protected Field createField(String name) {
		return doGetField(name, true);
	}

	protected Field createField(
		String name, boolean sortable, String... values) {

		Field field = createField(name);

		field.setSortable(sortable);
		field.setValues(values);

		return field;
	}

	protected Field createField(
		String name, Map<Locale, String> localizedValues) {

		return createField(name, localizedValues, false);
	}

	protected Field createField(
		String name, Map<Locale, String> localizedValues, boolean sortable) {

		Field field = createField(name);

		field.setLocalizedValues(localizedValues);
		field.setSortable(sortable);

		return field;
	}

	protected Field createField(String name, String... values) {
		return createField(name, false, values);
	}

	protected void createKeywordField(
		String name, String value, boolean lowerCase) {

		if (lowerCase && Validator.isNotNull(value)) {
			value = StringUtil.toLowerCase(value);
		}

		createField(name, value);
	}

	protected void createNumberField(
		String name, boolean typify, Number value) {

		if (value == null) {
			return;
		}

		String valueString = String.valueOf(value);

		createSortableNumericField(name, typify, valueString, value.getClass());

		createField(name, valueString);
	}

	protected <T extends Number & Comparable<? super T>> void createNumberField(
		String name, boolean typify, T... values) {

		if (values == null) {
			return;
		}

		createSortableNumericField(name, typify, values);

		createField(name, ArrayUtil.toStringArray(values));
	}

	protected void createNumberField(String name, Number value) {
		createNumberField(name, false, value);
	}

	protected <T extends Number & Comparable<? super T>> void createNumberField(
		String name, T... values) {

		createNumberField(name, false, values);
	}

	protected void createNumberFieldWithTypedSortable(
		String name, Number value) {

		createNumberField(name, true, value);
	}

	protected <T extends Number & Comparable<? super T>> void
		createNumberFieldWithTypedSortable(String name, T... values) {

		createNumberField(name, true, values);
	}

	protected void createSortableKeywordField(String name, String value) {
		if (isDocumentSortableTextField(name)) {
			_createSortableTextField(name, false, value);
		}
	}

	protected void createSortableKeywordField(String name, String[] values) {
		if (isDocumentSortableTextField(name)) {
			_createSortableTextField(name, false, values);
		}
	}

	protected void createSortableNumericField(
		String name, boolean typify, String value,
		Class<? extends Number> clazz) {

		if (typify) {
			name = name + "_Number";
		}

		Field field = createField(Field.getSortableFieldName(name), value);

		field.setNumeric(true);
		field.setNumericClass(clazz);
	}

	protected <T extends Number & Comparable<? super T>> void
		createSortableNumericField(String name, boolean typify, T... values) {

		if ((values == null) || (values.length == 0)) {
			return;
		}

		T minValue = Collections.min(Arrays.asList(values));

		createSortableNumericField(
			name, typify, String.valueOf(minValue), minValue.getClass());
	}

	protected void createSortableTextField(String name, String value) {
		_createSortableTextField(name, false, value);
	}

	protected void createSortableTextField(String name, String[] values) {
		_createSortableTextField(name, false, values);
	}

	protected Field doGetField(String name, boolean createIfNew) {
		Field field = _fields.get(name);

		if ((field == null) && createIfNew) {
			field = new Field(name);

			_fields.put(name, field);
		}

		return field;
	}

	protected void setSortableTextFields(Set<String> sortableTextFields) {
		_sortableTextFields = sortableTextFields;
	}

	protected void toString(StringBundler sb, Collection<Field> fields) {
		sb.append(StringPool.OPEN_CURLY_BRACE);

		boolean firstField = true;

		for (Field field : fields) {
			if (!firstField) {
				sb.append(StringPool.COMMA);
				sb.append(StringPool.SPACE);
			}
			else {
				firstField = false;
			}

			if (field.hasChildren()) {
				sb.append(field.getName());
				sb.append(StringPool.COLON);

				toString(sb, field.getFields());
			}
			else {
				sb.append(field.getName());
				sb.append(StringPool.EQUAL);
				sb.append(Arrays.toString(field.getValues()));
			}
		}

		sb.append(StringPool.CLOSE_CURLY_BRACE);
	}

	private void _createSortableTextField(
		String name, boolean typify, String value) {

		if (typify) {
			name = name + "_String";
		}

		String truncatedValue = value;

		if (value.length() > _SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH) {
			truncatedValue = value.substring(
				0, _SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH);
		}

		createKeywordField(
			Field.getSortableFieldName(name), truncatedValue, true);
	}

	private void _createSortableTextField(
		String name, boolean typify, String[] values) {

		if (values.length == 0) {
			return;
		}

		_createSortableTextField(
			name, typify, Collections.min(Arrays.<String>asList(values)));
	}

	private Format _getDateFormat() {
		if (_dateFormat == null) {
			_dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
				_INDEX_DATE_FORMAT_PATTERN);
		}

		return _dateFormat;
	}

	private static final String _INDEX_DATE_FORMAT_PATTERN = PropsUtil.get(
		PropsKeys.INDEX_DATE_FORMAT_PATTERN);

	private static final int _SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH =
		GetterUtil.getInteger(
			PropsUtil.get(
				PropsKeys.INDEX_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH));

	private static final String _UID_PORTLET = "_PORTLET_";

	private static Format _dateFormat;
	private static final Set<String> _defaultSortableTextFields =
		SetUtil.fromArray(
			PropsUtil.getArray(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS));

	private Map<String, Field> _fields = new HashMap<>();
	private Set<String> _sortableTextFields = _defaultSortableTextFields;

}