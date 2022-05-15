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

package com.liferay.frontend.taglib.clay.servlet.taglib;

import com.liferay.frontend.taglib.clay.internal.servlet.taglib.BaseContainerTag;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.TagResourceBundleUtil;

import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * @author Kresimir Coko
 */
public class CheckboxTag extends BaseContainerTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		if (Validator.isNotNull(getLabel())) {
			setDynamicAttribute(StringPool.BLANK, "aria-label", getLabel());
		}

		return super.doStartTag();
	}

	public String getLabel() {
		return LanguageUtil.get(
			TagResourceBundleUtil.getResourceBundle(pageContext), _label);
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	public boolean isChecked() {
		return _checked;
	}

	public boolean isDisabled() {
		return _disabled;
	}

	public boolean isIndeterminate() {
		return _indeterminate;
	}

	public boolean isInline() {
		return _inline;
	}

	public void setChecked(boolean checked) {
		_checked = checked;
	}

	public void setDisabled(boolean disabled) {
		_disabled = disabled;
	}

	public void setIndeterminate(boolean indeterminate) {
		_indeterminate = indeterminate;
	}

	public void setInline(boolean inline) {
		_inline = inline;
	}

	public void setLabel(String label) {
		_label = label;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setValue(String value) {
		_value = value;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_checked = false;
		_disabled = false;
		_indeterminate = false;
		_inline = false;
		_label = null;
		_name = null;
		_value = null;
	}

	@Override
	protected String getHydratedModuleName() {
		return "frontend-taglib-clay/Checkbox";
	}

	@Override
	protected Map<String, Object> prepareProps(Map<String, Object> props) {
		props.put("checked", _checked);
		props.put("disabled", _disabled);
		props.put("indeterminate", _indeterminate);
		props.put("inline", _inline);

		if (Validator.isNotNull(getLabel())) {
			props.put("label", getLabel());
		}

		props.put("name", _name);

		if (Validator.isNotNull(_value)) {
			props.put("value", _value);
		}

		return super.prepareProps(props);
	}

	@Override
	protected String processCssClasses(Set<String> cssClasses) {
		cssClasses.add("custom-control-input");

		return super.processCssClasses(cssClasses);
	}

	@Override
	protected int processStartTag() throws Exception {
		super.processStartTag();

		JspWriter jspWriter = pageContext.getOut();

		jspWriter.write("<label><input ");

		super.writeDynamicAttributes();

		if (_checked) {
			jspWriter.write(" checked");
		}

		super.writeCssClassAttribute();

		if (_disabled) {
			jspWriter.write(" disabled");
		}

		if (Validator.isNotNull(getId())) {
			super.writeIdAttribute();
		}

		if (Validator.isNotNull(_name)) {
			jspWriter.write(" name=\"");
			jspWriter.write(_name);
			jspWriter.write("\"");
		}

		jspWriter.write(" type=\"checkbox\"");

		if (Validator.isNotNull(_value)) {
			jspWriter.write(" value=\"");
			jspWriter.write(_value);
			jspWriter.write("\"");
		}

		jspWriter.write(" /><span class=\"custom-control-label\">");

		if (Validator.isNotNull(_label)) {
			jspWriter.write("<span class=\"custom-control-label-text\">");
			jspWriter.write(_label);
			jspWriter.write("</span>");
		}

		jspWriter.write("</span></label>");

		return SKIP_BODY;
	}

	@Override
	protected void writeCssClassAttribute() throws Exception {
		JspWriter jspWriter = pageContext.getOut();

		jspWriter.write(" class=\"custom-checkbox custom-control");

		if (_inline) {
			jspWriter.write(" custom-control-inline");
		}

		jspWriter.write("\"");
	}

	@Override
	protected void writeDynamicAttributes() {
	}

	@Override
	protected void writeIdAttribute() {
	}

	private static final String _ATTRIBUTE_NAMESPACE = "clay:checkbox:";

	private boolean _checked;
	private boolean _disabled;
	private boolean _indeterminate;
	private boolean _inline;
	private String _label;
	private String _name;
	private String _value;

}