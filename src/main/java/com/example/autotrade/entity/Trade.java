package com.example.autotrade.entity;

import com.example.autotrade.enums.TradeSide;
import com.example.autotrade.enums.TradeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeSide side;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus status;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 12, scale = 2)
    private BigDecimal entryPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal stopLossPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal targetPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal exitPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal realizedPnl = BigDecimal.ZERO;

    @Column
    private String kiteOrderId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime closedAt;
}
