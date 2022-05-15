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

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Eduardo Lundgren
 */
public class LocaleUtil {

	public static final Locale BRAZIL = new Locale("pt", "BR");

	public static final Locale CANADA = Locale.CANADA;

	public static final Locale CANADA_FRENCH = Locale.CANADA_FRENCH;

	public static final Locale CHINA = Locale.CHINA;

	public static final Locale CHINESE = Locale.CHINESE;

	public static final Locale ENGLISH = Locale.ENGLISH;

	public static final Locale FRANCE = Locale.FRANCE;

	public static final Locale FRENCH = Locale.FRENCH;

	public static final Locale GERMAN = Locale.GERMAN;

	public static final Locale GERMANY = Locale.GERMANY;

	public static final Locale HUNGARY = new Locale("hu", "HU");

	public static final Locale ITALIAN = Locale.ITALIAN;

	public static final Locale ITALY = Locale.ITALY;

	public static final Locale JAPAN = Locale.JAPAN;

	public static final Locale JAPANESE = Locale.JAPANESE;

	public static final Locale KOREA = Locale.KOREA;

	public static final Locale KOREAN = Locale.KOREAN;

	public static final Locale NETHERLANDS = new Locale("nl", "NL");

	public static final Locale PORTUGAL = new Locale("pt", "PT");

	public static final Locale PRC = Locale.PRC;

	public static final Locale ROOT = Locale.ROOT;

	public static final Locale SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE;

	public static final Locale SPAIN = new Locale("es", "ES");

	public static final Locale TAIWAN = Locale.TAIWAN;

	public static final Locale TRADITIONAL_CHINESE = Locale.TRADITIONAL_CHINESE;

	public static final Locale UK = Locale.UK;

	public static final Locale US = Locale.US;

	public static boolean equals(Locale locale1, Locale locale2) {
		return _localeUtil._equals(locale1, locale2);
	}

	public static Locale fromLanguageId(String languageId) {
		return _localeUtil._fromLanguageId(languageId, true);
	}

	public static Locale fromLanguageId(String languageId, boolean validate) {
		return _localeUtil._fromLanguageId(languageId, validate);
	}

	public static Locale fromLanguageId(
		String languageId, boolean validate, boolean useDefault) {

		return _localeUtil._fromLanguageId(languageId, validate, useDefault);
	}

	public static Locale[] fromLanguageIds(List<String> languageIds) {
		return _localeUtil._fromLanguageIds(languageIds);
	}

	public static Locale[] fromLanguageIds(String[] languageIds) {
		return _localeUtil._fromLanguageIds(languageIds);
	}

	public static Locale getDefault() {
		return _localeUtil._getDefault();
	}

	public static LocaleUtil getInstance() {
		return _localeUtil;
	}

	public static Map<String, String> getISOLanguages(Locale locale) {
		return _localeUtil._getISOLanguages(locale);
	}

	public static String getLocaleDisplayName(
		Locale displayLocale, Locale locale) {

		return _localeUtil._getLocaleDisplayName(displayLocale, locale);
	}

	public static String getLongDisplayName(
		Locale locale, Set<String> duplicateLanguages) {

		return _localeUtil._getLongDisplayName(locale, duplicateLanguages);
	}

	public static Locale getMostRelevantLocale() {
		return _localeUtil._getMostRelevantLocale();
	}

	public static String getShortDisplayName(
		Locale locale, Set<String> duplicateLanguages) {

		return _localeUtil._getShortDisplayName(locale, duplicateLanguages);
	}

	public static Locale getSiteDefault() {
		return _localeUtil._getSiteDefault();
	}

	public static void setDefault(
		String userLanguage, String userCountry, String userVariant) {

		_localeUtil._setDefault(userLanguage, userCountry, userVariant);
	}

	public static String toBCP47LanguageId(Locale locale) {
		return _localeUtil._toBCP47LanguageId(locale);
	}

