package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 1:57 PM
Year :- 2024
*/

import com.hazelcast.jet.pipeline.SourceBuilder;
import com.hazelcast.spring.context.SpringAware;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringAware
public class SourceProvider {

    @Autowired
    private transient HazelcastMeterRegistry meterRegistry;

    AtomicLong sourceGauge;

    private Integer counter = 0;

    public void generate(SourceBuilder.SourceBuffer<Integer> buffer) {
        while (counter.compareTo(1000)<0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buffer.add(counter++);
            this.meterRegistry.incrementCounter(this.getClass().getSimpleName() + ".process.success");
            log.info("Source Added {} ",counter);
        }
    }
}
