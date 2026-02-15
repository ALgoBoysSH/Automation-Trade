package com.example.autotrade.service;

import com.example.autotrade.dto.OrderResult;
import com.example.autotrade.dto.TradeSignal;
import com.example.autotrade.entity.Trade;
import com.example.autotrade.enums.TradeSide;
import com.example.autotrade.enums.TradeStatus;
import com.example.autotrade.kite.KiteOrderGateway;
import com.example.autotrade.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private KiteOrderGateway kiteOrderGateway;
    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    void shouldMarkTradePlacedWhenOrderSucceeds() {
        TradeSignal signal = TradeSignal.builder()
                .symbol("SBIN")
                .side(TradeSide.BUY)
                .entryPrice(new BigDecimal("120.00"))
                .stopLossPrice(new BigDecimal("118.00"))
                .targetPrice(new BigDecimal("124.00"))
                .build();

        when(tradeRepository.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(kiteOrderGateway.placeEntryOrder(any(TradeSignal.class), any(Integer.class)))
                .thenReturn(OrderResult.builder().success(true).orderId("order_123").statusMessage("OK").build());

        Trade trade = tradeService.executeSignal(signal, 10);

        assertEquals(TradeStatus.PLACED, trade.getStatus());
        assertEquals("order_123", trade.getKiteOrderId());
    }
}
