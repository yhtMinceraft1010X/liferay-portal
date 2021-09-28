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

package com.liferay.search.experiences.internal.blueprint.aggregation;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.aggregation.bucket.CollectionMode;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.DateRangeAggregation;
import com.liferay.portal.search.aggregation.bucket.DiversifiedSamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FiltersAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoDistanceAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoHashGridAggregation;
import com.liferay.portal.search.aggregation.bucket.GlobalAggregation;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.IncludeExcludeClause;
import com.liferay.portal.search.aggregation.bucket.MissingAggregation;
import com.liferay.portal.search.aggregation.bucket.NestedAggregation;
import com.liferay.portal.search.aggregation.bucket.Order;
import com.liferay.portal.search.aggregation.bucket.Range;
import com.liferay.portal.search.aggregation.bucket.RangeAggregation;
import com.liferay.portal.search.aggregation.bucket.ReverseNestedAggregation;
import com.liferay.portal.search.aggregation.bucket.SamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.SignificantTermsAggregation;
import com.liferay.portal.search.aggregation.bucket.SignificantTextAggregation;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.metrics.AvgAggregation;
import com.liferay.portal.search.aggregation.metrics.CardinalityAggregation;
import com.liferay.portal.search.aggregation.metrics.ExtendedStatsAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoBoundsAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoCentroidAggregation;
import com.liferay.portal.search.aggregation.metrics.MaxAggregation;
import com.liferay.portal.search.aggregation.metrics.MinAggregation;
import com.liferay.portal.search.aggregation.metrics.PercentileRanksAggregation;
import com.liferay.portal.search.aggregation.metrics.PercentilesAggregation;
import com.liferay.portal.search.aggregation.metrics.ScriptedMetricAggregation;
import com.liferay.portal.search.aggregation.metrics.StatsAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.metrics.TopHitsAggregation;
import com.liferay.portal.search.aggregation.metrics.ValueCountAggregation;
import com.liferay.portal.search.aggregation.metrics.WeightedAvgAggregation;
import com.liferay.portal.search.aggregation.pipeline.AvgBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.BucketScriptPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.BucketSelectorPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.BucketSortPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.CumulativeSumPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.ExtendedStatsBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.GapPolicy;
import com.liferay.portal.search.aggregation.pipeline.MaxBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MinBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MovingFunctionPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.SerialDiffPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.StatsBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.SumBucketPipelineAggregation;
import com.liferay.portal.search.geolocation.DistanceUnit;
import com.liferay.portal.search.geolocation.GeoBuilders;
import com.liferay.portal.search.geolocation.GeoDistanceType;
import com.liferay.portal.search.script.Script;
import com.liferay.portal.search.script.ScriptField;
import com.liferay.portal.search.script.ScriptFieldBuilder;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.search.sort.FieldSort;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;
import com.liferay.search.experiences.internal.blueprint.highlight.HighlightConverter;
import com.liferay.search.experiences.internal.blueprint.script.ScriptConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Petteri Karttunen
 */
public class AggregationWrapperConverter {

