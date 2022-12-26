package com.example.demo.test;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexClientFactory;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexOrderBuilder;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketConfiguration;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrder;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexOrderType;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexSubmittedOrder;
import com.github.jnidzwetzki.bitfinex.v2.entity.currency.BitfinexCurrencyPair;
import com.github.jnidzwetzki.bitfinex.v2.manager.TradeManager;

public class subricribe {

      public static void main(String[] args) throws InterruptedException {


          final String apiKey = "LeynBy81llOl49DayHkdRdRX4iMARG3DDGrFYprI6IB";
          final String apiSecret = "Q1DfiGRaZdGvBugAfL41vzixIIXKSSXS8ebDixD8zoi";


          // For public and private operations (executing orders, read wallets)
          final BitfinexWebsocketConfiguration config = new BitfinexWebsocketConfiguration();
          config.setApiCredentials(apiKey, apiSecret);

          final BitfinexWebsocketClient bitfinexClient = BitfinexClientFactory.newSimpleClient(config);
          bitfinexClient.connect();
          try {
              BitfinexCurrencyPair.registerDefaults();

              final BitfinexOrder order = BitfinexOrderBuilder
                      .create(BitfinexCurrencyPair.of("BTC", "USD"), BitfinexOrderType.MARKET, -0.001)
                      .build();

              BitfinexSubmittedOrder order1 = bitfinexClient.getOrderManager().placeOrderAndWaitUntilActive(order);
              System.out.println("订单返回值" + order1.toString());
              final TradeManager tradeManager = bitfinexClient.getTradeManager();

              tradeManager.registerCallback((trade) -> {
                          System.out.format("Got trade callback (%s)\n", trade);
                      }
              );
          }catch (Exception e){
              e.printStackTrace();
              System.out.println("服务中断");
              bitfinexClient.close();
          }
      }
}
