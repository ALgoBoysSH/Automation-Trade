package com.example.autotrade.service;

import com.example.autotrade.dto.Candle;

import java.math.BigDecimal;
import java.util.List;

public interface MarketDataService {
    void start();

    void stop();

    List<Candle> getRecentCandles(String symbol);

    void onTick(String symbol, BigDecimal price, long volume);
}
