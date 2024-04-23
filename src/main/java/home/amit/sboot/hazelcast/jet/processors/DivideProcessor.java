package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 2:01 PM
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

@Slf4j
@SpringAware
public class DivideProcessor extends AbstractProcessor implements Serializable {

    @Autowired
    private transient ExecutorService executor;

    public DivideProcessor () {

    }

    @Override
    protected void init(Context context) throws Exception{
      super.init(context);
    }


    @Override
    public boolean isCooperative () {
        return true;
    }

    @Override
    protected boolean tryProcess (int ordinal, @NonNull Object item) {

        Integer number = (Integer) item;
        CompletableFuture<Integer> future=CompletableFuture.supplyAsync(() ->(number), executor);
        future.thenAccept((result)->{
            tryRelease(result);
        });
        log.info("Processing for {}  ",number);
        return true;
    }



    private synchronized boolean tryRelease(Object item){
        log.info("UnderDivide Emitting val {} ",item);
        boolean isSuccess= tryEmit(item);
        if (isSuccess)
        {
            log.info("Emitted from {} ",this.getClass().getName());
        }
        else
        {
            log.info("Failed to emit from {} ",this.getClass().getName());
        }
        return isSuccess;
    }


}