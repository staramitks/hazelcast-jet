package home.amit.sboot.hazelcast.jet.config;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 3:19 PM
Year :- 2024
*/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class PipelineExecutorsConfig {

    @Value("${thread.pool.count:10}")
    private Integer processorsPool;

    @Bean
    public ExecutorService executors(){
        return Executors.newFixedThreadPool(processorsPool);
    }
}
