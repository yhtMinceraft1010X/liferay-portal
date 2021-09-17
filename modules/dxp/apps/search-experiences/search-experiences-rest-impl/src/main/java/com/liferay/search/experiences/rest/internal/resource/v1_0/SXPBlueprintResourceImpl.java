/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.rest.internal.resource.v1_0;

import com.liferay.search.experiences.rest.dto.v1_0.SXPBlueprint;
import com.liferay.search.experiences.rest.resource.v1_0.SXPBlueprintResource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/sxp-blueprint.properties",
	scope = ServiceScope.PROTOTYPE, service = SXPBlueprintResource.class
)
public class SXPBlueprintResourceImpl extends BaseSXPBlueprintResourceImpl {

	@Override
	public void deleteSXPBlueprint(Long sxpBlueprintId) throws Exception {
	}

	@Override
	public SXPBlueprint getSXPBlueprint(Long sxpBlueprintId) throws Exception {
		return null;
	}

	@Override
	public SXPBlueprint postSXPBlueprint(SXPBlueprint sxpBlueprint)
		throws Exception {

		return null;
	}

}