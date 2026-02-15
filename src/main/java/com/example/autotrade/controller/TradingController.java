package com.example.autotrade.controller;

import com.example.autotrade.entity.Trade;
import com.example.autotrade.service.TradeService;
import com.example.autotrade.service.TradingControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trading")
@RequiredArgsConstructor
public class TradingController {
    private final TradingControlService tradingControlService;
    private final TradeService tradeService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start() {
        boolean started = tradingControlService.start();
        return ResponseEntity.ok(Map.of("running", tradingControlService.isRunning(), "started", started));
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stop() {
        boolean stopped = tradingControlService.stop();
        return ResponseEntity.ok(Map.of("running", tradingControlService.isRunning(), "stopped", stopped));
    }

    @GetMapping("/positions")
    public ResponseEntity<List<Trade>> positions() {
        return ResponseEntity.ok(tradeService.getOpenTrades());
    }

    @GetMapping("/trades/today")
    public ResponseEntity<List<Trade>> todayTrades() {
        return ResponseEntity.ok(tradeService.getTodayTrades());
    }
}
