package org.mdb.rest.testers.flat;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTester {
    private static String host = "http://localhost:8080//api/v3/monthly_flat";
    private static String fromDateTime = "?fromDateTime=2016-11-01T00:00:00Z";
    private static String toDateTime = "&toDateTime=2018-11-02T00:00:00Z";
    private static String userId = "&userId=";

    private static String[] users = {
            "e.Bukowski", "o.Sweetwood", "c.Mohr", "i.Wheeler", "e.Henjik",
            "d.West", "w.Larsen", "e.Bowers", "e.Munroe", "o.Carter", "z.Jones",
            "u.Miller", "u.Golden", "o.Letanik", "o.Bismark", "o.Carter", "o.Fargo",
            "o.Gilmer"
    };

    public static void main(String [] args) throws Exception {
        int t = Integer.parseInt(args[0]);

        ExecutorService e = Executors.newFixedThreadPool(t);
        String u = host + fromDateTime + toDateTime;
        int i = 0;
        while(true){
            if(i >= users.length){
                i = 0;
            }
            Runnable worker = new Worker((HttpURLConnection) new URL(u + userId + users[i]).openConnection());
            e.execute(worker);
        }
    }
}