package home.amit.sboot.hazelcast.jet.utils;
/*
User :- AmitSingh
Date :- 5/26/2024
Time :- 11:42 AM
Year :- 2024
*/

public class NamesProvider {

    public static String getStreamJobName(String jobName){
        return String.join("-",jobName,"stream");
    }

    public static String getStreamDAGJobName(String jobName){
        return String.join("-",jobName,"dag-stream");
    }
}
