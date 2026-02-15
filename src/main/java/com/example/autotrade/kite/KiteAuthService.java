package com.example.autotrade.kite;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import org.springframework.stereotype.Service;

@Service
public class KiteAuthService {

    public KiteConnect createClient(String apiKey) {
        return new KiteConnect(apiKey);
    }

    public User generateSession(KiteConnect kiteConnect, String requestToken, String apiSecret) throws Exception {
        User user = kiteConnect.generateSession(requestToken, apiSecret);
        kiteConnect.setAccessToken(user.accessToken);
        kiteConnect.setPublicToken(user.publicToken);
        return user;
    }
}
