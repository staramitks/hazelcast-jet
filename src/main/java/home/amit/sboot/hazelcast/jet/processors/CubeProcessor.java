package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 2:02 PM
Year :- 2024
*/

import com.hazelcast.jet.core.AbstractProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CubeProcessor extends AbstractProcessor {

    private final ExecutorService executor;

    @Override
    protected void init(Context context) throws Exception{
        super.init(context);
    }

    public CubeProcessor () {
        this.executor = Executors.newFixedThreadPool(2);
    }

//    public CubeProcessor(ExecutorService executor) {
//        this.executor = executor;
//    }

    @Override
    public boolean isCooperative(){
        return false;
    }


    @Override
    protected boolean tryProcess(int ordinal, @NonNull Object item) {
        Integer number=(Integer)item;
        AtomicInteger output= new AtomicInteger();
        CompletableFuture<Integer> future=CompletableFuture.supplyAsync(() -> number*number*number, executor);
        future.thenAccept((result)->{
            tryRelease(result);
        });
        log.info("Processing for {}  ",number);
        return true;
    }

    private boolean tryRelease(Object item){
        log.info("Cube Emitting val {} ",item);
        return  tryEmit(item);
    }
}
