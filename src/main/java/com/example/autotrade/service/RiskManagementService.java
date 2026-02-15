package com.example.autotrade.service;

import com.example.autotrade.dto.TradeSignal;
import com.example.autotrade.entity.DailyPnl;
import com.example.autotrade.entity.StrategyConfig;
import com.example.autotrade.repository.DailyPnlRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class RiskManagementService {
    private final DailyPnlRepository dailyPnlRepository;

    public RiskManagementService(DailyPnlRepository dailyPnlRepository) {
        this.dailyPnlRepository = dailyPnlRepository;
    }

    public boolean isTradingAllowed(StrategyConfig config) {
        DailyPnl dailyPnl = dailyPnlRepository.findByTradeDate(LocalDate.now()).orElseGet(DailyPnl::new);
        return dailyPnl.getNetPnl().negate().compareTo(config.getDailyLossLimit()) < 0;
    }

    public int calculateQuantity(TradeSignal signal, StrategyConfig config) {
        BigDecimal slDistance = signal.getEntryPrice().subtract(signal.getStopLossPrice()).abs();
        if (slDistance.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        BigDecimal rawQty = config.getRiskPerTrade().divide(slDistance, 0, RoundingMode.DOWN);
        BigDecimal affordableQty = config.getCapital().divide(signal.getEntryPrice(), 0, RoundingMode.DOWN);

        return rawQty.min(affordableQty).intValue();
    }
}
