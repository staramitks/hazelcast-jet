package home.amit.sboot.hazelcast.jet.pipeline.filterpipeline;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 2:45 PM
Year :- 2024
*/

import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.test.TestSources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class BasicFilterPipeline implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        log.info("invoking BasicFilterPipeline");
        Pipeline p = Pipeline.create();
        p.readFrom(TestSources.itemStream(10))
                .withoutTimestamps()
                .filter(event -> event.sequence() % 2 == 0)
                .setName("filter out odd numbers")
                .peek()
                .writeTo(Sinks.logger());
    }
}
