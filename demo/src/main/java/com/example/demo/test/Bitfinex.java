package com.example.demo.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class Bitfinex {

    public void test () throws IOException {
       AsyncHttpClient client = new DefaultAsyncHttpClient();
//        client.prepare("GET", "https://api-pub.bitfinex.com/v2/tickers?symbols=tBTCUSD")
//                .setHeader("accept", "application/json")
//                .execute()
//                .toCompletableFuture()
//                .thenAccept(System.out::println)
//                .join();


       CompletableFuture<Response> future = client.prepare("GET", "https://api-pub.bitfinex.com/v2/tickers?symbols=tBTCUSD")
               .setHeader("accept", "application/json")
               .setHeader("content-type", "application/x-www-form-urlencoded")

               .execute()
                .toCompletableFuture();
        String version = SpringBootVersion.getVersion();
System.out.println(version);
        try {
            Response response = future.get();
            String body = response.getResponseBody();
                       System.out.println(body);

            List<List<Object>> arr = JSONObject.parseObject(body,new TypeReference<List<List<Object>>>(){});
            System.out.println( arr.get(0).get(0));

            System.out.println( arr.get(0).get(1));
            System.out.println( arr.get(0).get(2));

//            System.out.println(body);
           // System.out.println(JSONArray.parse(body));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        client.close();
        System.out.println( "close");

    }
}
