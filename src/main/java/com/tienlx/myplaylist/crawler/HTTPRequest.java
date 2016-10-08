package com.tienlx.myplaylist.crawler;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ASUS on 10/08/2016.
 */
public class HTTPRequest {
    public static String getHTML(String url) {

        //create a singular HttpClient object
        HttpClient client = HttpClientBuilder.create().build();

        try {
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);
            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
