package home.amit.sboot.hazelcast.jet.pipeline;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 1:56 PM
Year :- 2024
*/

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.core.ProcessorSupplier;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.SourceBuilder;
import com.hazelcast.jet.pipeline.StreamSource;
import home.amit.sboot.hazelcast.jet.processors.CubeProcessor;
import home.amit.sboot.hazelcast.jet.processors.SourceProvider;
import home.amit.sboot.hazelcast.jet.processors.SquareProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
@Slf4j
public class JetPipelineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        JetInstance  jet = Jet.newJetInstance();
        Pipeline pipeline = buildPipeline();

        JobConfig jobConfig = streamJobConfig();
        jet.newJobIfAbsent(pipeline,jobConfig);
    }

    public  Pipeline buildPipeline() {
        Pipeline pipeline = Pipeline.create();
        ExecutorService executor = Executors.newFixedThreadPool(4); //
        log.info("Jet Pipeline for Squaring then Cubing numbers");
        // Define source
        StreamSource<Integer> source = SourceBuilder.stream("numbers", ctx -> new SourceProvider())
                .fillBufferFn(SourceProvider::generate)
                .distributed(1)
                .build();

        // Stage 1: Stream numbers from 1 to 100
        pipeline.readFrom(source).withoutTimestamps().peek()
                .customTransform("squaring number", ProcessorSupplier.of(()->new SquareProcessor()))
                .customTransform("Cube number", ProcessorSupplier.of(()->new CubeProcessor()))
                .writeTo(Sinks.logger());

        return pipeline;
    }


    protected JobConfig streamJobConfig(){
        final JobConfig jobConfig= new JobConfig();
        jobConfig.setName("Test JOb");
        jobConfig.setSnapshotIntervalMillis(100);
        jobConfig.setProcessingGuarantee(ProcessingGuarantee.AT_LEAST_ONCE);
        return jobConfig;
    }

}
