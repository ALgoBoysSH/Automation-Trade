package com.example.autotrade.config;

import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KiteConfig {

    @Bean
    public KiteConnect kiteConnect(@Value("${kite.api-key}") String apiKey,
                                   @Value("${kite.access-token:}") String accessToken) {
        KiteConnect kiteConnect = new KiteConnect(apiKey);
        if (!accessToken.isBlank()) {
            kiteConnect.setAccessToken(accessToken);
        }
        return kiteConnect;
    }
}
