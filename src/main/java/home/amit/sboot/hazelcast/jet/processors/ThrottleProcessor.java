package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/26/2024
Time :- 11:51 AM
Year :- 2024
*/

import com.hazelcast.jet.core.AbstractProcessor;

import java.util.concurrent.TimeUnit;

public class ThrottleProcessor<T> extends AbstractProcessor {
    private final long nanosBetweenItems;
    private long lastProcessingTime = 0;

    public ThrottleProcessor (int maxItemsPerSecond) {
        this.nanosBetweenItems = TimeUnit.SECONDS.toNanos(1) / maxItemsPerSecond;
    }

    @Override
    protected boolean tryProcess(int ordinal, Object item) {
        long currentTime = System.nanoTime();
        long waitTime = nanosBetweenItems - (currentTime - lastProcessingTime);

        if (waitTime > 0) {
            try {
                TimeUnit.NANOSECONDS.sleep(waitTime);
            } catch (InterruptedException e) {
                // Handle interruption if needed
                e.printStackTrace();
            }
        }

        lastProcessingTime = System.nanoTime();
        return tryEmit(item);
    }
}
