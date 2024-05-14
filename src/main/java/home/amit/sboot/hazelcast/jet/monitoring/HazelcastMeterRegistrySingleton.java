package home.amit.sboot.hazelcast.jet.monitoring;
/*
User :- AmitSingh
Date :- 5/10/2024
Time :- 6:21 PM
Year :- 2024
*/

import com.hazelcast.core.HazelcastInstance;

public class HazelcastMeterRegistrySingleton {
    private static HazelcastMeterRegistry instance;
    private static final Object lock = new Object();

    private HazelcastMeterRegistrySingleton() {
        // Private constructor to prevent instantiation
    }

    public static HazelcastMeterRegistry getInstance(HazelcastInstance hazelcastInstance) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new HazelcastMeterRegistry(hazelcastInstance);
                }
            }
        }
        return instance;
    }
}