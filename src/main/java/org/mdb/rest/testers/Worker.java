package org.mdb.rest.testers;

import java.io.IOException;
import java.net.HttpURLConnection;

public final class Worker implements Runnable {

    private final HttpURLConnection connection;

    public Worker(HttpURLConnection c){
        connection = c;
    }


    @Override public void run(){
        try {
            System.out.println("rc; " + connection.getResponseMessage());
            Object o = connection.getContent();

        } catch(IOException ioe){
            System.err.println(ioe.getLocalizedMessage());
        }
    }
}