	public AggregationWrapperConverter(
		Aggregations aggregations, GeoBuilders geoBuilders,
		HighlightConverter highlightConverter, ScriptConverter scriptConverter,
		Sorts sorts) {

		_aggregations = aggregations;
		_geoBuilders = geoBuilders;
		_scriptConverter = scriptConverter;
		_sorts = sorts;

		// Bucket

		_convertFunctions.putAll(
			HashMapBuilder.<String, ConvertFunction>put(
				"date_histogram", this::_toDateHistogramAggregation
			).put(
				"date_range", this::_toDateRangeAggregation
			).put(
				"diversified_sampler", this::_toDiversifiedSamplerAggregation
			).put(
				"filter", this::_toFilterAggregation
			).put(
				"filters", this::_toFiltersAggregation
			).put(
				"geo_distance", this::_toGeoDistanceAggregation
			).put(
				"geohash_grid", this::_toGeoHashGridAggregation
			).put(
				"global", this::_toGlobalAggregation
			).put(
				"histogram", this::_toHistogramAggregation
			).put(
				"missing", this::_toMissingAggregation
			).put(
				"nested", this::_toNestedAggregation
			).put(
				"range", this::_toRangeAggregation
			).put(
				"reverse_nested", this::_toReverseNestedAggregation
			).put(
				"sampler", this::_toSamplerAggregation
			).put(
				"significant_terms", this::_toSignificantTermsAggregation
			).put(
				"significant_text", this::_toSignificantTextAggregation
			).put(
				"terms", this::_toTermsAggregation
			).build());

		// Metrics

		_convertFunctions.putAll(
			HashMapBuilder.<String, ConvertFunction>put(
				"avg", this::_toAvgAggregation
			).put(
				"cardinality", this::_toCardinalityAggregation
			).put(
				"extended_stats", this::_toExtendedStatsAggregation
			).put(
				"geo_bounds", this::_toGeoBoundsAggregation
			).put(
				"geo_centroid", this::_toGeoCentroidAggregation
			).put(
				"max", this::_toMaxAggregation
			).put(
				"min", this::_toMinAggregation
			).put(
				"percentile_ranks", this::_toPercentileRanksAggregation
			).put(
				"percentiles", this::_toPercentilesAggregation
			).put(
				"scripted_metric", this::_toScriptedMetricAggregation
			).put(
				"scripted_metric", this::_toStatsAggregation
			).put(
				"sum", this::_toSumAggregation
			).put(
				"top_hits", this::_toTopHitsAggregation
			).put(
				"value_count", this::_toValueCountAggregation
			).put(
				"weighted_avg", this::_toWeightedAvgAggregation
			).build());

		// Pipeline

		_convertFunctions.putAll(
			HashMapBuilder.<String, ConvertFunction>put(
				"avg_bucket", this::_toAvgBucketPipelineAggregation
			).put(
				"bucket_script", this::_toBucketScriptPipelineAggregation
			).put(
				"bucket_selector", this::_toBucketSelectorPipelineAggregation
			).put(
				"bucket_sort", this::_toBucketSortPipelineAggregation
			).put(
				"cumulative_sum", this::_toCumulativeSumPipelineAggregation
			).put(
				"derivative", this::_toDerivativePipelineAggregation
			).put(
				"extended_stats_bucket",
				this::_toExtendedStatsBucketPipelineAggregation
			).put(
				"max_bucket", this::_toMaxBucketPipelineAggregation
			).put(
				"min_bucket", this::_toMinBucketPipelineAggregation
			).put(
				"moving_function", this::_toMovingFunctionPipelineAggregation
			).put(
				"percentiles_bucket",
				this::_toPercentilesBucketPipelineAggregation
			).put(
				"serial_differencing", this::_toSerialDiffPipelineAggregation
			).put(
				"stats_bucket", this::_toStatsBucketPipelineAggregation
			).put(
				"sum_bucket", this::_toSumBucketPipelineAggregation
			).build());

		_scripts = scriptConverter.getScripts();
	}

	public AggregationWrapper toAggregationWrapper(
		JSONObject jsonObject, String name, String type) {

		ConvertFunction convertFunction = _convertFunctions.get(type);

		if (convertFunction == null) {
			throw new IllegalArgumentException("Uknown aggregation " + type);
		}

		try {
			Object object = convertFunction.apply(jsonObject, name);

			if (object == null) {
				return null;
			}

			if (object instanceof Aggregation) {
				return new AggregationWrapper((Aggregation)object);
			}
			else if (object instanceof PipelineAggregation) {
				return new AggregationWrapper((PipelineAggregation)object);
			}
		}
		catch (Exception exception) {
			return ReflectionUtil.throwException(exception);
		}

		throw new IllegalArgumentException();
	}

	@FunctionalInterface
	public interface ConvertFunction {

		public Object apply(JSONObject jsonObject, String name)
			throws Exception;

	}

