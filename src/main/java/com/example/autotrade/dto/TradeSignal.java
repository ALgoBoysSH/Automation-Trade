package com.example.autotrade.dto;

import com.example.autotrade.enums.TradeSide;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TradeSignal {
    String symbol;
    TradeSide side;
    BigDecimal entryPrice;
    BigDecimal stopLossPrice;
    BigDecimal targetPrice;
}
