package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/26/2024
Time :- 11:59 AM
Year :- 2024
*/

import com.hazelcast.jet.core.AbstractProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

@Slf4j
public class FilterProcessor extends AbstractProcessor {

    @Override
    public boolean tryProcess (int ordinal , @NonNull Object item) {
        // only emit numbers which are in the multiples of 5
        Integer currentNumber=(Integer) item;
        if (currentNumber %5 ==0) {
            log.info(" Emitting {} ",currentNumber);
            return tryEmit(currentNumber);
        }
        return true;
    }


}