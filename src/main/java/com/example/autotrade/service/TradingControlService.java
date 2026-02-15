package com.example.autotrade.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TradingControlService {
    private final AtomicBoolean running = new AtomicBoolean(false);

    public boolean start() {
        return running.compareAndSet(false, true);
    }

    public boolean stop() {
        return running.compareAndSet(true, false);
    }

    public boolean isRunning() {
        return running.get();
    }
}
