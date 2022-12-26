package com.example.demo.test;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexClientFactory;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketClient;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexWebsocketConfiguration;
import com.github.jnidzwetzki.bitfinex.v2.entity.BitfinexWallet;
import com.google.common.collect.Table;

import java.util.concurrent.TimeUnit;

public class BinfinexSdk {
    public static void main(String[] args) throws InterruptedException {
        final String apiKey = "tKCGeSqbUndezWJxfnsfXTW3r0lf6jaUr07PiyeGnJt";
        final String apiSecret = "vQpL3UzkTxrG6IhItx96ivJnxl2q0v8EnU4HDNv1f80";

// For public operations (subscribe ticker, candles)
//        final BitfinexWebsocketClient client = BitfinexClientFactory.newSimpleClient();
//        client.connect();

// For public and private operations (executing orders, read wallets)
        final BitfinexWebsocketConfiguration config = new BitfinexWebsocketConfiguration();
        config.setApiCredentials(apiKey, apiSecret);

        final BitfinexWebsocketClient bitfinexClient = BitfinexClientFactory.newSimpleClient(config);
        bitfinexClient.connect();

//        final BitfinexApiKeyPermissions permissions = bitfinexClient.getApiKeyPermissions();
//        if( !permissions.isOrderWritePermission() ) {
//            System.err.println("This API key does not allow the placement of orders");
//        } else {
//            // place order
//        }

        System.out.println("金额"+bitfinexClient.getWalletManager().getWalletTable().get(BitfinexWallet.Type.EXCHANGE,"BTC"));

        Table<BitfinexWallet.Type, String, BitfinexWallet> table = bitfinexClient.getWalletManager().getWalletTable();
        BitfinexWallet wallet = table.get(BitfinexWallet.Type.EXCHANGE,"BTC");
        System.out.println(wallet.getBalance());

        bitfinexClient.getWalletManager().calculateWalletMarginBalance("USD");

// Wait some time until Bitfinex has send us a wallet update
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));

        System.out.println("余额"+bitfinexClient.getWalletManager().getWalletTable().get("margin", "BTC"));

    }

}
