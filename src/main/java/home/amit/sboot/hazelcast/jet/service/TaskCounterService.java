//package home.amit.sboot.hazelcast.jet.service;
///*
//User :- AmitSingh
//Date :- 5/8/2024
//Time :- 8:36 PM
//Year :- 2024
//*/
//
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.MeterRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TaskCounterService {
//
//    @Autowired
//    private MeterRegistry meterRegistry;
//
//    @Autowired
//    public TaskCounterService() {
//        this.taskCounter = registry.counter("task_counter");
//    }
//
//    public void incrementTaskCounter() {
//        taskCounter.increment();
//    }
//}