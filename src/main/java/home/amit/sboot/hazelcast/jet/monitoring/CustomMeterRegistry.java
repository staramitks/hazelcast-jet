//package home.amit.sboot.hazelcast.jet.monitoring;
///*
//User :- AmitSingh
//Date :- 5/9/2024
//Time :- 3:44 PM
//Year :- 2024
//*/
//
//import io.micrometer.core.instrument.*;
//import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
//import io.micrometer.core.instrument.distribution.pause.PauseDetector;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.TimeUnit;
//import java.util.function.ToDoubleFunction;
//import java.util.function.ToLongFunction;
//
//@Component("meterRegistry")
//public class CustomMeterRegistry extends MeterRegistry {
//    private final ConcurrentMap<Meter.Id, CustomCounter> counters = new ConcurrentHashMap<>();
//
//    public CustomMeterRegistry(Clock clock) {
//        super(clock);
//    }
//
//    @Override
//    protected <T> Gauge newGauge (Meter.Id id, T t, ToDoubleFunction<T> toDoubleFunction) {
//        return null;
//    }
//
//    @Override
//    protected Counter newCounter(Meter.Id id) {
//        CustomCounter customCounter = new CustomCounter(id);
//        counters.put(id, customCounter);
//        return customCounter;
//    }
//
//    @Override
//    protected Timer newTimer (Meter.Id id, DistributionStatisticConfig distributionStatisticConfig, PauseDetector pauseDetector) {
//        return null;
//    }
//
//    @Override
//    protected DistributionSummary newDistributionSummary (Meter.Id id, DistributionStatisticConfig distributionStatisticConfig, double v) {
//        return null;
//    }
//
//    @Override
//    protected Meter newMeter (Meter.Id id, Meter.Type type, Iterable<Measurement> iterable) {
//        return null;
//    }
//
//    @Override
//    protected <T> FunctionTimer newFunctionTimer (Meter.Id id, T t, ToLongFunction<T> toLongFunction, ToDoubleFunction<T> toDoubleFunction, TimeUnit timeUnit) {
//        return null;
//    }
//
//    @Override
//    protected <T> FunctionCounter newFunctionCounter (Meter.Id id, T t, ToDoubleFunction<T> toDoubleFunction) {
//        return null;
//    }
//
//    @Override
//    protected TimeUnit getBaseTimeUnit () {
//        return null;
//    }
//
//    @Override
//    protected DistributionStatisticConfig defaultHistogramConfig () {
//        return null;
//    }
//
//    // Implement other methods as needed
//
//    // Method to set a specific value in the custom counter
//    public void setCounterValue(Meter.Id id, double value) {
//        CustomCounter customCounter = counters.get(id);
//        if (customCounter != null) {
//            customCounter.setValue(value);
//        }
//    }
//
//    // Method to get the value of a custom counter
//    public double getCounterValue(Meter.Id id) {
//        CustomCounter customCounter = counters.get(id);
//        return customCounter != null ? customCounter.getValue() : 0.0;
//    }
//}