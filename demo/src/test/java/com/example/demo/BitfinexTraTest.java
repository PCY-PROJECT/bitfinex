package com.example.demo;

import com.example.demo.Bitfinex.BitfinexTra;
import org.junit.Test;

import java.io.IOException;

public class BitfinexTraTest {

    @Test
    public void test() throws IOException {
        final BitfinexTra bitfinexTra = new BitfinexTra();
        bitfinexTra.query();

    }

}