	public static String toBCP47LanguageId(String languageId) {
		return _localeUtil._toBCP47LanguageId(languageId);
	}

	public static String[] toBCP47LanguageIds(Locale[] locales) {
		return _localeUtil._toBCP47LanguageIds(locales);
	}

	public static String[] toBCP47LanguageIds(String[] languageIds) {
		return _localeUtil._toBCP47LanguageIds(languageIds);
	}

	public static String[] toDisplayNames(
		Collection<Locale> locales, Locale locale) {

		return _localeUtil._toDisplayNames(locales, locale);
	}

	public static String toLanguageId(Locale locale) {
		return _localeUtil._toLanguageId(locale);
	}

	public static String[] toLanguageIds(Collection<Locale> locales) {
		return _localeUtil._toLanguageIds(locales);
	}

	public static String[] toLanguageIds(Locale[] locales) {
		return _localeUtil._toLanguageIds(locales);
	}

	public static String toW3cLanguageId(Locale locale) {
		return _localeUtil._toW3cLanguageId(locale);
	}

	public static String toW3cLanguageId(String languageId) {
		return _localeUtil._toW3cLanguageId(languageId);
	}

	public static String[] toW3cLanguageIds(Locale[] locales) {
		return _localeUtil._toW3cLanguageIds(locales);
	}

	public static String[] toW3cLanguageIds(String[] languageIds) {
		return _localeUtil._toW3cLanguageIds(languageIds);
	}

	private LocaleUtil() {
		_locale = new Locale("en", "US");
	}

	private boolean _equals(Locale locale1, Locale locale2) {
		String languageId1 = _toLanguageId(locale1);
		String languageId2 = _toLanguageId(locale2);

		return StringUtil.equalsIgnoreCase(languageId1, languageId2);
	}

	private Locale _fromLanguageId(String languageId, boolean validate) {
		return _fromLanguageId(languageId, validate, true);
	}

	private Locale _fromLanguageId(
		String languageId, boolean validate, boolean useDefault) {

		if (languageId == null) {
			if (useDefault) {
				return _getDefault();
			}

			return null;
		}

		Locale locale = _locales.get(languageId);

		if (locale != null) {
			return locale;
		}

		if (languageId.equals("zh-Hans-CN")) {
			languageId = "zh_CN";
		}
		else if (languageId.equals("zh-Hant-TW")) {
			languageId = "zh_TW";
		}
		else {
			languageId = StringUtil.replace(
				languageId, CharPool.MINUS, CharPool.UNDERLINE);
		}

		int pos = languageId.indexOf(CharPool.UNDERLINE);

		if (pos == -1) {
			locale = new Locale(languageId);
		}
		else {
			String[] languageIdParts = StringUtil.split(
				languageId, CharPool.UNDERLINE);

			String languageCode = languageIdParts[0];
			String countryCode = languageIdParts[1];

			String variant = null;

			if (languageIdParts.length > 2) {
				variant = languageIdParts[2];
			}

			if (Validator.isNotNull(variant)) {
				locale = new Locale(languageCode, countryCode, variant);
			}
			else {
				locale = new Locale(languageCode, countryCode);
			}
		}

		if (validate && !LanguageUtil.isAvailableLocale(locale)) {
			locale = null;

			if (_log.isWarnEnabled()) {
				_log.warn(languageId + " is not a valid language id");
			}
		}
		else {
			_locales.put(languageId, locale);
		}

		if ((locale == null) && useDefault) {
			locale = _locale;
		}

		return locale;
	}

	private Locale[] _fromLanguageIds(List<String> languageIds) {
		Locale[] locales = new Locale[languageIds.size()];

		for (int i = 0; i < languageIds.size(); i++) {
			locales[i] = _fromLanguageId(languageIds.get(i), true);
		}

		return locales;
	}

