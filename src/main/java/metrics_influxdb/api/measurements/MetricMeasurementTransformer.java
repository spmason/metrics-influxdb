package metrics_influxdb.api.measurements;

import java.util.Collections;
import java.util.Map;

import metrics_influxdb.measurements.Measure;

public interface MetricMeasurementTransformer {
	public Map<String, String> tags(String metricName);

	public String measurementName(String metricName);

	public static final MetricMeasurementTransformer NOOP = new MetricMeasurementTransformer() {
		@Override
		public Map<String, String> tags(String metricName) {
			return Collections.emptyMap();
		}

		@Override
		public String measurementName(String metricName) {
			return metricName;
		}
	};

	default Measure measurement(Measure measure) {
		return measure;
	}
}
