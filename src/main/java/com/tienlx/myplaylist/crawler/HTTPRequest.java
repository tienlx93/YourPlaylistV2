package com.tienlx.myplaylist.crawler;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by ASUS on 10/08/2016.
 */
public class HTTPRequest {
    public static String getHTML(String url) throws Exception {

        //create a singular HttpClient object
        HttpClient client = new HttpClient();

        //establish a connection within 5 seconds
        client.getHttpConnectionManager().
                getParams().setConnectionTimeout(5000);

        HttpMethod method = null;

        //create a method object
        method = new GetMethod(url);
        method.setFollowRedirects(true);

        //execute the method
        String responseBody = null;
        try{
            client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
        } catch (HttpException he) {
            System.err.println("Http error connecting to '" + url + "'");
            System.err.println(he.getMessage());
            return null;
        } catch (IOException ioe){
            System.err.println("Unable to connect to '" + url + "'");
            return null;
        }

        //write out the response headers
        System.out.println("*** Response ***");
        System.out.println("Status Line: " + method.getStatusLine());

        //write out the response body
        System.out.println("*** Response Body ***");
        System.out.println(responseBody);

        //clean up the connection resources
        method.releaseConnection();
        return responseBody;
    }
}