	private void _addBucketsPaths(
		BiConsumer<String, String> biConsumer, JSONObject jsonObject) {

		Object object = jsonObject.get("buckets_path");

		if ((object == null) || !(object instanceof JSONObject)) {
			return;
		}

		JSONObject bucketsPathJSONObject = (JSONObject)object;

		for (String key : bucketsPathJSONObject.keySet()) {
			biConsumer.accept(key, bucketsPathJSONObject.getString(key));
		}
	}

	private void _addOrders(Consumer<Order[]> consumer, JSONObject jsonObject) {
		JSONObject orderJSONObject = jsonObject.getJSONObject("order");

		if (orderJSONObject == null) {
			return;
		}

		List<Order> orders = new ArrayList<>();

		for (String key : orderJSONObject.keySet()) {
			Order order = null;

			boolean ascending = StringUtil.equalsIgnoreCase(
				orderJSONObject.getString(key), "asc");

			if (Order.COUNT_METRIC_NAME.equals(key)) {
				order = Order.count(ascending);
			}
			else if (Order.KEY_METRIC_NAME.equals(key)) {
				order = Order.key(ascending);
			}
			else {
				order = new Order(key);

				order.setAscending(ascending);
			}

			orders.add(order);
		}

		consumer.accept(orders.toArray(new Order[0]));
	}

	private void _addRange(Consumer<Range> consumer, JSONObject jsonObject) {
		JSONArray rangesJSONArray = jsonObject.getJSONArray("ranges");

		if (rangesJSONArray == null) {
			return;
		}

		for (int i = 0; i < rangesJSONArray.length(); i++) {
			JSONObject rangeJSONObject = rangesJSONArray.getJSONObject(i);

			String key = rangeJSONObject.getString("key");

			if (Validator.isNotNull(key)) {
				consumer.accept(
					new Range(
						key, rangeJSONObject.getString("from", null),
						rangeJSONObject.getString("to", null)));
			}
			else {
				consumer.accept(
					new Range(
						rangeJSONObject.getString("from", null),
						rangeJSONObject.getString("to", null)));
			}
		}
	}

	private void _setBoolean(
		Consumer<Boolean> consumer, JSONObject jsonObject, String key) {

		if (!jsonObject.has(key)) {
			return;
		}

		consumer.accept(jsonObject.getBoolean(key));
	}

	private void _setGapPolicy(
		Consumer<GapPolicy> consumer, JSONObject jsonObject) {

		String gapPolicy = jsonObject.getString("gap_policy");

		if (Validator.isNull(gapPolicy)) {
			return;
		}

		consumer.accept(GapPolicy.valueOf(StringUtil.toUpperCase(gapPolicy)));
	}

	private void _setIncludeExcludeClause(
		Consumer<IncludeExcludeClause> consumer, JSONObject jsonObject) {

		Object excludeObject = jsonObject.get("exclude");
		Object includeObject = jsonObject.get("include");

		if ((excludeObject == null) && (includeObject == null)) {
			return;
		}

		String[] excludedValues = null;
		String excludeRegex = null;
		String[] includedValues = null;
		String includeRegex = null;

		if (excludeObject != null) {
			if (excludeObject instanceof JSONArray) {
				excludedValues = JSONUtil.toStringArray(
					(JSONArray)excludeObject);
			}
			else {
				excludeRegex = GetterUtil.getString(excludeObject);
			}
		}

		if (includeObject != null) {
			if (includeObject instanceof JSONArray) {
				includedValues = JSONUtil.toStringArray(
					(JSONArray)includeObject);
			}
			else {
				includeRegex = GetterUtil.getString(includeObject);
			}
		}

		final String[] finalExcludedValues = excludedValues;
		final String finalExcludeRegex = excludeRegex;
		final String[] finalIncludedValues = includedValues;
		final String finalIncludeRegex = includeRegex;

		consumer.accept(
			new IncludeExcludeClause() {

				@Override
				public String[] getExcludedValues() {
					return finalExcludedValues;
				}

				@Override
				public String getExcludeRegex() {
					return finalExcludeRegex;
				}

				@Override
				public String[] getIncludedValues() {
					return finalIncludedValues;
				}

				@Override
				public String getIncludeRegex() {
					return finalIncludeRegex;
				}

			});
	}

