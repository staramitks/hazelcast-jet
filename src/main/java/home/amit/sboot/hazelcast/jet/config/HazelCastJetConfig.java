package home.amit.sboot.hazelcast.jet.config;
/*
User :- AmitSingh
Date :- 4/21/2024
Time :- 4:49 PM
Year :- 2024
*/

import com.hazelcast.config.Config;
import com.hazelcast.config.MetricsConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ManagedContext;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.spring.context.SpringManagedContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class HazelCastJetConfig {

    @Value("${thread.pool.count:10}")
    private int processorsPool;

    @Bean
    public Config hazelcastConfig(){
        final var config= new Config();
        return config;
    }

    @Bean
    public SpringManagedContext springManagedContext(){return new SpringManagedContext();}
    @Bean
    public JetConfig jetConfig (Config hazelcastConfig, final ManagedContext managedContext)
    {
        hazelcastConfig.setManagedContext(managedContext);
        final MetricsConfig metricsConfig= new MetricsConfig();
        metricsConfig.setEnabled(false);
        final JetConfig jetConfig= new JetConfig();
        jetConfig.setHazelcastConfig(hazelcastConfig);
        return jetConfig;

    }

    @Bean
    public JetInstance jetInstance(JetConfig jetConfig)
    {
        JetInstance jetInstance= Jet.newJetInstance(jetConfig);
        return jetInstance;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(JetInstance jetInstance)
    {
        return jetInstance.getHazelcastInstance();
    }

    @Bean
    public ExecutorService executors(){
        return Executors.newFixedThreadPool(processorsPool);
    }
}
