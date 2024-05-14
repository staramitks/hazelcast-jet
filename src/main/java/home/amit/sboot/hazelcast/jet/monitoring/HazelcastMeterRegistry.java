package home.amit.sboot.hazelcast.jet.monitoring;
/*
User :- AmitSingh
Date :- 5/10/2024
Time :- 11:35 AM
Year :- 2024
*/

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class HazelcastMeterRegistry extends SimpleMeterRegistry {
    private final HazelcastInstance hazelcastInstance;
    @Getter
    private final ConcurrentMap<Meter.Id, IAtomicLong> counters = new ConcurrentHashMap<>();

    public HazelcastMeterRegistry(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public void  incrementCounter(String counterName) {
        Meter.Id id = new Meter.Id(counterName, Tags.empty(), null, null, Meter.Type.COUNTER);
        counters.computeIfAbsent(id, k -> hazelcastInstance.getCPSubsystem().getAtomicLong(counterName)).incrementAndGet();
    }

    public void resetCounter(String counterName) {
        Meter.Id id = new Meter.Id(counterName, Tags.empty(), null, null, Meter.Type.COUNTER);
        counters.computeIfAbsent(id, k -> hazelcastInstance.getCPSubsystem().getAtomicLong(counterName)).set(0l);
    }
    public void resetCounters() {
        for (Map.Entry<Meter.Id, IAtomicLong> entry : counters.entrySet())
        {
            entry.getValue().set(0);
        }

    }

}
