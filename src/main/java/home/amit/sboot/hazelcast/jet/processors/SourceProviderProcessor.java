package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/26/2024
Time :- 11:59 AM
Year :- 2024
*/

import com.hazelcast.jet.core.AbstractProcessor;

public class SourceProviderProcessor extends AbstractProcessor {
    private int currentNumber = 1;

    @Override
    public boolean tryProcess () {
        if (currentNumber <= 1000) {
            return tryEmit(currentNumber++);
        } else {
            return false; // No more numbers to emit
        }
    }


}