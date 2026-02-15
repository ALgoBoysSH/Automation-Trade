package com.example.autotrade.strategy;

import com.example.autotrade.dto.Candle;
import com.example.autotrade.dto.TradeSignal;
import com.example.autotrade.entity.StrategyConfig;
import com.example.autotrade.enums.TradeSide;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
public class VwapPullbackStrategy {

    public Optional<TradeSignal> evaluate(String symbol, List<Candle> candles, StrategyConfig config) {
        if (candles.size() < 5) {
            return Optional.empty();
        }

        BigDecimal vwap = calculateVwap(candles);
        Candle latest = candles.get(candles.size() - 1);
        Candle previous = candles.get(candles.size() - 2);

        boolean hadTrendAboveVwap = candles.subList(candles.size() - 5, candles.size() - 1)
                .stream().allMatch(candle -> candle.getClose().compareTo(vwap) > 0);

        boolean pulledBackToVwap = latest.getLow().compareTo(vwap) <= 0
                && latest.getClose().compareTo(vwap) >= 0;

        if (hadTrendAboveVwap && pulledBackToVwap && previous.getClose().compareTo(vwap) > 0) {
            BigDecimal sl = latest.getClose().subtract(BigDecimal.valueOf(config.getSlPoints()));
            BigDecimal target = latest.getClose().add(BigDecimal.valueOf(config.getTargetPoints()));
            return Optional.of(TradeSignal.builder()
                    .symbol(symbol)
                    .side(TradeSide.BUY)
                    .entryPrice(latest.getClose())
                    .stopLossPrice(sl)
                    .targetPrice(target)
                    .build());
        }

        return Optional.empty();
    }

    private BigDecimal calculateVwap(List<Candle> candles) {
        BigDecimal weightedPriceSum = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;

        for (Candle candle : candles) {
            BigDecimal typicalPrice = candle.getHigh().add(candle.getLow()).add(candle.getClose())
                    .divide(BigDecimal.valueOf(3), 4, RoundingMode.HALF_UP);
            weightedPriceSum = weightedPriceSum.add(typicalPrice.multiply(BigDecimal.valueOf(candle.getVolume())));
            totalVolume = totalVolume.add(BigDecimal.valueOf(candle.getVolume()));
        }

        if (totalVolume.compareTo(BigDecimal.ZERO) == 0) {
            return candles.get(candles.size() - 1).getClose();
        }

        return weightedPriceSum.divide(totalVolume, 4, RoundingMode.HALF_UP);
    }
}
