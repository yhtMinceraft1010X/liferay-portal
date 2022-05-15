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

package com.liferay.headless.delivery.resource.v1_0;

import com.liferay.headless.delivery.dto.v1_0.Comment;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.annotation.versioning.ProviderType;

/**
 * To access this resource, run:
 *
 *     curl -u your@email.com:yourpassword -D - http://localhost:8080/o/headless-delivery/v1.0
 *
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@ProviderType
public interface CommentResource {

	public static Builder builder() {
		return FactoryHolder.factory.create();
	}

	public Page<Comment> getBlogPostingCommentsPage(
			Long blogPostingId, String search,
			com.liferay.portal.vulcan.aggregation.Aggregation aggregation,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception;

	public Comment postBlogPostingComment(Long blogPostingId, Comment comment)
		throws Exception;

	public Response postBlogPostingCommentBatch(
			Long blogPostingId, String callbackURL, Object object)
		throws Exception;

	public void deleteComment(Long commentId) throws Exception;

	public Response deleteCommentBatch(String callbackURL, Object object)
		throws Exception;

	public Comment getComment(Long commentId) throws Exception;

	public Comment putComment(Long commentId, Comment comment) throws Exception;

	public Response putCommentBatch(String callbackURL, Object object)
		throws Exception;

	public Page<Comment> getCommentCommentsPage(
			Long parentCommentId, String search,
			com.liferay.portal.vulcan.aggregation.Aggregation aggregation,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception;

	public Comment postCommentComment(Long parentCommentId, Comment comment)
		throws Exception;

	public Page<Comment> getDocumentCommentsPage(
			Long documentId, String search,
			com.liferay.portal.vulcan.aggregation.Aggregation aggregation,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception;

	public Comment postDocumentComment(Long documentId, Comment comment)
		throws Exception;

	public Response postDocumentCommentBatch(
			Long documentId, String callbackURL, Object object)
		throws Exception;

	public void
			deleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String blogPostingExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			getSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String blogPostingExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			putSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String blogPostingExternalReferenceCode,
				String externalReferenceCode, Comment comment)
		throws Exception;

	public void
			deleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String parentCommentExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			getSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String parentCommentExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			putSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String parentCommentExternalReferenceCode,
				String externalReferenceCode, Comment comment)
		throws Exception;

	public void
			deleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String documentExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			getSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String documentExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			putSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String documentExternalReferenceCode,
				String externalReferenceCode, Comment comment)
		throws Exception;

	public void
			deleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String structuredContentExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			getSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String structuredContentExternalReferenceCode,
				String externalReferenceCode)
		throws Exception;

	public Comment
			putSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
				Long siteId, String structuredContentExternalReferenceCode,
				String externalReferenceCode, Comment comment)
		throws Exception;

	public Page<Comment> getStructuredContentCommentsPage(
			Long structuredContentId, String search,
			com.liferay.portal.vulcan.aggregation.Aggregation aggregation,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception;

	public Comment postStructuredContentComment(
			Long structuredContentId, Comment comment)
		throws Exception;

	public Response postStructuredContentCommentBatch(
			Long structuredContentId, String callbackURL, Object object)
		throws Exception;

	public default void setContextAcceptLanguage(
		AcceptLanguage contextAcceptLanguage) {
	}

	public void setContextCompany(
		com.liferay.portal.kernel.model.Company contextCompany);

	public default void setContextHttpServletRequest(
		HttpServletRequest contextHttpServletRequest) {
	}

	public default void setContextHttpServletResponse(
		HttpServletResponse contextHttpServletResponse) {
	}

	public default void setContextUriInfo(UriInfo contextUriInfo) {
	}

	public void setContextUser(
		com.liferay.portal.kernel.model.User contextUser);

	public void setExpressionConvert(
		ExpressionConvert<Filter> expressionConvert);

	public void setFilterParserProvider(
		FilterParserProvider filterParserProvider);

	public void setGroupLocalService(GroupLocalService groupLocalService);

	public void setResourceActionLocalService(
		ResourceActionLocalService resourceActionLocalService);

	public void setResourcePermissionLocalService(
		ResourcePermissionLocalService resourcePermissionLocalService);

	public void setRoleLocalService(RoleLocalService roleLocalService);

	public void setVulcanBatchEngineImportTaskResource(
		VulcanBatchEngineImportTaskResource
			vulcanBatchEngineImportTaskResource);

	public default Filter toFilter(String filterString) {
		return toFilter(
			filterString, Collections.<String, List<String>>emptyMap());
	}

	public default Filter toFilter(
		String filterString, Map<String, List<String>> multivaluedMap) {

		return null;
	}

	public static class FactoryHolder {

		public static volatile Factory factory;

	}

	@ProviderType
	public interface Builder {

		public CommentResource build();

		public Builder checkPermissions(boolean checkPermissions);

		public Builder httpServletRequest(
			HttpServletRequest httpServletRequest);

		public Builder httpServletResponse(
			HttpServletResponse httpServletResponse);

		public Builder preferredLocale(Locale preferredLocale);

		public Builder user(com.liferay.portal.kernel.model.User user);

	}

	@ProviderType
	public interface Factory {

		public Builder create();

	}

}