package home.amit.sboot.hazelcast.jet.rest;
/*
User :- AmitSingh
Date :- 5/8/2024
Time :- 7:45 PM
Year :- 2024
*/

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistry;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistrySingleton;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.Counter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloWorldRestController {


    private HazelcastMeterRegistry meterRegistry;

    @Autowired
    private HazelcastInstance hazelcastInstance;


    @GetMapping("/hello")
    public String helloWorld(){
        meterRegistry.counter("MyApp_AmitSingh_Requests").increment();
        return "Hello World";
    }
    @GetMapping("/test-stats-controller")
    public Map<String,Long> getStats(){
        Map<String,Long> hzMap=new HashMap<>();

        long sqsuccess=hazelcastInstance.getCPSubsystem().getAtomicLong("SquareProcessor.process.success").get();
        long cubesuccess=hazelcastInstance.getCPSubsystem().getAtomicLong("CubeProcessor.process.success").get();

        meterRegistry= HazelcastMeterRegistrySingleton.getInstance(hazelcastInstance);
        ConcurrentMap<Meter.Id, IAtomicLong> counters = meterRegistry.getCounters();
        counters.entrySet().stream().forEach((es)->hzMap.put(es.getKey().getName(), es.getValue().get()));
        return hzMap;

    }

    @GetMapping("/reset-stats")
    public String resetStats(){
        meterRegistry.resetCounters();
        return "Reset done";
    }
}
