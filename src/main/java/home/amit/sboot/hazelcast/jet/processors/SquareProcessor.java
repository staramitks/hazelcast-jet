package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 2:01 PM
Year :- 2024
*/

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.spring.context.SpringAware;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistry;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistrySingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@SpringAware
public class SquareProcessor extends AbstractProcessor implements Serializable {

    private IAtomicLong processCounter;

    private final AtomicReference<Long> gaugeValue = new AtomicReference<>(0l);

    @Autowired
    private  transient ExecutorService executor;

    private String name;


    private transient HazelcastMeterRegistry hazelcastMeterRegistry;


    AtomicLong myGauge;


    public SquareProcessor () {

    }


    public SquareProcessor (String name) {
        this.name=name;
    }


    @Override
    protected void init(Context context) throws Exception{
        final HazelcastInstance hazelcastInstance= context.jetInstance().getHazelcastInstance();
        HazelcastMeterRegistry hazelcastMeterRegistry = HazelcastMeterRegistrySingleton.getInstance(hazelcastInstance);
        this.hazelcastMeterRegistry=hazelcastMeterRegistry;
        this.processCounter=hazelcastInstance.getCPSubsystem().getAtomicLong(this.getClass().getSimpleName()+".process.success");
       // myGauge = meterRegistry.gauge(this.getClass().getSimpleName()+".meter.success", new AtomicLong(0l));
        super.init(context);
    }


    @Override
    public boolean isCooperative () {
        return true;
    }

    @Override
    protected boolean tryProcess (int ordinal, @NonNull Object item) {

        Integer number = (Integer) item;
        CompletableFuture<Integer> future=CompletableFuture.supplyAsync(() ->number, executor);
        future.thenAccept((result)->{
            tryRelease(result);
        });
        log.info("Processing for {}  ",number);
        return true;
    }

    private synchronized boolean tryRelease(Object item){
        log.info("Square Emitting val {} ",item);
        boolean isSuccess= tryEmit(item);
        if (isSuccess)
        {
            Integer obj=(Integer) item;
//            this.processCounter.incrementAndGet();
//            if (obj%2==0) {
                this.hazelcastMeterRegistry.incrementCounter(this.getClass().getSimpleName() + ".process.success");
//            }
            log.info("Getting value for Custom Meter Registry SquareProcessor {} ",this.hazelcastMeterRegistry.get(this.getClass().getSimpleName()+".process.success"));
            log.info("Emitted {} from {} ",item, this.getClass().getName());
        }
        else
        {
           // meterRegistry.counter(name+"-failure").increment();
            log.info("Failed to emit {} from {} ",item,this.getClass().getName());
        }
        return isSuccess;
    }


}