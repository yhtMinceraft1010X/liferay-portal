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

package com.liferay.portal.search.internal.indexer;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.search.DocumentContributor;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistry;
import com.liferay.portal.kernel.search.SearchResultPermissionFilterFactory;
import com.liferay.portal.kernel.search.hits.HitsProcessorRegistry;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.search.batch.BatchIndexingHelper;
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.index.UpdateDocumentIndexWriter;
import com.liferay.portal.search.indexer.BaseModelDocumentFactory;
import com.liferay.portal.search.indexer.BaseModelRetriever;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;
import com.liferay.portal.search.indexer.IndexerPermissionPostFilter;
import com.liferay.portal.search.indexer.IndexerQueryBuilder;
import com.liferay.portal.search.indexer.IndexerSearcher;
import com.liferay.portal.search.indexer.IndexerSummaryBuilder;
import com.liferay.portal.search.indexer.IndexerWriter;
import com.liferay.portal.search.internal.expando.helper.ExpandoQueryContributorHelper;
import com.liferay.portal.search.internal.indexer.helper.AddSearchKeywordsQueryContributorHelper;
import com.liferay.portal.search.internal.indexer.helper.PreFilterContributorHelper;
import com.liferay.portal.search.internal.searcher.helper.IndexSearcherHelper;
import com.liferay.portal.search.permission.SearchPermissionDocumentContributor;
import com.liferay.portal.search.permission.SearchPermissionIndexWriter;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.portal.search.spi.model.query.contributor.QueryConfigContributor;
import com.liferay.portal.search.spi.model.query.contributor.SearchContextContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchConfigurator;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	service = ModelSearchConfiguratorServiceTrackerCustomizer.class
)
public class ModelSearchConfiguratorServiceTrackerCustomizer
	<T extends BaseModel<?>>
		implements ServiceTrackerCustomizer
			<ModelSearchConfigurator<T>, ModelSearchConfigurator<T>> {

	@Override
	@SuppressWarnings("unchecked")
	public ModelSearchConfigurator<T> addingService(
		ServiceReference<ModelSearchConfigurator<T>> serviceReference) {

		int serviceRanking = GetterUtil.getInteger(
			serviceReference.getProperty(Constants.SERVICE_RANKING));

		ModelSearchConfigurator<T> modelSearchConfigurator =
			_bundleContext.getService(serviceReference);

		ServiceRegistrationHolder serviceRegistrationHolder =
			_serviceRegistrationHolders.get(
				modelSearchConfigurator.getClassName());

		if ((serviceRegistrationHolder != null) &&
			(serviceRegistrationHolder._serviceRanking > serviceRanking)) {

			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						ClassUtil.getClassName(serviceRegistrationHolder),
						" is already registered with a higher ranking of ",
						serviceRegistrationHolder._serviceRanking, " for: ",
						modelSearchConfigurator.getClassName()));
			}

			return modelSearchConfigurator;
		}

		serviceRegistrationHolder = new ServiceRegistrationHolder(
			modelSearchConfigurator, serviceRanking);

		Dictionary<String, ?> serviceProperties = new Hashtable<>(
			Collections.singletonMap(
				"indexer.class.name", modelSearchConfigurator.getClassName()));

		Indexer<?> defaultIndexer = _buildIndexer(
			modelSearchConfigurator, serviceRegistrationHolder,
			serviceProperties);

		serviceRegistrationHolder.setIndexerServiceRegistration(
			_bundleContext.registerService(
				(Class<Indexer<?>>)(Class<?>)Indexer.class, defaultIndexer,
				serviceProperties));

		_serviceRegistrationHolders.put(
			modelSearchConfigurator.getClassName(), serviceRegistrationHolder);

		return modelSearchConfigurator;
	}

	@Override
	public void modifiedService(
		ServiceReference<ModelSearchConfigurator<T>> serviceReference,
		ModelSearchConfigurator<T> modelSearchConfigurator) {

		removedService(serviceReference, modelSearchConfigurator);

		addingService(serviceReference);
	}

	@Override
	public void removedService(
		ServiceReference<ModelSearchConfigurator<T>> serviceReference,
		ModelSearchConfigurator<T> modelSearchConfigurator) {

		ServiceRegistrationHolder serviceRegistrationHolder =
			_serviceRegistrationHolders.remove(
				modelSearchConfigurator.getClassName());

		if (serviceRegistrationHolder != null) {
			serviceRegistrationHolder.close();
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_documentContributors = ServiceTrackerListFactory.open(
			_bundleContext,
			(Class<DocumentContributor<?>>)(Class<?>)DocumentContributor.class,
			"(!(indexer.class.name=*))");

		_modelResourcePermissionServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext,
				(Class<ModelResourcePermission<?>>)
					(Class<?>)ModelResourcePermission.class,
				"model.class.name");

		_queryConfigContributors = ServiceTrackerListFactory.open(
			_bundleContext, QueryConfigContributor.class,
			"(!(indexer.class.name=*))");

		_searchContextContributors = ServiceTrackerListFactory.open(
			_bundleContext, SearchContextContributor.class,
			"(!(indexer.class.name=*))");

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext,
			(Class<ModelSearchConfigurator<T>>)
				(Class<?>)ModelSearchConfigurator.class,
			this);
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext = null;

		_serviceTracker.close();
		_documentContributors.close();
		_queryConfigContributors.close();
		_searchContextContributors.close();

		_serviceRegistrationHolders.forEach(
			(key, serviceRegistrationHolder) ->
				serviceRegistrationHolder.close());
	}

	@Reference
	protected AddSearchKeywordsQueryContributorHelper
		addSearchKeywordsQueryContributorHelper;

	@Reference
	protected BaseModelDocumentFactory baseModelDocumentFactory;

	@Reference
	protected BaseModelRetriever baseModelRetriever;

	@Reference
	protected BatchIndexingHelper batchIndexingHelper;

	@Reference
	protected ExpandoQueryContributorHelper expandoQueryContributorHelper;

	@Reference
	protected HitsProcessorRegistry hitsProcessorRegistry;

	@Reference
	protected IndexerRegistry indexerRegistry;

	@Reference
	protected IndexSearcherHelper indexSearcherHelper;

	@Reference
	protected IndexStatusManager indexStatusManager;

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	@Reference
	protected PreFilterContributorHelper preFilterContributorHelper;

	@Reference
	protected Props props;

	@Reference
	protected RelatedEntryIndexerRegistry relatedEntryIndexerRegistry;

	@Reference
	protected SearchPermissionDocumentContributor
		searchPermissionDocumentContributor;

	@Reference
	protected SearchPermissionIndexWriter searchPermissionIndexWriter;

	@Reference
	protected SearchResultPermissionFilterFactory
		searchResultPermissionFilterFactory;

	@Reference
	protected UpdateDocumentIndexWriter updateDocumentIndexWriter;

	private Indexer<?> _buildIndexer(
		ModelSearchConfigurator<T> modelSearchConfigurator,
		ServiceRegistrationHolder serviceRegistrationHolder,
		Dictionary<String, ?> serviceProperties) {

		Iterable<ModelDocumentContributor<?>> modelDocumentContributors =
			modelSearchConfigurator.getModelDocumentContributors();

		Iterable<DocumentContributor<?>> documentContributors =
			_documentContributors;

		IndexerPostProcessorsHolder indexerPostProcessorsHolder =
			new IndexerPostProcessorsHolder();

		IndexerDocumentBuilder indexerDocumentBuilder =
			new IndexerDocumentBuilderImpl(
				baseModelDocumentFactory, modelDocumentContributors,
				documentContributors, indexerPostProcessorsHolder,
				searchPermissionDocumentContributor);

		serviceRegistrationHolder.setIndexerDocumentBuilderServiceRegistration(
			_bundleContext.registerService(
				IndexerDocumentBuilder.class, indexerDocumentBuilder,
				serviceProperties));

		IndexerQueryBuilderImpl indexerQueryBuilderImpl =
			new IndexerQueryBuilderImpl<>(
				addSearchKeywordsQueryContributorHelper,
				expandoQueryContributorHelper, indexerRegistry,
				modelSearchConfigurator.getModelSearchSettings(),
				new ModelKeywordQueryContributorsHolderImpl(
					modelSearchConfigurator.getKeywordQueryContributors()),
				modelSearchConfigurator.getSearchContextContributors(),
				preFilterContributorHelper, _searchContextContributors,
				indexerPostProcessorsHolder, relatedEntryIndexerRegistry);

		serviceRegistrationHolder.setIndexerQueryBuilderServiceRegistration(
			_bundleContext.registerService(
				IndexerQueryBuilder.class, indexerQueryBuilderImpl,
				serviceProperties));

		IndexerPermissionPostFilter indexerPermissionPostFilter =
			new IndexerPermissionPostFilterImpl(
				() -> Optional.ofNullable(
					_modelResourcePermissionServiceTrackerMap.getService(
						modelSearchConfigurator.getClassName())),
				() -> Optional.ofNullable(
					modelSearchConfigurator.getModelVisibilityContributor()));

		serviceRegistrationHolder.
			setIndexerPermissionPostFilterServiceRegistration(
				_bundleContext.registerService(
					IndexerPermissionPostFilter.class,
					indexerPermissionPostFilter, serviceProperties));

		IndexerSearcher indexerSearcher = new IndexerSearcherImpl<>(
			modelSearchConfigurator.getModelSearchSettings(),
			modelSearchConfigurator.getQueryConfigContributors(),
			indexerPermissionPostFilter, indexerQueryBuilderImpl,
			hitsProcessorRegistry, indexSearcherHelper,
			_queryConfigContributors, searchResultPermissionFilterFactory);

		serviceRegistrationHolder.setIndexerSearcherServiceRegistration(
			_bundleContext.registerService(
				IndexerSearcher.class, indexerSearcher, serviceProperties));

		IndexerWriter<?> indexerWriter = new IndexerWriterImpl<>(
			modelSearchConfigurator.getModelSearchSettings(),
			baseModelRetriever, batchIndexingHelper,
			modelSearchConfigurator.getModelIndexerWriterContributor(),
			indexerDocumentBuilder, searchPermissionIndexWriter,
			updateDocumentIndexWriter, indexStatusManager, indexWriterHelper,
			props);

		serviceRegistrationHolder.setIndexerWriterServiceRegistration(
			_bundleContext.registerService(
				(Class<IndexerWriter<?>>)(Class<?>)IndexerWriter.class,
				indexerWriter, serviceProperties));

		IndexerSummaryBuilder indexerSummaryBuilder =
			new IndexerSummaryBuilderImpl(
				modelSearchConfigurator.getModelSummaryBuilder(),
				indexerPostProcessorsHolder);

		serviceRegistrationHolder.setIndexerSummaryBuilderServiceRegistration(
			_bundleContext.registerService(
				IndexerSummaryBuilder.class, indexerSummaryBuilder,
				serviceProperties));

		return new DefaultIndexer<>(
			modelSearchConfigurator.getModelSearchSettings(),
			indexerDocumentBuilder, indexerSearcher, indexerWriter,
			indexerPermissionPostFilter, indexerQueryBuilderImpl,
			indexerSummaryBuilder, indexerPostProcessorsHolder);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ModelSearchConfiguratorServiceTrackerCustomizer.class);

	private BundleContext _bundleContext;
	private ServiceTrackerList<DocumentContributor<?>> _documentContributors;
	private ServiceTrackerMap<String, ModelResourcePermission<?>>
		_modelResourcePermissionServiceTrackerMap;

	@Reference(target = ModuleServiceLifecycle.PORTLETS_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	private ServiceTrackerList<QueryConfigContributor> _queryConfigContributors;
	private ServiceTrackerList<SearchContextContributor>
		_searchContextContributors;
	private final Map<String, ServiceRegistrationHolder>
		_serviceRegistrationHolders = new Hashtable<>();
	private ServiceTracker
		<ModelSearchConfigurator<T>, ModelSearchConfigurator<T>>
			_serviceTracker;

	private class ServiceRegistrationHolder {

		public ServiceRegistrationHolder(
			ModelSearchConfigurator<?> modelSearchConfigurator,
			int serviceRanking) {

			_modelSearchConfigurator = modelSearchConfigurator;
			_serviceRanking = serviceRanking;
		}

		public void close() {
			_modelSearchConfigurator.close();

			if (_indexerDocumentBuilderServiceRegistration != null) {
				_indexerDocumentBuilderServiceRegistration.unregister();
			}

			if (_indexerPermissionPostFilterServiceRegistration != null) {
				_indexerPermissionPostFilterServiceRegistration.unregister();
			}

			if (_indexerQueryBuilderServiceRegistration != null) {
				_indexerQueryBuilderServiceRegistration.unregister();
			}

			if (_indexerSearcherServiceRegistration != null) {
				_indexerSearcherServiceRegistration.unregister();
			}

			if (_indexerServiceRegistration != null) {
				_indexerServiceRegistration.unregister();
			}

			if (_indexerSummaryBuilderServiceRegistration != null) {
				_indexerSummaryBuilderServiceRegistration.unregister();
			}

			if (_indexerWriterServiceRegistration != null) {
				_indexerWriterServiceRegistration.unregister();
			}
		}

		public void setIndexerDocumentBuilderServiceRegistration(
			ServiceRegistration<IndexerDocumentBuilder>
				indexerDocumentBuilderServiceRegistration) {

			_indexerDocumentBuilderServiceRegistration =
				indexerDocumentBuilderServiceRegistration;
		}

		public void setIndexerPermissionPostFilterServiceRegistration(
			ServiceRegistration<IndexerPermissionPostFilter>
				indexerPermissionPostFilterServiceRegistration) {

			_indexerPermissionPostFilterServiceRegistration =
				indexerPermissionPostFilterServiceRegistration;
		}

		public void setIndexerQueryBuilderServiceRegistration(
			ServiceRegistration<IndexerQueryBuilder>
				indexerQueryBuilderServiceRegistration) {

			_indexerQueryBuilderServiceRegistration =
				indexerQueryBuilderServiceRegistration;
		}

		public void setIndexerSearcherServiceRegistration(
			ServiceRegistration<IndexerSearcher>
				indexerSearcherServiceRegistration) {

			_indexerSearcherServiceRegistration =
				indexerSearcherServiceRegistration;
		}

		public void setIndexerServiceRegistration(
			ServiceRegistration<Indexer<?>> indexerServiceRegistration) {

			_indexerServiceRegistration = indexerServiceRegistration;
		}

		public void setIndexerSummaryBuilderServiceRegistration(
			ServiceRegistration<IndexerSummaryBuilder>
				indexerSummaryBuilderServiceRegistration) {

			_indexerSummaryBuilderServiceRegistration =
				indexerSummaryBuilderServiceRegistration;
		}

		public void setIndexerWriterServiceRegistration(
			ServiceRegistration<IndexerWriter<?>>
				indexerWriterServiceRegistration) {

			_indexerWriterServiceRegistration =
				indexerWriterServiceRegistration;
		}

		private ServiceRegistration<IndexerDocumentBuilder>
			_indexerDocumentBuilderServiceRegistration;
		private ServiceRegistration<IndexerPermissionPostFilter>
			_indexerPermissionPostFilterServiceRegistration;
		private ServiceRegistration<IndexerQueryBuilder>
			_indexerQueryBuilderServiceRegistration;
		private ServiceRegistration<IndexerSearcher>
			_indexerSearcherServiceRegistration;
		private ServiceRegistration<Indexer<?>> _indexerServiceRegistration;
		private ServiceRegistration<IndexerSummaryBuilder>
			_indexerSummaryBuilderServiceRegistration;
		private ServiceRegistration<IndexerWriter<?>>
			_indexerWriterServiceRegistration;
		private final ModelSearchConfigurator<?> _modelSearchConfigurator;
		private final int _serviceRanking;

	}

}