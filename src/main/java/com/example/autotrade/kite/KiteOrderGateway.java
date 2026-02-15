package com.example.autotrade.kite;

import com.example.autotrade.dto.OrderResult;
import com.example.autotrade.dto.TradeSignal;

public interface KiteOrderGateway {
    OrderResult placeEntryOrder(TradeSignal signal, int quantity);
}