	private Locale[] _fromLanguageIds(String[] languageIds) {
		Locale[] locales = new Locale[languageIds.length];

		for (int i = 0; i < languageIds.length; i++) {
			locales[i] = _fromLanguageId(languageIds[i], true);
		}

		return locales;
	}

	private Locale _getDefault() {
		Locale locale = LocaleThreadLocal.getDefaultLocale();

		if (locale != null) {
			return locale;
		}

		return _locale;
	}

	private String _getDisplayCountry(Locale displayLocale, Locale locale) {
		String country = displayLocale.getDisplayCountry(locale);
		String variant = displayLocale.getDisplayVariant(locale);

		if (Validator.isNull(variant)) {
			return country;
		}

		return StringUtil.merge(
			new String[] {country, variant}, StringPool.COMMA_AND_SPACE);
	}

	private String _getDisplayName(
		String language, String country, Locale locale,
		Set<String> duplicateLanguages) {

		String displayName = null;

		if (duplicateLanguages.contains(locale.getLanguage()) &&
			Validator.isNotNull(country)) {

			displayName = StringUtil.appendParentheticalSuffix(
				language, country);
		}
		else {
			displayName = language;
		}

		if (LanguageUtil.isBetaLocale(locale)) {
			displayName = displayName.concat(_BETA_SUFFIX);
		}

		return displayName;
	}

	private Map<String, String> _getISOLanguages(Locale locale) {
		Map<String, String> isoLanguages = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

		for (String isoLanguageId : Locale.getISOLanguages()) {
			Locale isoLocale = _fromLanguageId(isoLanguageId, true);

			isoLanguages.put(
				isoLocale.getDisplayLanguage(locale), isoLanguageId);
		}

		return isoLanguages;
	}

	private String _getLocaleDisplayName(Locale displayLocale, Locale locale) {
		String key = "language." + displayLocale.getLanguage();

		String displayName = LanguageUtil.get(locale, key);

		if (displayName.equals(key)) {
			return displayLocale.getDisplayName(locale);
		}

		String country = _getDisplayCountry(displayLocale, locale);

		if (Validator.isNull(country)) {
			return displayName;
		}

		return StringBundler.concat(
			displayName, StringPool.SPACE, StringPool.OPEN_PARENTHESIS, country,
			StringPool.CLOSE_PARENTHESIS);
	}

	private String _getLongDisplayName(
		Locale locale, Set<String> duplicateLanguages) {

		return _getDisplayName(
			locale.getDisplayLanguage(locale),
			_getDisplayCountry(locale, locale), locale, duplicateLanguages);
	}

	private Locale _getMostRelevantLocale() {
		Locale locale = LocaleThreadLocal.getThemeDisplayLocale();

		if (locale == null) {
			locale = _getDefault();
		}

		return locale;
	}

	private String _getShortDisplayName(
		Locale locale, Set<String> duplicateLanguages) {

		String language = locale.getDisplayLanguage(locale);

		if (language.length() > 3) {
			language = locale.getLanguage();
			language = StringUtil.toUpperCase(language);
		}

		return _getDisplayName(
			language, StringUtil.toUpperCase(locale.getCountry()), locale,
			duplicateLanguages);
	}

	private Locale _getSiteDefault() {
		Locale locale = LocaleThreadLocal.getSiteDefaultLocale();

		if (locale != null) {
			return locale;
		}

		return _getDefault();
	}

	private void _setDefault(
		String userLanguage, String userCountry, String userVariant) {

		if (Validator.isNotNull(userLanguage) &&
			Validator.isNull(userCountry) && Validator.isNull(userVariant)) {

			_locale = new Locale(userLanguage);
		}
		else if (Validator.isNotNull(userLanguage) &&
				 Validator.isNotNull(userCountry) &&
				 Validator.isNull(userVariant)) {

			_locale = new Locale(userLanguage, userCountry);
		}
		else if (Validator.isNotNull(userLanguage) &&
				 Validator.isNotNull(userCountry) &&
				 Validator.isNotNull(userVariant)) {

			_locale = new Locale(userLanguage, userCountry, userVariant);
		}

		LocaleThreadLocal.setDefaultLocale(_locale);
	}

