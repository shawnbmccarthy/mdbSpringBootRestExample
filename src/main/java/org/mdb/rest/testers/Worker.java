package org.mdb.rest.testers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;


public final class Worker implements Runnable {

    private HttpClient clnt;
    private HttpUriRequest uri;

    public Worker(String u){
        clnt = HttpClients.createMinimal();
        uri = RequestBuilder.get(u).build();
    }


    @Override public void run(){
        try {
            long start = System.currentTimeMillis();
            HttpResponse res = clnt.execute(uri);
            long end = System.currentTimeMillis();
            System.out.println("rc: " + res.getStatusLine().getStatusCode() + ", type: " + res.getEntity().getContentType() + " l: "
                    + res.getEntity().getContentLength() + ", t: " + (end - start));
        } catch(IOException ioe){
            System.err.println(ioe.getLocalizedMessage());
        }
    }
}
