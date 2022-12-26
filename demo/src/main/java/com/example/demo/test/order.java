package com.example.demo.test;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexClientFactory;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexOrderBuilder;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketConfiguration;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexApiKeyPermissions;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrder;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrderType;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexSubmittedOrder;
import com.github.jnidzwetzki.bitfinex.v2.entity.currency.BitfinexCurrencyPair;
import com.github.jnidzwetzki.bitfinex.v2.manager.OrderManager;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexAccountSymbol;
import com.github.jnidzwetzki.bitfinex.v2.symbol.BitfinexSymbols;

public class order {
    public static void main(String[] args) throws Exception {


        final String apiKey = "CeKV096ypmjs9ANm1zLQabaafpmB87aQt5rmVxLOheO";
        final String apiSecret = "AjlEtcCUGbVsBvSEcHHC2QBCciD1a3FRsrBxWrdqueJ";


        // For public and private operations (executing orders, read wallets)
        final BitfinexWebsocketConfiguration config = new BitfinexWebsocketConfiguration();
        config.setApiCredentials(apiKey, apiSecret);

        final BitfinexWebsocketClient bitfinexApiBroker = BitfinexClientFactory.newSimpleClient(config);
        bitfinexApiBroker.connect();
        try {
            BitfinexCurrencyPair.registerDefaults();
          //  BitfinexCurrencyPair.register("BTC","USD", BitfinexCurrencyType.CURRENCY, 0.001);

            final OrderManager orderManager = bitfinexApiBroker.getOrderManager();

            final BitfinexOrder order
                    = BitfinexOrderBuilder.create(BitfinexCurrencyPair.of("BTC", "USD"), BitfinexOrderType.EXCHANGE_MARKET, 0.001).build();
            BitfinexAccountSymbol symbol = BitfinexSymbols.account(BitfinexApiKeyPermissions.ALL_PERMISSIONS, apiKey);

//            CountDownLatch waitLatch = new CountDownLatch(1);
//
//            final Runnable r = () -> {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    return;
//                }
//                final BitfinexSubmittedOrder exchangeOrder = new BitfinexSubmittedOrder();
//                //exchangeOrder.setOrderId();
//                exchangeOrder.setClientId(order.getClientId());
//                exchangeOrder.setStatus(BitfinexSubmittedOrderStatus.ACTIVE);
//                orderManager.updateOrderCallback(symbol, exchangeOrder);
//                waitLatch.countDown();
//                System.out.print(orderManager);
//            };

            // Cancel event
           // (new Thread(r)).start();
            //waitLatch.await(1000, TimeUnit.SECONDS);


            BitfinexSubmittedOrder returnOrder  = orderManager.placeOrderAndWaitUntilActive(order);
            System.out.println(returnOrder.getOrderId());
            System.out.println("交易完成");

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("服务中断");
            bitfinexApiBroker.close();
        }
    }
}
