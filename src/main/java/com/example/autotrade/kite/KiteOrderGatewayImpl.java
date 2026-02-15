package com.example.autotrade.kite;

import com.example.autotrade.dto.OrderResult;
import com.example.autotrade.dto.TradeSignal;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KiteOrderGatewayImpl implements KiteOrderGateway {
    private final KiteConnect kiteConnect;

    @Value("${kite.user-id}")
    private String userId;

    @Override
    public OrderResult placeEntryOrder(TradeSignal signal, int quantity) {
        try {
            OrderParams params = new OrderParams();
            params.tradingsymbol = signal.getSymbol();
            params.exchange = "NSE";
            params.transactionType = signal.getSide().name();
            params.orderType = "MARKET";
            params.quantity = quantity;
            params.product = "MIS";
            params.validity = "DAY";

            Order order = kiteConnect.placeOrder(params, "regular");
            return OrderResult.builder().success(true).orderId(order.orderId).statusMessage("Order placed").build();
        } catch (Exception ex) {
            log.error("Failed to place order for {}", signal.getSymbol(), ex);
            return OrderResult.builder().success(false).statusMessage(ex.getMessage()).build();
        }
    }
}
