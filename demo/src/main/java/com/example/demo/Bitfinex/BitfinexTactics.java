package com.example.demo.Bitfinex;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.constant.Constant;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexClientFactory;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexOrderBuilder;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketConfiguration;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrder;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrderType;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexSubmittedOrder;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexWallet;
import com.github.jnidzwetzki.bitfinex.v2.entity.currency.BitfinexCurrencyPair;
import com.github.jnidzwetzki.bitfinex.v2.manager.OrderManager;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.boot.SpringBootVersion;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class BitfinexTactics {
//    @Scheduled(cron = "0 0/5 * * * ?", zone = "UTC")
    public void query() throws IOException {
        final String apiKey = "JNZLDoj18BvwlraqsLj4gEXsQYa4Xg51GWDtEyQrFHV";
        final String apiSecret = "oqC0HDhQJgypjwgSpK00XozB8GuVKzq7ElifFB3qJ9X";
        final BitfinexWebsocketConfiguration config = new BitfinexWebsocketConfiguration();
        config.setApiCredentials(apiKey, apiSecret);

        final BitfinexWebsocketClient bitfinexClient = BitfinexClientFactory.newSimpleClient(config);
        bitfinexClient.connect();
        //使用不同api循环执行交易操作
        try {
            for (int i = 0; i < ApiKey.apiKey.length; i++) {
                String coinType="BTC";
                //查询仓位与币种价格
                Object prise = queryBtcPrise();
                System.out.println("oldTranConstant"+Constant.prise.toString());
                Constant.prise=new BigDecimal(prise.toString());
                System.out.println("价格是"+prise+"TranConstant"+Constant.prise.toString());
                BitfinexWallet wallets = queryWallets(bitfinexClient,coinType);
                BitfinexWallet USD = queryWallets(bitfinexClient,"USD");

                //如果有仓位的话则全部卖出
                if (wallets.getBalance().compareTo(new BigDecimal(0.0001)) > 0) {
                    sell(bitfinexClient,wallets);
                } else {
                    buy(bitfinexClient,wallets,USD,new BigDecimal(prise.toString()));
                    //如果没有仓位则全部买入
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("服务中断");
            bitfinexClient.close();
        }finally {
            bitfinexClient.close();

        }
    }

    private void buy(BitfinexWebsocketClient bitfinexClient, BitfinexWallet wallets, BitfinexWallet USD, BigDecimal prise) throws InterruptedException {
        final OrderManager orderManager = bitfinexClient.getOrderManager();
//        System.out.println("价格是"+prise+"  "+USD.getBalance());
        //计算购买的btc数量
        BigDecimal CoinAmount=USD.getBalance().divide(prise,6, BigDecimal.ROUND_DOWN);
        System.out.println("购买数量"+CoinAmount);
        final BitfinexOrder order
                = BitfinexOrderBuilder.create(BitfinexCurrencyPair.of("BTC", "USD"), BitfinexOrderType.EXCHANGE_MARKET, CoinAmount).build();

        BitfinexSubmittedOrder returnOrder  = orderManager.placeOrderAndWaitUntilActive(order);
        System.out.println(returnOrder.getOrderId());
        System.out.println("买入交易完成，买入数量"+CoinAmount+"价格是"+prise+"  "+USD.getBalance());
    }

    private void sell(BitfinexWebsocketClient bitfinexClient,BitfinexWallet wallets) throws InterruptedException {

        final OrderManager orderManager = bitfinexClient.getOrderManager();

        final BitfinexOrder order
                = BitfinexOrderBuilder.create(BitfinexCurrencyPair.of("BTC", "USD"), BitfinexOrderType.EXCHANGE_MARKET, wallets.getBalance().setScale(6,BigDecimal.ROUND_DOWN).multiply(new BigDecimal("-1"))).build();
//                = BitfinexOrderBuilder.create(BitfinexCurrencyPair.of("BTC", "USD"), BitfinexOrderType.EXCHANGE_MARKET, -0.001).build()

        BitfinexSubmittedOrder returnOrder  = orderManager.placeOrderAndWaitUntilActive(order);
        System.out.println(returnOrder.getOrderId());
        System.out.println("卖出交易完成，卖出数量"+wallets.getBalance());

    }

    private BitfinexWallet queryWallets(BitfinexWebsocketClient bitfinexClient, String coinType) {

        Table<BitfinexWallet.Type, String, BitfinexWallet> table = bitfinexClient.getWalletManager().getWalletTable();
        BitfinexWallet wallet = table.get(BitfinexWallet.Type.EXCHANGE,coinType);
        return wallet;
    }

    private Object queryBtcPrise() throws IOException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        CompletableFuture<Response> future = client.prepare("GET", "https://api-pub.bitfinex.com/v2/tickers?symbols=tBTCUSD")
                .setHeader("Content-Type", "application/json")
                .execute()
                .toCompletableFuture();
        String version = SpringBootVersion.getVersion();
        System.out.println(version);
        List<List<Object>> arr=null;
        try {
            Response response = future.get();
            String body = response.getResponseBody();
            System.out.println(body);
            arr= JSONObject.parseObject(body,new TypeReference<List<List<Object>>>(){});
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        client.close();
        return  arr.get(0).get(1);
    }

}
