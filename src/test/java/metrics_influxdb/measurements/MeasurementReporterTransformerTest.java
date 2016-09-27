package metrics_influxdb.measurements;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import metrics_influxdb.SortedMaps;
import metrics_influxdb.api.measurements.MetricMeasurementTransformer;

public class MeasurementReporterTransformerTest {
		private Sender sender;
		private MetricMeasurementTransformer transformer;
		private Measure mockMeasure;

		private MeasurementReporter reporter;

		@BeforeMethod
		public void setup() {
				sender = mock(Sender.class);
				transformer = mock(MetricMeasurementTransformer.class);
				mockMeasure = mock(Measure.class);

				when(transformer.measurement(any(Measure.class))).thenReturn(mockMeasure);

				reporter = new MeasurementReporter(sender,  mock(MetricRegistry.class), null, TimeUnit.SECONDS,
																															 TimeUnit.MILLISECONDS, Clock.defaultClock(),
																															 Collections.emptyMap(),
																															 transformer);
		}

		@Test
		public void sendsGaugesThroughTransformer() {
				Gauge gauge = mock(Gauge.class, RETURNS_DEEP_STUBS);
				reporter.report(
								SortedMaps.singleton("metric", gauge),
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								Collections.emptySortedMap()
				);

				verify(transformer).measurement(any(Measure.class));
				verify(sender).send(mockMeasure);
		}

		@Test
		public void sendsCountersThroughTransformer() {
				Counter counter = mock(Counter.class, RETURNS_DEEP_STUBS);
				reporter.report(
								Collections.emptySortedMap(),
								SortedMaps.singleton("metric", counter),
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								Collections.emptySortedMap()
				);

				verify(transformer).measurement(any(Measure.class));
				verify(sender).send(mockMeasure);
		}

		@Test
		public void sendsHistogramsThroughTransformer() {
				Histogram histo = mock(Histogram.class, RETURNS_DEEP_STUBS);
				reporter.report(
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								SortedMaps.singleton("metric", histo),
								Collections.emptySortedMap(),
								Collections.emptySortedMap()
				);

				verify(transformer).measurement(any(Measure.class));
				verify(sender).send(mockMeasure);
		}

		@Test
		public void sendsMetersThroughTransformer() {
				Meter meter = mock(Meter.class, RETURNS_DEEP_STUBS);
				reporter.report(
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								SortedMaps.singleton("metric", meter),
								Collections.emptySortedMap()
				);

				verify(transformer).measurement(any(Measure.class));
				verify(sender).send(mockMeasure);
		}

		@Test
		public void sendsTimersThroughTransformer() {
				Timer timer = mock(Timer.class, RETURNS_DEEP_STUBS);
				reporter.report(
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								Collections.emptySortedMap(),
								SortedMaps.singleton("metric", timer)
				);

				verify(transformer).measurement(any(Measure.class));
				verify(sender).send(mockMeasure);
		}

}