	private void _setInteger(
		Consumer<Integer> consumer, JSONObject jsonObject, String key) {

		if (!jsonObject.has(key)) {
			return;
		}

		consumer.accept(jsonObject.getInt(key));
	}

	private void _setLong(
		Consumer<Long> consumer, JSONObject jsonObject, String key) {

		if (!jsonObject.has(key)) {
			return;
		}

		consumer.accept(jsonObject.getLong(key));
	}

	private void _setObject(
		Consumer<Object> consumer, JSONObject jsonObject, String key) {

		if (!jsonObject.has(key)) {
			return;
		}

		consumer.accept(jsonObject.get(key));
	}

	private void _setScript(Consumer<Script> consumer, JSONObject jsonObject) {
		consumer.accept(_scriptConverter.toScript(jsonObject.get("script")));
	}

	private void _setString(
		Consumer<String> consumer, JSONObject jsonObject, String key) {

		if (!jsonObject.has(key)) {
			return;
		}

		consumer.accept(jsonObject.getString(key));
	}

	private AvgAggregation _toAvgAggregation(
		JSONObject jsonObject, String name) {

		AvgAggregation avgAggregation = _aggregations.avg(
			name, jsonObject.getString("field"));

		_setString(avgAggregation::setMissing, jsonObject, "missing");
		_setScript(avgAggregation::setScript, jsonObject);

		return avgAggregation;
	}

	private AvgBucketPipelineAggregation _toAvgBucketPipelineAggregation(
		JSONObject jsonObject, String name) {

		AvgBucketPipelineAggregation avgBucketPipelineAggregation =
			_aggregations.avgBucket(name, jsonObject.getString("buckets_path"));

		_setString(
			avgBucketPipelineAggregation::setFormat, jsonObject, "format");
		_setGapPolicy(avgBucketPipelineAggregation::setGapPolicy, jsonObject);

		return avgBucketPipelineAggregation;
	}

	private BucketScriptPipelineAggregation _toBucketScriptPipelineAggregation(
		JSONObject jsonObject, String name) {

		Script script = _scriptConverter.toScript(jsonObject.get("script"));

		if (script == null) {
			return null;
		}

		BucketScriptPipelineAggregation bucketScriptPipelineAggregation =
			_aggregations.bucketScript(name, script);

		_addBucketsPaths(
			bucketScriptPipelineAggregation::addBucketPath, jsonObject);
		_setString(
			bucketScriptPipelineAggregation::setFormat, jsonObject, "format");

		return bucketScriptPipelineAggregation;
	}

	private BucketSelectorPipelineAggregation
		_toBucketSelectorPipelineAggregation(
			JSONObject jsonObject, String name) {

		Script script = _scriptConverter.toScript(jsonObject.get("script"));

		if (script == null) {
			return null;
		}

		BucketSelectorPipelineAggregation bucketSelectorPipelineAggregation =
			_aggregations.bucketSelector(name, script);

		_addBucketsPaths(
			bucketSelectorPipelineAggregation::addBucketPath, jsonObject);
		_setGapPolicy(
			bucketSelectorPipelineAggregation::setGapPolicy, jsonObject);

		return bucketSelectorPipelineAggregation;
	}

