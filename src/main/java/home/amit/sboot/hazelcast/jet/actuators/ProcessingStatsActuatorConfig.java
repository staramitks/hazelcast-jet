package home.amit.sboot.hazelcast.jet.actuators;
/*
User :- AmitSingh
Date :- 5/9/2024
Time :- 1:34 PM
Year :- 2024
*/

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id="test-stats")
public class ProcessingStatsActuatorConfig {

    @Autowired
    private MeterRegistry meterRegistry;

    @ReadOperation
    public Map<String,Long> getStats(){
        Collection<Meter> allMeters = meterRegistry.getMeters();
        Map<String, Long> gaugeMap = new HashMap<>();
        // Filter out gauge meters
        allMeters.stream()
                .filter(meter -> meter instanceof io.micrometer.core.instrument.Gauge)
                .forEach(gauge -> {
                    io.micrometer.core.instrument.Gauge gaugeMetric = (io.micrometer.core.instrument.Gauge) gauge;
                    System.out.println("Gauge Name: " + gaugeMetric.getId().getName());
                    System.out.println("Gauge Value: " + gaugeMetric.value());
                    gaugeMap.put(gaugeMetric.getId().getName(), (long) gaugeMetric.value());
                });
        gaugeMap.forEach((name, value) -> System.out.println(name + ": " + value));
        return gaugeMap;
    }
}
