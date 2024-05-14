package home.amit.sboot.hazelcast.jet.runner;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 1:56 PM
Year :- 2024
*/

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.core.ProcessorSupplier;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.SourceBuilder;
import com.hazelcast.jet.pipeline.StreamSource;
import home.amit.sboot.hazelcast.jet.processors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class JetPipelineRunner implements CommandLineRunner , Serializable {

    @Autowired
    private transient ApplicationContext applicationContext;

    @Autowired
    private JetInstance jetInstance;

    @Autowired
    private JetConfig jetConfig;

    @Autowired
    private ExecutorService executors;

    @Override
    public void run(String... args) {
        Pipeline pipeline = buildPipeline();
        JobConfig jobConfig = streamJobConfig();
        jetInstance.newJobIfAbsent(pipeline,jobConfig);
    }

    public  Pipeline buildPipeline() {
        Pipeline pipeline = Pipeline.create();

       // ExecutorService executor = Executors.newFixedThreadPool(4); //
        log.info("Jet Pipeline for Squaring then Cubing numbers");
        // Define source
        StreamSource<Integer> source = SourceBuilder.stream("numbers", ctx -> new SourceProvider())
                .fillBufferFn(SourceProvider::generate)
                .distributed(1)
                .build();

        // Stage 1: Stream numbers from 1 to 100
        pipeline.readFrom(source).withoutTimestamps().peek()
                .customTransform("apply-throttle",ProcessorSupplier.of(()->new ThrottleProcessor<Integer>(2)))
                .customTransform("squaring number", ProcessorSupplier.of(()->new SquareProcessor("SquareProcessor")))
                .customTransform("Under squaring number", ProcessorSupplier.of(()->new DivideProcessor()))
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
