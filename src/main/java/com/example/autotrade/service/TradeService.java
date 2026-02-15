package com.example.autotrade.service;

import com.example.autotrade.dto.OrderResult;
import com.example.autotrade.dto.TradeSignal;
import com.example.autotrade.entity.Trade;
import com.example.autotrade.enums.TradeStatus;
import com.example.autotrade.kite.KiteOrderGateway;
import com.example.autotrade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {
    private final KiteOrderGateway kiteOrderGateway;
    private final TradeRepository tradeRepository;

    public Trade executeSignal(TradeSignal signal, int quantity) {
        Trade trade = new Trade();
        trade.setSymbol(signal.getSymbol());
        trade.setSide(signal.getSide());
        trade.setStatus(TradeStatus.PENDING);
        trade.setEntryPrice(signal.getEntryPrice());
        trade.setStopLossPrice(signal.getStopLossPrice());
        trade.setTargetPrice(signal.getTargetPrice());
        trade.setQuantity(quantity);
        trade = tradeRepository.save(trade);

        OrderResult orderResult = kiteOrderGateway.placeEntryOrder(signal, quantity);
        if (orderResult.isSuccess()) {
            trade.setStatus(TradeStatus.PLACED);
            trade.setKiteOrderId(orderResult.getOrderId());
        } else {
            trade.setStatus(TradeStatus.REJECTED);
        }

        return tradeRepository.save(trade);
    }

    public List<Trade> getOpenTrades() {
        return tradeRepository.findByStatusIn(List.of(TradeStatus.PENDING, TradeStatus.PLACED, TradeStatus.EXECUTED));
    }

    public List<Trade> getTodayTrades() {
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        return tradeRepository.findByCreatedAtBetween(start, LocalDateTime.now());
    }
}
