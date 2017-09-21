package org.mdb.rest.teters;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public final class Worker implements Runnable {

    private final URLConnection connection;

    public Worker(URLConnection c){
        connection = c;
    }


    @Override public void run(){
        try {
            Object o = connection.getContent();
        } catch(IOException ioe){
            System.err.println(ioe.getLocalizedMessage());
        }
    }
}
