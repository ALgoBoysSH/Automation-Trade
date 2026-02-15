package com.example.autotrade.repository;

import com.example.autotrade.entity.Trade;
import com.example.autotrade.enums.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByStatusIn(List<TradeStatus> status);

    List<Trade> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
