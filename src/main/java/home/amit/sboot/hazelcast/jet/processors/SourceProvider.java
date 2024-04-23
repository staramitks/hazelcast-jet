package home.amit.sboot.hazelcast.jet.processors;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 1:57 PM
Year :- 2024
*/

import com.hazelcast.jet.pipeline.SourceBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SourceProvider {

    private Integer counter = 0;

    public void generate(SourceBuilder.SourceBuffer<Integer> buffer) {
        while (counter.compareTo(1000)<0) {
            buffer.add(counter++);
            log.info("Source Added {} ",counter);
        }
    }
}
