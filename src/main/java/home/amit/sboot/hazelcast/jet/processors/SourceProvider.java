package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 1:57 PM
Year :- 2024
*/

import com.hazelcast.jet.pipeline.SourceBuilder;

public class SourceProvider {

    private Integer counter = 0;

    public void generate(SourceBuilder.SourceBuffer<Integer> buffer) {
        while (counter.compareTo(10)<0) {
            buffer.add(counter++);
        }
    }
}
