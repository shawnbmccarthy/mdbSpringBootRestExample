package org.mdb.rest.testers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTester {
    private static String host = "http://localhost:8080/nnas";
    private static String userId = "?userId=e.Tom";
    private static String fromDateTime = "&fromDateTime=2016-11-01T00:00:00Z";
    private static String toDateTime = "&toDateTime=2018-11-02T00:00:00Z";
    private static String dims = "&dimensions=ACCT_TYPE";

    public static void main(String [] args) throws Exception {
        int t = Integer.parseInt(args[0]);

        ExecutorService e = Executors.newFixedThreadPool(t);
        String u = host + userId + fromDateTime + toDateTime + dims;
        while(true){
            Runnable worker = new Worker((HttpURLConnection) new URL(u).openConnection());
            e.execute(worker);
        }
    }
}