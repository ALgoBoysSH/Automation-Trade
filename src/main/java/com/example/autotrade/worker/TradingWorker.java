package com.example.autotrade.worker;

import com.example.autotrade.dto.TradeSignal;
import com.example.autotrade.entity.StrategyConfig;
import com.example.autotrade.repository.StrategyConfigRepository;
import com.example.autotrade.service.MarketDataService;
import com.example.autotrade.service.RiskManagementService;
import com.example.autotrade.service.TradeService;
import com.example.autotrade.service.TradingControlService;
import com.example.autotrade.strategy.VwapPullbackStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradingWorker {
    private final TradingControlService tradingControlService;
    private final StrategyConfigRepository strategyConfigRepository;
    private final MarketDataService marketDataService;
    private final VwapPullbackStrategy strategy;
    private final RiskManagementService riskManagementService;
    private final TradeService tradeService;

    @Scheduled(fixedDelayString = "${strategy.poll-ms:5000}")
    public void runStrategyLoop() {
        if (!tradingControlService.isRunning()) {
            return;
        }

        for (StrategyConfig config : strategyConfigRepository.findByEnabledTrue()) {
            if (!riskManagementService.isTradingAllowed(config)) {
                log.warn("Daily loss cap hit for {}, skipping", config.getSymbol());
                continue;
            }

            Optional<TradeSignal> signal = strategy.evaluate(config.getSymbol(),
                    marketDataService.getRecentCandles(config.getSymbol()), config);

            signal.ifPresent(value -> {
                int qty = riskManagementService.calculateQuantity(value, config);
                if (qty > 0) {
                    tradeService.executeSignal(value, qty);
                } else {
                    log.info("Skipped {} signal due to insufficient quantity", value.getSymbol());
                }
            });
        }
    }
}
