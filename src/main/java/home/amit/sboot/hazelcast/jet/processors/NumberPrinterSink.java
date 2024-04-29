package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/26/2024
Time :- 12:20 PM
Year :- 2024
*/

import com.hazelcast.jet.core.AbstractProcessor;

public class NumberPrinterSink extends AbstractProcessor {
    @Override
    protected boolean tryProcess(int ordinal, Object item) {
        System.out.println("Received number: " + item);
        return true; // Continue processing
    }
}