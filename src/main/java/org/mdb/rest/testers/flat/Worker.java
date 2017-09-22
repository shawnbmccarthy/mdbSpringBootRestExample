package org.mdb.rest.testers.flat;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public final class Worker implements Runnable {

    private final HttpURLConnection connection;

    public Worker(HttpURLConnection c){
        connection = c;
    }


    @Override public void run(){
        try {
            long start = System.currentTimeMillis();
            JSONObject o = (JSONObject) connection.getContent();
            long end = System.currentTimeMillis();
            System.out.println("rc: " + connection.getResponseCode() + ", type: " + connection.getContentType() + " l: "
                    + connection.getContentLength() + ", t: " + (end - start));
        } catch(IOException ioe){
            System.err.println(ioe.getLocalizedMessage());
        }
    }
}
