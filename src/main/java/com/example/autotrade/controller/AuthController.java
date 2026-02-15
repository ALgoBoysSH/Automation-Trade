package com.example.autotrade.controller;

import com.example.autotrade.kite.KiteAuthService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KiteAuthService kiteAuthService;

    @Value("${kite.api-key}")
    private String apiKey;

    @Value("${kite.api-secret}")
    private String apiSecret;

    @GetMapping("/login-url")
    public ResponseEntity<Map<String, String>> loginUrl() {
        KiteConnect kiteConnect = kiteAuthService.createClient(apiKey);
        return ResponseEntity.ok(Map.of("loginUrl", kiteConnect.getLoginURL()));
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, String>> session(@RequestParam String requestToken) throws Exception {
        KiteConnect kiteConnect = kiteAuthService.createClient(apiKey);
        User user = kiteAuthService.generateSession(kiteConnect, requestToken, apiSecret);
        return ResponseEntity.ok(Map.of(
                "userId", user.userId,
                "accessToken", user.accessToken,
                "publicToken", user.publicToken
        ));
    }
}
