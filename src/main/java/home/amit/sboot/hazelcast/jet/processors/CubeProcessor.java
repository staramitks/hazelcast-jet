package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 2:02 PM
Year :- 2024
*/

import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.spring.context.SpringAware;
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

    @Autowired
    private transient ExecutorService executor;

    @Override
    protected void init(Context context) throws Exception{
        super.init(context);
    }


//    public CubeProcessor(ExecutorService executor) {
//        this.executor = executor;
//    }

    public CubeProcessor() {

    }

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