	private String _toBCP47LanguageId(Locale locale) {
		return _toBCP47LanguageId(_toLanguageId(locale));
	}

	private String _toBCP47LanguageId(String languageId) {
		if (languageId.equals("zh_CN")) {
			return "zh-Hans-CN";
		}
		else if (languageId.equals("zh_TW")) {
			return "zh-Hant-TW";
		}

		return StringUtil.replace(
			languageId, CharPool.UNDERLINE, CharPool.MINUS);
	}

	private String[] _toBCP47LanguageIds(Locale[] locales) {
		return _toBCP47LanguageIds(_toLanguageIds(locales));
	}

	private String[] _toBCP47LanguageIds(String[] languageIds) {
		String[] bcp47LanguageIds = new String[languageIds.length];

		for (int i = 0; i < languageIds.length; i++) {
			bcp47LanguageIds[i] = _toBCP47LanguageId(languageIds[i]);
		}

		return bcp47LanguageIds;
	}

	private String[] _toDisplayNames(
		Collection<Locale> locales, Locale locale) {

		String[] displayNames = new String[locales.size()];

		int i = 0;

		for (Locale curLocale : locales) {
			displayNames[i++] = curLocale.getDisplayName(locale);
		}

		return displayNames;
	}

	private String _toLanguageId(Locale locale) {
		if (locale == null) {
			locale = _locale;
		}

		String country = locale.getCountry();

		boolean hasCountry = false;

		if (country.length() != 0) {
			hasCountry = true;
		}

		String variant = locale.getVariant();

		boolean hasVariant = false;

		if (variant.length() != 0) {
			hasVariant = true;
		}

		if (!hasCountry && !hasVariant) {
			return locale.getLanguage();
		}

		int length = 3;

		if (hasCountry && hasVariant) {
			length = 5;
		}

		StringBundler sb = new StringBundler(length);

		sb.append(locale.getLanguage());

		if (hasCountry) {
			sb.append(StringPool.UNDERLINE);
			sb.append(country);
		}

		if (hasVariant) {
			sb.append(StringPool.UNDERLINE);
			sb.append(variant);
		}

		return sb.toString();
	}

	private String[] _toLanguageIds(Collection<Locale> locales) {
		String[] languageIds = new String[locales.size()];

		int i = 0;

		for (Locale locale : locales) {
			languageIds[i++] = _toLanguageId(locale);
		}

		return languageIds;
	}

	private String[] _toLanguageIds(Locale[] locales) {
		String[] languageIds = new String[locales.length];

		for (int i = 0; i < locales.length; i++) {
			languageIds[i] = _toLanguageId(locales[i]);
		}

		return languageIds;
	}

	private String _toW3cLanguageId(Locale locale) {
		return _toW3cLanguageId(_toLanguageId(locale));
	}

	private String _toW3cLanguageId(String languageId) {
		return StringUtil.replace(
			languageId, CharPool.UNDERLINE, CharPool.MINUS);
	}

	private String[] _toW3cLanguageIds(Locale[] locales) {
		return _toW3cLanguageIds(_toLanguageIds(locales));
	}

	private String[] _toW3cLanguageIds(String[] languageIds) {
		String[] w3cLanguageIds = new String[languageIds.length];

		for (int i = 0; i < languageIds.length; i++) {
			w3cLanguageIds[i] = _toW3cLanguageId(languageIds[i]);
		}

		return w3cLanguageIds;
	}

	private static final String _BETA_SUFFIX = " [Beta]";

	private static final Log _log = LogFactoryUtil.getLog(LocaleUtil.class);

	private static final LocaleUtil _localeUtil = new LocaleUtil();

	private Locale _locale;
	private final Map<String, Locale> _locales = new HashMap<>();

}