package org.mdb.rest.testers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTester {
    private static String host = "http://localhost:8080/nnas";
    private static String fromDateTime = "?fromDateTime=2016-11-01T00:00:00Z";
    private static String toDateTime = "&toDateTime=2018-11-02T00:00:00Z";
    private static String dims = "&dimensions=ACCT_TYPE";
    private static String userId = "&userId=";

    private static String[] users = {
            "a.Adanto", "a.Andrews", "a.Barnabas", "u.Harris", "a.Bering", "a.Brock", "a.Brunsky", "a.Caan",
            "a.Calder", "a.Carter", "a.Cole", "a.Conway", "a.Couric", "a.Curie", "a.Curtis", "i.Wheeler", "e.Henjik",
            "a.Diamond", "a.Dillon", "a.Donovan", "d.West", "w.Larsen", "e.Bowers", "e.Munroe", "o.Carter", "z.Jones",
            "a.Duke", "a.Finn", "a.Garity", "u.Miller", "u.Golden", "o.Letanik", "o.Bismark", "o.Carter", "o.Fargo",
            "o.Gilmer", "a.Gross", "a.Haberman", "a.Harrington", "a.Hernandez", "a.Hughes", "a.Jackson", "a.Jardin",
            "a.Kinser", "a.Lattimer", "a.Lauer", "a.Leonardo", "a.Lewis", "a.Lockhart", "a.Logan", "a.Lovejoy",
            "a.MacPherson", "a.Mallow", "a.Malloy", "a.Miller", "a.Newell", "a.Perkins", "a.Preston", "a.Raynes",
            "a.Resnick", "a.Roth", "a.Sawyer", "a.Smith", "a.Stefano", "a.Steinbruck", "a.Stone"
    };

    public static void main(String [] args) throws Exception {
        int t = Integer.parseInt(args[0]);
        int l = Integer.parseInt(args[1]);
        ExecutorService e = Executors.newFixedThreadPool(t);
        String u = host + fromDateTime + toDateTime + dims;
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

        System.out.println("ran " + l + " on thread pool of size " + t + " in " + (end-start) + "ms" );
    }
}