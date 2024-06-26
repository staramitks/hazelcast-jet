package home.amit.sboot.hazelcast.jet.runner;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 1:56 PM
Year :- 2024
*/

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.core.DAG;
import com.hazelcast.jet.core.Edge;
import com.hazelcast.jet.core.ProcessorSupplier;
import com.hazelcast.jet.core.Vertex;
import home.amit.sboot.hazelcast.jet.processors.NumberPrinterSink;
import home.amit.sboot.hazelcast.jet.processors.SourceProviderProcessor;
import home.amit.sboot.hazelcast.jet.processors.SquareProcessor;
import home.amit.sboot.hazelcast.jet.processors.ThrottleProcessor;
import home.amit.sboot.hazelcast.jet.utils.NamesProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//@Component
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
        DAG dag = getDagPipeline();
        JobConfig jobConfig = streamJobConfig();
        Job job=jetInstance.newJob(dag,jobConfig);
        job.getMetrics();

    }

    public  DAG getDagPipeline() {

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
        jobConfig.setName(NamesProvider.getStreamDAGJobName("square"));
        jobConfig.setSnapshotIntervalMillis(100);
        jobConfig.setProcessingGuarantee(ProcessingGuarantee.AT_LEAST_ONCE);
        jobConfig.setMetricsEnabled(true);
        return jobConfig;
    }

}
