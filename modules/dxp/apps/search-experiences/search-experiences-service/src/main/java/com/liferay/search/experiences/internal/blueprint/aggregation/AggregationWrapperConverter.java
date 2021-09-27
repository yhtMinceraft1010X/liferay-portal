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
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.DateRangeAggregation;
import com.liferay.portal.search.aggregation.bucket.DiversifiedSamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FiltersAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoDistanceAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoHashGridAggregation;
import com.liferay.portal.search.aggregation.bucket.GlobalAggregation;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.MissingAggregation;
import com.liferay.portal.search.aggregation.bucket.NestedAggregation;
import com.liferay.portal.search.aggregation.bucket.Order;
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
import com.liferay.portal.search.script.Script;
import com.liferay.search.experiences.internal.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.internal.blueprint.script.ScriptConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class AggregationWrapperConverter {

	public AggregationWrapperConverter(
		Aggregations aggregations, ScriptConverter scriptConverter) {

		_aggregations = aggregations;
		_scriptConverter = scriptConverter;

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
	}

	public AggregationWrapper toAggregationWrapper(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData,
		String type) {

		ConvertFunction convertFunction = _convertFunctions.get(type);

		if (convertFunction == null) {
			throw new IllegalArgumentException("Uknown aggregation " + type);
		}

		try {
			Object object = convertFunction.apply(
				jsonObject, name, sxpParameterData);

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

		public Object apply(
				JSONObject jsonObject, String name,
				SXPParameterData sxpParameterData)
			throws Exception;

	}

	private AvgAggregation _toAvgAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private AvgBucketPipelineAggregation _toAvgBucketPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private BucketScriptPipelineAggregation _toBucketScriptPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private BucketSelectorPipelineAggregation
		_toBucketSelectorPipelineAggregation(
			JSONObject jsonObject, String name,
			SXPParameterData sxpParameterData) {

		return null;
	}

	private BucketSortPipelineAggregation _toBucketSortPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private CardinalityAggregation _toCardinalityAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private CumulativeSumPipelineAggregation
		_toCumulativeSumPipelineAggregation(
			JSONObject jsonObject, String name,
			SXPParameterData sxpParameterData) {

		return null;
	}

	private DateHistogramAggregation _toDateHistogramAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		DateHistogramAggregation dateHistogramAggregation =
			_aggregations.dateHistogram(name, jsonObject.getString("field"));

		if (jsonObject.has("date_histogram_interval")) {
			dateHistogramAggregation.setDateHistogramInterval(
				jsonObject.getString("date_histogram_interval"));
		}

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

		if (jsonObject.has("keyed")) {
			dateHistogramAggregation.setKeyed(jsonObject.getBoolean("keyed"));
		}

		if (jsonObject.has("min_doc_count")) {
			dateHistogramAggregation.setMinDocCount(
				jsonObject.getLong("min_doc_count"));
		}

		if (jsonObject.has("missing")) {
			dateHistogramAggregation.setMissing(
				jsonObject.getString("missing"));
		}

		if (jsonObject.has("offset")) {
			dateHistogramAggregation.setMinDocCount(
				jsonObject.getLong("offset"));
		}

		JSONObject orderJSONObject = jsonObject.getJSONObject("order");

		if (orderJSONObject != null) {
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

			dateHistogramAggregation.addOrders(orders.toArray(new Order[0]));
		}

		Script script = _scriptConverter.toScript(jsonObject);

		if (script != null) {
			dateHistogramAggregation.setScript(script);
		}

		return dateHistogramAggregation;
	}

	private DateRangeAggregation _toDateRangeAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private DerivativePipelineAggregation _toDerivativePipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private DiversifiedSamplerAggregation _toDiversifiedSamplerAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private ExtendedStatsAggregation _toExtendedStatsAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private ExtendedStatsBucketPipelineAggregation
		_toExtendedStatsBucketPipelineAggregation(
			JSONObject jsonObject, String name,
			SXPParameterData sxpParameterData) {

		return null;
	}

	private FilterAggregation _toFilterAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private FiltersAggregation _toFiltersAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private GeoBoundsAggregation _toGeoBoundsAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private GeoCentroidAggregation _toGeoCentroidAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private GeoDistanceAggregation _toGeoDistanceAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private GeoHashGridAggregation _toGeoHashGridAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private GlobalAggregation _toGlobalAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private HistogramAggregation _toHistogramAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private MaxAggregation _toMaxAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private MaxBucketPipelineAggregation _toMaxBucketPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private MinAggregation _toMinAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private MinBucketPipelineAggregation _toMinBucketPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private MissingAggregation _toMissingAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private MovingFunctionPipelineAggregation
		_toMovingFunctionPipelineAggregation(
			JSONObject jsonObject, String name,
			SXPParameterData sxpParameterData) {

		return null;
	}

	private NestedAggregation _toNestedAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private PercentileRanksAggregation _toPercentileRanksAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private PercentilesAggregation _toPercentilesAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private PercentilesBucketPipelineAggregation
		_toPercentilesBucketPipelineAggregation(
			JSONObject jsonObject, String name,
			SXPParameterData sxpParameterData) {

		return null;
	}

	private RangeAggregation _toRangeAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private ReverseNestedAggregation _toReverseNestedAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private SamplerAggregation _toSamplerAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private ScriptedMetricAggregation _toScriptedMetricAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private SerialDiffPipelineAggregation _toSerialDiffPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private SignificantTermsAggregation _toSignificantTermsAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private SignificantTextAggregation _toSignificantTextAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private StatsAggregation _toStatsAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private StatsBucketPipelineAggregation _toStatsBucketPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private SumAggregation _toSumAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private SumBucketPipelineAggregation _toSumBucketPipelineAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		SumBucketPipelineAggregation sumBucketPipelineAggregation =
			_aggregations.sumBucket(name, jsonObject.getString("buckets_path"));

		if (jsonObject.has("format")) {
			sumBucketPipelineAggregation.setFormat(
				jsonObject.getString("format"));
		}

		String gapPolicy = jsonObject.getString("gap_policy");

		if (Validator.isNotNull(gapPolicy)) {
			sumBucketPipelineAggregation.setGapPolicy(
				GapPolicy.valueOf(StringUtil.toUpperCase(gapPolicy)));
		}

		return sumBucketPipelineAggregation;
	}

	private TermsAggregation _toTermsAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private TopHitsAggregation _toTopHitsAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private ValueCountAggregation _toValueCountAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private WeightedAvgAggregation _toWeightedAvgAggregation(
		JSONObject jsonObject, String name, SXPParameterData sxpParameterData) {

		return null;
	}

	private final Aggregations _aggregations;
	private final Map<String, ConvertFunction> _convertFunctions =
		new HashMap<>();
	private final ScriptConverter _scriptConverter;

}