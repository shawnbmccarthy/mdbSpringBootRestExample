package org.mdb.rest.testers.flat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTester {
    private static String host = "http://localhost:8080//api/v3/monthly_flat";
    private static String fromDateTime = "?fromDateTime=2016-11-01T00:00:00Z";
    private static String toDateTime = "&toDateTime=2018-11-02T00:00:00Z";
    private static String userId = "&userId=";

    private static String[] users = {
            "J.Fox", "N.Gillian", "C.Crowley", "W.Smoller", "G.Malone", "S.Tager"
    };

    public static void main(String [] args) throws Exception {
        int t = Integer.parseInt(args[0]);
        int l = Integer.parseInt(args[1]);
        ExecutorService e = Executors.newFixedThreadPool(t);
        String u = host + fromDateTime + toDateTime;
        int i = 0;
        int j = 0;
        long start = System.currentTimeMillis();

        while(j <= l){
            if(i >= users.length){
                i = 0;
            }
            Runnable worker = new Worker(u + userId + users[i]);
            e.execute(worker);
        }
        e.shutdown();
        long end = System.currentTimeMillis();

        System.out.println("ran " + l + " on thread pool of size " + t + " in " + (end-start) + "ms (FLAT)" );
    }
}