package com.example.autotrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "strategy_configs")
public class StrategyConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal capital = new BigDecimal("5000.00");

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal riskPerTrade = new BigDecimal("100.00");

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal dailyLossLimit = new BigDecimal("300.00");

    @Column(nullable = false)
    private Integer slPoints = 2;

    @Column(nullable = false)
    private Integer targetPoints = 4;

    @Column(nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
