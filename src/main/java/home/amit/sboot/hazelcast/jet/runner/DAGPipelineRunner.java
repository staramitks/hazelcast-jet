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
import com.hazelcast.jet.core.DAG;
import com.hazelcast.jet.core.Edge;
import com.hazelcast.jet.core.ProcessorSupplier;
import com.hazelcast.jet.core.Vertex;
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

@Component
@Slf4j
public class DAGPipelineRunner implements CommandLineRunner , Serializable {

    @Autowired
    private transient ApplicationContext applicationContext;

    @Autowired
    private JetInstance jetInstance;

    @Autowired
    private JetConfig jetConfig;


    @Override
    public void run(String... args) {
        DAG dag = dag();
        JobConfig jobConfig = streamJobConfig();
        jetInstance.newJobIfAbsent(dag,jobConfig);

    }

    public  DAG dag() {

        DAG dag = new DAG();
        final Vertex numbersStreamSource=dag.newVertex("numbers-source-stream",ProcessorSupplier.of(()->new SourceProviderProcessor())).localParallelism(1);
        final Vertex throttleSource=dag.newVertex("throttle-source-stream",ProcessorSupplier.of(()->new ThrottleProcessor<Integer>(10000))).localParallelism(1);
        final Vertex squareP=dag.newVertex("Square-stream",ProcessorSupplier.of(()->new SquareProcessor())).localParallelism(1);
        final Vertex numberPrintSinkP=dag.newVertex("Print-Sink",ProcessorSupplier.of(()->new NumberPrinterSink())).localParallelism(1);
        dag.edge(Edge.from(numbersStreamSource).to(throttleSource).distributed().unicast())
                .edge(Edge.from(throttleSource).to(squareP).local().unicast())
                .edge(Edge.from(squareP).to(numberPrintSinkP).local().unicast());

        return dag;
    }


    protected JobConfig streamJobConfig(){
        final JobConfig jobConfig= new JobConfig();
        jobConfig.setName("Test JOb");
        jobConfig.setSnapshotIntervalMillis(100);
        jobConfig.setProcessingGuarantee(ProcessingGuarantee.AT_LEAST_ONCE);
        return jobConfig;
    }

}
