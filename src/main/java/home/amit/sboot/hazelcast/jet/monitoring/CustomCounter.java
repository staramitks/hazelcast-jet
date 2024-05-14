//package home.amit.sboot.hazelcast.jet.monitoring;
///*
//User :- AmitSingh
//Date :- 5/9/2024
//Time :- 3:44 PM
//Year :- 2024
//*/
//
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.cp.IAtomicLong;
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.Measurement;
//import io.micrometer.core.instrument.Meter;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomCounter implements Counter {
//
//    private final HazelcastInstance hazelcastInstance;
//    @Getter
//    private final IAtomicLong atomicLong;
//    private final Meter.Id id;
//
//    public CustomCounter(Meter.Id id, HazelcastInstance hazelcastInstance) {
//        this.hazelcastInstance = hazelcastInstance;
//        this.atomicLong = hazelcastInstance.getCPSubsystem().getAtomicLong(id.getName());
//        this.id=id;
//    }
//
//    public long getValue() {
//        return this.atomicLong.get();
//    }
//
//    public void resetValue() {
//        this.atomicLong.set(0l);
//    }
//
//    public Meter.Id getId() {
//        return id;
//    }
//
//    @Override
//    public void increment () {
//        Counter.super.increment();
//        this.atomicLong.incrementAndGet();
//    }
//
//    @Override
//    public void increment (double v) {
//        this.atomicLong.addAndGet((long)v);
//    }
//
//    @Override
//    public double count () {
//        return 0;
//    }
//
//    @Override
//    public Iterable<Measurement> measure () {
//        return Counter.super.measure();
//    }
//}