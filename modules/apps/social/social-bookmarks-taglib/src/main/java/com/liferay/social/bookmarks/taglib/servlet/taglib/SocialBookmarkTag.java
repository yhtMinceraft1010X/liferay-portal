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

package com.liferay.social.bookmarks.taglib.servlet.taglib;

import com.liferay.social.bookmarks.SocialBookmark;
import com.liferay.social.bookmarks.taglib.internal.servlet.ServletContextUtil;
import com.liferay.social.bookmarks.taglib.internal.util.SocialBookmarksRegistryUtil;
import com.liferay.taglib.servlet.PipingServletResponseFactory;
import com.liferay.taglib.util.AttributesTagSupport;

import java.io.IOException;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author David Truong
 * @author Jorge Ferrer
 * @author Brian Wing Shun Chan
 */
public class SocialBookmarkTag extends AttributesTagSupport {

	@Override
	public int doEndTag() throws JspException {
		try {
			SocialBookmark socialBookmark = _getSocialBookmark();

			if (socialBookmark != null) {
				HttpServletRequest httpServletRequest = getRequest();

				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:additionalProps",
					_additionalProps);
				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:displayStyle",
					_displayStyle);
				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:socialBookmark",
					_getSocialBookmark());
				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:target", _target);
				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:title", _title);
				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:type", _type);
				httpServletRequest.setAttribute(
					"liferay-social-bookmarks:bookmark:url", _url);

				socialBookmark.render(
					_target, _title, _url, httpServletRequest,
					PipingServletResponseFactory.createPipingServletResponse(
						pageContext));
			}

			return EVAL_PAGE;
		}
		catch (IOException | ServletException exception) {
			throw new JspException(exception);
		}
	}

	public void setAdditionalProps(Map<String, Object> additionalProps) {
		_additionalProps = additionalProps;
	}

	public void setDisplayStyle(String displayStyle) {
		_displayStyle = displayStyle;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	public void setTarget(String target) {
		_target = target;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setUrl(String url) {
		_url = url;
	}

	private SocialBookmark _getSocialBookmark() {
		return SocialBookmarksRegistryUtil.getSocialBookmark(_type);
	}

	private Map<String, Object> _additionalProps;
	private String _displayStyle;
	private String _target;
	private String _title;
	private String _type;
	private String _url;

}