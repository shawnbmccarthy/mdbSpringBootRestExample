package org.mdb.rest.testers.flat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTester {
    private static String host = "http://localhost:8080//api/v3/monthly_flat";
    private static String fromDateTime = "?fromDateTime=2016-11-01T00:00:00Z";
    private static String toDateTime = "&toDateTime=2018-11-02T00:00:00Z";
    private static String userId = "&userId=";

    private static String[] users = {
            "A.Abbott", "A.Adanto", "A.Anderson", "A.Andrews", "A.Angelini", "A.Ashton", "A.Baird", "A.Barker",
            "A.Barlowe", "A.Barnabas", "A.Barton", "A.Battes", "A.Baxter", "A.Bell", "A.Belski", "A.Bering",
            "A.Bernardo", "A.Bishop", "A.Bismark", "A.Blake", "A.Bowers", "A.Brock", "A.Brunsky", "A.Bukowski",
            "A.Busecki", "A.Bush", "A.Byck", "A.Caan", "A.Calder", "A.Canning", "A.Carlson", "A.Carsen", "A.Carson",
            "A.Carter", "A.Cassell", "A.Cedolia", "A.Chambers", "A.Chow", "A.Cillian", "A.Clark", "A.Cobb", "A.Cole",
            "A.Whiticus", "A.Whitman", "A.Wyatt", "A.Young", "A.Zimmer", "B.Abbott", "B.Adanto", "B.Anderson",
            "w.Flores", "w.Freitag", "w.Garity", "w.Gilmer", "w.Glazer", "w.Glenn", "w.Green", "w.Gregor", "w.Griff",
            "w.Gross", "w.Haberman", "w.Harlow", "w.Hernandez", "w.Hewlett", "w.Hopper", "w.Houk", "w.Hughes", "w.Jackson",
            "w.James", "w.Jardin", "w.Jinks", "w.Johnson", "w.Kosan", "w.Krueger", "w.Larsen", "w.Liddell", "w.Lockhart",
            "w.Logan", "w.Madden", "w.Manlius", "w.Martin", "w.Martino", "w.Marzotto", "w.McNeal", "w.McShane", "w.Meyer", "w.Miller",
            "w.Mohr", "w.Monroe", "w.Morgan", "w.Munroe", "w.Napier", "w.Nelson", "w.Nielson", "w.Osbourne", "w.Pak",
            "w.Perkins", "w.Powell", "w.Radnor", "w.Raynes", "w.Resnick", "w.Rice", "w.Robertson", "w.Roth",
            "w.Sawyer", "w.Seinfeld", "w.Shreve", "w.Simmons", "w.Smith", "w.Soliday", "w.St.Clair", "w.Stark",
            "w.Steinbruck", "w.Stukowski", "w.Suenos", "w.Sykes", "w.Taggart", "w.Taylor", "w.Tivoli", "w.Valda", "w.Varley",
            "w.Wallace", "w.Watts", "w.Well", "w.Wells", "w.Westford", "w.Wheeler", "w.Whiticus", "w.Whitman", "w.Young"
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