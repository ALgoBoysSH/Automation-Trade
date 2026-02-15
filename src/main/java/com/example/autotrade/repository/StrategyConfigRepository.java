package com.example.autotrade.repository;

import com.example.autotrade.entity.StrategyConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StrategyConfigRepository extends JpaRepository<StrategyConfig, Long> {
    List<StrategyConfig> findByEnabledTrue();
}
