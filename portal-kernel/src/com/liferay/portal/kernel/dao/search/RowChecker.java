/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.kernel.dao.search;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.RenderResponse;

/**
 * <a href="RowChecker.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 */
public class RowChecker {

	public static final String ALIGN = "left";

	public static final String VALIGN = "middle";

	public static final int COLSPAN = 1;

	public static final String FORM_NAME = "fm";

	public static final String ALL_ROW_IDS = "allRowIds";

	public static final String ROW_IDS = "rowIds";

	public RowChecker(RenderResponse renderResponse) {
		this(
			renderResponse, ALIGN, VALIGN, COLSPAN, FORM_NAME, ALL_ROW_IDS,
			ROW_IDS);
	}

	public RowChecker(
		RenderResponse renderResponse, String align, String valign,
		String formName, String allRowsId, String rowId) {

		this(
			renderResponse, align, valign, COLSPAN, formName, allRowsId, rowId);
	}

	public RowChecker(
		RenderResponse renderResponse, String align, String valign, int colspan,
		String formName, String allRowsId, String rowId) {

		_align = align;
		_valign = valign;
		_colspan = colspan;
		_formName = renderResponse.getNamespace() + formName;

		if (Validator.isNotNull(allRowsId)) {
			_allRowsId = renderResponse.getNamespace() + allRowsId;
		}

		_rowId = renderResponse.getNamespace() + rowId;
	}

	public String getAlign() {
		return _align;
	}

	public String getValign() {
		return _valign;
	}

	public int getColspan() {
		return _colspan;
	}

	public String getFormName() {
		return _formName;
	}

	public String getAllRowsId() {
		return _allRowsId;
	}

	public String getRowId() {
		return _rowId;
	}

	public String getAllRowsCheckBox() {
		if (Validator.isNull(_allRowsId)) {
			return StringPool.BLANK;
		}
		else {
			StringBuilder sb = new StringBuilder();

			sb.append("<input name=\"");
			sb.append(_allRowsId);
			sb.append("\" type=\"checkbox\" ");
			sb.append("onClick=\"Liferay.Util.checkAll(");
			sb.append("AUI().one(this).ancestor('table.taglib-search-iterator'), ");
			sb.append("'");
			sb.append(_rowId);
			sb.append("', this");
			sb.append(");\">");

			return sb.toString();
		}
	}

	public String getRowCheckBox(boolean checked, String primaryKey) {
		StringBuilder sb = new StringBuilder();

		sb.append("<input ");

		if (checked) {
			sb.append("checked ");
		}

		sb.append("name=\"");
		sb.append(_rowId);
		sb.append("\" type=\"checkbox\" value=\"");
		sb.append(primaryKey);
		sb.append("\" ");

		if (Validator.isNotNull(_allRowsId)) {
			sb.append("onClick=\"Liferay.Util.checkAllBox(");
			sb.append("AUI().one(this).ancestor('table.taglib-search-iterator'), ");
			sb.append("'");
			sb.append(_rowId);
			sb.append("', ");
			sb.append(_allRowsId);
			sb.append(");\"");
		}

		sb.append(">");

		return sb.toString();
	}

	public boolean isChecked(Object obj) {
		return false;
	}

	private String _align;
	private String _valign;
	private int _colspan;
	private String _formName;
	private String _allRowsId;
	private String _rowId;

}