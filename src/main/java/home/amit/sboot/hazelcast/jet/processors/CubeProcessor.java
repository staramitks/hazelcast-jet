package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 2:02 PM
Year :- 2024
*/

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.spring.context.SpringAware;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistry;
import home.amit.sboot.hazelcast.jet.monitoring.HazelcastMeterRegistrySingleton;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@SpringAware
@Slf4j
public class CubeProcessor extends AbstractProcessor implements Serializable {

    private IAtomicLong processCounter;

    @Autowired
    private transient ExecutorService executor;

    private transient HazelcastMeterRegistry hazelcastMeterRegistry;

    @Override
    protected void init(Context context) throws Exception{
        final HazelcastInstance hazelcastInstance= context.jetInstance().getHazelcastInstance();
        HazelcastMeterRegistry hazelcastMeterRegistry = HazelcastMeterRegistrySingleton.getInstance(hazelcastInstance);
        this.hazelcastMeterRegistry=hazelcastMeterRegistry;
        super.init(context);
    }

    public CubeProcessor() {

    }

    @Override
    public boolean isCooperative(){
        return true;
    }


    @Override
    protected boolean tryProcess(int ordinal, @NonNull Object item) {
        Integer number=(Integer)item;
        AtomicInteger output= new AtomicInteger();
        CompletableFuture<Integer> future=CompletableFuture.supplyAsync(() -> number, executor);
        future.thenAccept((result)->{
            tryRelease(result);
        });
        log.info("Processing for {}  ",number);
        return true;
    }

    private synchronized boolean  tryRelease(Object item){
        log.info("Cube Emitting val {} ",item);
        boolean isSuccess= tryEmit(item);
        if (isSuccess)
        {
//            log.info("Getting value for HAZ Atomic Reference {} ",this.processCounter.get());
            this.hazelcastMeterRegistry.incrementCounter(this.getClass().getSimpleName());
            log.info("Getting value for Custom Meter Registry CubeProcessor {} ",this.hazelcastMeterRegistry.get(this.getClass().getSimpleName()+".process.success"));
            log.info("Emitted {} from {} ",item, this.getClass().getName());
        }
        else
        {
            log.info("Failed to emit {} from {} ",item,this.getClass().getName());
        }
        return isSuccess;
    }

}
