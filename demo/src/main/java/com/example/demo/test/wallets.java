package com.example.demo.test;

import com.alibaba.fastjson.JSON;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class wallets {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String nonce = String.valueOf(System.currentTimeMillis()* 1000);// Standard nonce generator. Timestamp * 1000
        String apikey="LeynBy81llOl49DayHkdRdRX4iMARG3DDGrFYprI6IB";
        String body="";
        String signature="/v2/auth/r/wallets/v2/auth/r/wallets"+nonce+ JSON.toJSONString(body);
        String secret="Q1DfiGRaZdGvBugAfL41vzixIIXKSSXS8ebDixD8zoi";
         // MAC加密算法
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        //        let signature = `/api/${apiPath}${nonce}${JSON.stringify(body)}`
        byte[] signData = mac.doFinal(signature.getBytes(StandardCharsets.UTF_8));
        client.prepare("POST", "https://api.bitfinex.com/v2/auth/r/wallets")
                .setHeader("accept", "application/json")
                .setHeader("content-type", "application/x-www-form-urlencoded")
                .setHeader("bfx-nonce",nonce)
                .setHeader("bfx-apikey",apikey)
                //Q1DfiGRaZdGvBugAfL41vzixIIXKSSXS8ebDixD8zoi
                .setHeader("bfx-signature",signData)
                .execute()
                .toCompletableFuture()
                .thenAccept(System.out::println)
                .join();

        client.close();
    }
}
