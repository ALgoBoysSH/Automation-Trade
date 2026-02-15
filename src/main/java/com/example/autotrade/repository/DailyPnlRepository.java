package com.example.autotrade.repository;

import com.example.autotrade.entity.DailyPnl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyPnlRepository extends JpaRepository<DailyPnl, Long> {
    Optional<DailyPnl> findByTradeDate(LocalDate tradeDate);
}