	private BucketSortPipelineAggregation _toBucketSortPipelineAggregation(
		JSONObject jsonObject, String name) {

		BucketSortPipelineAggregation bucketSortPipelineAggregation =
			_aggregations.bucketSort(name);

		if (jsonObject.has("sort")) {
			JSONArray sortJSONArray = jsonObject.getJSONArray("sort");

			List<FieldSort> fieldSorts = new ArrayList<>();

			for (int i = 0; i < sortJSONArray.length(); i++) {
				JSONObject sortJSONObject = sortJSONArray.getJSONObject(i);

				Iterator<String> iterator = sortJSONObject.keys();

				String field = iterator.next();

				JSONObject fieldJSONObject = sortJSONObject.getJSONObject(
					field);

				if (fieldJSONObject.has("order")) {
					fieldSorts.add(
						_sorts.field(
							field,
							SortOrder.valueOf(
								StringUtil.toUpperCase(
									fieldJSONObject.getString("order")))));
				}
				else {
					fieldSorts.add(_sorts.field(field));
				}
			}

			bucketSortPipelineAggregation.addSortFields(
				fieldSorts.toArray(new FieldSort[0]));
		}

		_setInteger(bucketSortPipelineAggregation::setFrom, jsonObject, "from");
		_setGapPolicy(bucketSortPipelineAggregation::setGapPolicy, jsonObject);
		_setInteger(bucketSortPipelineAggregation::setSize, jsonObject, "size");

		return bucketSortPipelineAggregation;
	}

	private CardinalityAggregation _toCardinalityAggregation(
		JSONObject jsonObject, String name) {

		CardinalityAggregation cardinalityAggregation =
			_aggregations.cardinality(name, jsonObject.getString("field"));

		_setString(cardinalityAggregation::setMissing, jsonObject, "missing");
		_setInteger(
			cardinalityAggregation::setPrecisionThreshold, jsonObject,
			"precision_threshold");
		_setScript(cardinalityAggregation::setScript, jsonObject);

		return cardinalityAggregation;
	}

	private CumulativeSumPipelineAggregation
		_toCumulativeSumPipelineAggregation(
			JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private DateHistogramAggregation _toDateHistogramAggregation(
		JSONObject jsonObject, String name) {

		DateHistogramAggregation dateHistogramAggregation =
			_aggregations.dateHistogram(name, jsonObject.getString("field"));

		_addOrders(dateHistogramAggregation::addOrders, jsonObject);
		_setString(
			dateHistogramAggregation::setDateHistogramInterval, jsonObject,
			"date_histogram_interval");

		JSONObject extendedBoundsJSONObject = jsonObject.getJSONObject(
			"extended_bounds");

		if (extendedBoundsJSONObject != null) {
			dateHistogramAggregation.setBounds(
				extendedBoundsJSONObject.getLong("min"),
				extendedBoundsJSONObject.getLong("max"));
		}
		else {
			JSONObject hardBoundsJSONObject = jsonObject.getJSONObject(
				"hard_bounds");

			if (hardBoundsJSONObject != null) {
				dateHistogramAggregation.setBounds(
					hardBoundsJSONObject.getLong("min"),
					hardBoundsJSONObject.getLong("max"));
			}
		}

		_setBoolean(dateHistogramAggregation::setKeyed, jsonObject, "keyed");
		_setLong(
			dateHistogramAggregation::setMinDocCount, jsonObject,
			"min_doc_count");
		_setString(dateHistogramAggregation::setMissing, jsonObject, "missing");
		_setLong(dateHistogramAggregation::setOffset, jsonObject, "offset");
		_setScript(dateHistogramAggregation::setScript, jsonObject);

		return dateHistogramAggregation;
	}

	private DateRangeAggregation _toDateRangeAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private DerivativePipelineAggregation _toDerivativePipelineAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private DiversifiedSamplerAggregation _toDiversifiedSamplerAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private ExtendedStatsAggregation _toExtendedStatsAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private ExtendedStatsBucketPipelineAggregation
		_toExtendedStatsBucketPipelineAggregation(
			JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private FilterAggregation _toFilterAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private FiltersAggregation _toFiltersAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private GeoBoundsAggregation _toGeoBoundsAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private GeoCentroidAggregation _toGeoCentroidAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private GeoDistanceAggregation _toGeoDistanceAggregation(
		JSONObject jsonObject, String name) {

		String[] coordinates = StringUtil.split(jsonObject.getString("origin"));

		if (coordinates.length != 2) {
			return null;
		}

		GeoDistanceAggregation geoDistanceAggregation =
			_aggregations.geoDistance(
				name, jsonObject.getString("field"),
				_geoBuilders.geoLocationPoint(
					GetterUtil.getDouble(coordinates[0]),
					GetterUtil.getDouble(coordinates[1])));

		_addRange(geoDistanceAggregation::addRange, jsonObject);
		geoDistanceAggregation.setDistanceUnit(
			DistanceUnit.create(jsonObject.getString("unit")));

		String distanceType = jsonObject.getString("distance_type");

		if (Validator.isNotNull(distanceType)) {
			geoDistanceAggregation.setGeoDistanceType(
				GeoDistanceType.valueOf(StringUtil.toUpperCase(distanceType)));
		}

		_setBoolean(geoDistanceAggregation::setKeyed, jsonObject, "keyed");
		_setScript(geoDistanceAggregation::setScript, jsonObject);

		return geoDistanceAggregation;
	}

	private GeoHashGridAggregation _toGeoHashGridAggregation(
		JSONObject jsonObject, String name) {

		GeoHashGridAggregation geoHashGridAggregation =
			_aggregations.geoHashGrid(name, jsonObject.getString("field"));

		_setString(geoHashGridAggregation::setMissing, jsonObject, "missing");
		_setInteger(
			geoHashGridAggregation::setPrecision, jsonObject, "precision");
		_setScript(geoHashGridAggregation::setScript, jsonObject);
		_setInteger(
			geoHashGridAggregation::setShardSize, jsonObject, "shard_size");
		_setInteger(geoHashGridAggregation::setSize, jsonObject, "size");

		return geoHashGridAggregation;
	}

	private GlobalAggregation _toGlobalAggregation(
		JSONObject jsonObject, String name) {

		return _aggregations.global(name);
	}

	private HistogramAggregation _toHistogramAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private MaxAggregation _toMaxAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private MaxBucketPipelineAggregation _toMaxBucketPipelineAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private MinAggregation _toMinAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private MinBucketPipelineAggregation _toMinBucketPipelineAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private MissingAggregation _toMissingAggregation(
		JSONObject jsonObject, String name) {

		MissingAggregation missingAggregation = _aggregations.missing(
			name, jsonObject.getString("field"));

		_setString(missingAggregation::setMissing, jsonObject, "missing");
		_setScript(missingAggregation::setScript, jsonObject);

		return missingAggregation;
	}

	private MovingFunctionPipelineAggregation
		_toMovingFunctionPipelineAggregation(
			JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private NestedAggregation _toNestedAggregation(
		JSONObject jsonObject, String name) {

		return _aggregations.nested(name, jsonObject.getString("path"));
	}

	private PercentileRanksAggregation _toPercentileRanksAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private PercentilesAggregation _toPercentilesAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private PercentilesBucketPipelineAggregation
		_toPercentilesBucketPipelineAggregation(
			JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private RangeAggregation _toRangeAggregation(
		JSONObject jsonObject, String name) {

		RangeAggregation rangeAggregation = _aggregations.range(
			name, jsonObject.getString("field"));

		_addRange(rangeAggregation::addRange, jsonObject);
		_setString(rangeAggregation::setFormat, jsonObject, "format");
		_setBoolean(rangeAggregation::setKeyed, jsonObject, "keyed");
		_setString(rangeAggregation::setMissing, jsonObject, "missing");
		_setScript(rangeAggregation::setScript, jsonObject);

		return rangeAggregation;
	}

	private ReverseNestedAggregation _toReverseNestedAggregation(
		JSONObject jsonObject, String name) {

		return _aggregations.reverseNested(name, jsonObject.getString("path"));
	}

	private SamplerAggregation _toSamplerAggregation(
		JSONObject jsonObject, String name) {

		SamplerAggregation samplerAggregation = _aggregations.sampler(name);

		_setInteger(samplerAggregation::setShardSize, jsonObject, "shard_size");

		return samplerAggregation;
	}

	private ScriptedMetricAggregation _toScriptedMetricAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private SerialDiffPipelineAggregation _toSerialDiffPipelineAggregation(
		JSONObject jsonObject, String name) {

		SerialDiffPipelineAggregation serialDiffPipelineAggregation =
			_aggregations.serialDiff(
				name, jsonObject.getString("buckets_path"));

		_setString(
			serialDiffPipelineAggregation::setFormat, jsonObject, "format");
		_setGapPolicy(serialDiffPipelineAggregation::setGapPolicy, jsonObject);
		_setInteger(serialDiffPipelineAggregation::setLag, jsonObject, "lag");

		return serialDiffPipelineAggregation;
	}

	private SignificantTermsAggregation _toSignificantTermsAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private SignificantTextAggregation _toSignificantTextAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private StatsAggregation _toStatsAggregation(
		JSONObject jsonObject, String name) {

		// TODO

		return null;
	}

	private StatsBucketPipelineAggregation _toStatsBucketPipelineAggregation(
		JSONObject jsonObject, String name) {

		StatsBucketPipelineAggregation statsBucketPipelineAggregation =
			_aggregations.statsBucket(
				name, jsonObject.getString("buckets_path"));

		_setString(
			statsBucketPipelineAggregation::setFormat, jsonObject, "format");
		_setGapPolicy(statsBucketPipelineAggregation::setGapPolicy, jsonObject);

		return statsBucketPipelineAggregation;
	}

	private String[] _toStringArray(JSONArray jsonArray) {
		if (jsonArray == null) {
			return null;
		}

		return JSONUtil.toStringArray(jsonArray);
	}

	private List<String> _toStringList(JSONArray jsonArray) {
		if (jsonArray == null) {
			return null;
		}

		return JSONUtil.toStringList(jsonArray);
	}

	private SumAggregation _toSumAggregation(
		JSONObject jsonObject, String name) {

		SumAggregation sumAggregation = _aggregations.sum(
			name, jsonObject.getString("field"));

		_setString(sumAggregation::setMissing, jsonObject, "missing");
		_setScript(sumAggregation::setScript, jsonObject);

		return sumAggregation;
	}

	private SumBucketPipelineAggregation _toSumBucketPipelineAggregation(
		JSONObject jsonObject, String name) {

		SumBucketPipelineAggregation sumBucketPipelineAggregation =
			_aggregations.sumBucket(name, jsonObject.getString("buckets_path"));

		_setString(
			sumBucketPipelineAggregation::setFormat, jsonObject, "format");
		_setGapPolicy(sumBucketPipelineAggregation::setGapPolicy, jsonObject);

		return sumBucketPipelineAggregation;
	}

	private TermsAggregation _toTermsAggregation(
		JSONObject jsonObject, String name) {

		TermsAggregation termsAggregation = _aggregations.terms(
			name, jsonObject.getString("field"));

		_addOrders(termsAggregation::addOrders, jsonObject);

		String collectMode = jsonObject.getString("collect_mode");

		if (Validator.isNotNull(collectMode)) {
			termsAggregation.setCollectionMode(
				CollectionMode.valueOf(StringUtil.toUpperCase(collectMode)));
		}

		_setString(
			termsAggregation::setExecutionHint, jsonObject, "execution_hint");
		_setIncludeExcludeClause(
			termsAggregation::setIncludeExcludeClause, jsonObject);
		_setInteger(
			termsAggregation::setMinDocCount, jsonObject, "min_doc_count");
		_setString(termsAggregation::setMissing, jsonObject, "missing");
		_setInteger(
			termsAggregation::setShardMinDocCount, jsonObject,
			"shard_min_doc_count");
		_setInteger(termsAggregation::setShardSize, jsonObject, "shard_size");
		_setBoolean(
			termsAggregation::setShowTermDocCountError, jsonObject,
			"show_term_doc_count_error");
		_setScript(termsAggregation::setScript, jsonObject);
		_setInteger(termsAggregation::setSize, jsonObject, "size");

		return termsAggregation;
	}

	private TopHitsAggregation _toTopHitsAggregation(
		JSONObject jsonObject, String name) {

		TopHitsAggregation topHitsAggregation = _aggregations.topHits(name);

		_setBoolean(topHitsAggregation::setExplain, jsonObject, "explain");

		Object object = jsonObject.get("_source");

		if (object != null) {
			if (object instanceof JSONObject) {
				JSONObject sourceJSONObject = (JSONObject)object;

				topHitsAggregation.setFetchSourceIncludeExclude(
					_toStringArray(sourceJSONObject.getJSONArray("includes")),
					_toStringArray(sourceJSONObject.getJSONArray("excludes")));
			}
			else {
				topHitsAggregation.setFetchSource(
					GetterUtil.getBoolean(object));
			}
		}

		_setInteger(topHitsAggregation::setFrom, jsonObject, "from");
		topHitsAggregation.setHighlight(
			_highlightConverter.toHighlight(
				jsonObject.getJSONObject("highlight")));

		JSONArray scriptFieldsJSONArray = jsonObject.getJSONArray(
			"script_fields");

		if (scriptFieldsJSONArray != null) {
			List<ScriptField> scriptFields = new ArrayList<>();

			for (int i = 0; i < scriptFieldsJSONArray.length(); i++) {
				JSONObject scriptFieldJSONObject =
					scriptFieldsJSONArray.getJSONObject(i);

				Iterator<String> iterator = scriptFieldJSONObject.keys();

				Script script = _scriptConverter.toScript(
					scriptFieldJSONObject.get(iterator.next()));

				if (script == null) {
					continue;
				}

				ScriptFieldBuilder scriptFieldBuilder = _scripts.fieldBuilder();

				scriptFieldBuilder.script(script);

				scriptFields.add(scriptFieldBuilder.build());
			}

			topHitsAggregation.setScriptFields(scriptFields);
		}

		topHitsAggregation.setSelectedFields(
			_toStringList(jsonObject.getJSONArray("docvalue_fields")));

		_setInteger(topHitsAggregation::setSize, jsonObject, "size");
		_setBoolean(
			topHitsAggregation::setTrackScores, jsonObject, "track_scores");
		_setBoolean(topHitsAggregation::setVersion, jsonObject, "version");

		return topHitsAggregation;
	}

	private ValueCountAggregation _toValueCountAggregation(
		JSONObject jsonObject, String name) {

		ValueCountAggregation valueCountAggregation = _aggregations.valueCount(
			name, jsonObject.getString("field"));

		_setScript(valueCountAggregation::setScript, jsonObject);

		return valueCountAggregation;
	}

	private WeightedAvgAggregation _toWeightedAvgAggregation(
		JSONObject jsonObject, String name) {

		JSONObject valueJSONObject = jsonObject.getJSONObject("value");
		JSONObject weightJSONObject = jsonObject.getJSONObject("weight");

		WeightedAvgAggregation weightedAvgAggregation =
			_aggregations.weightedAvg(
				name, valueJSONObject.getString("field"),
				weightJSONObject.getString("field"));

		_setString(weightedAvgAggregation::setFormat, jsonObject, "format");
		_setObject(
			weightedAvgAggregation::setValueMissing, valueJSONObject,
			"missing");
		weightedAvgAggregation.setValueScript(
			_scriptConverter.toScript(valueJSONObject.get("script")));

		_setObject(
			weightedAvgAggregation::setWeightMissing, weightJSONObject,
			"missing");
		weightedAvgAggregation.setWeightScript(
			_scriptConverter.toScript(weightJSONObject.get("script")));

		return weightedAvgAggregation;
	}

	private final Aggregations _aggregations;
	private final Map<String, ConvertFunction> _convertFunctions =
		new HashMap<>();
	private final GeoBuilders _geoBuilders;
	private HighlightConverter _highlightConverter;
	private final ScriptConverter _scriptConverter;
	private final Scripts _scripts;
	private final Sorts _sorts;

}