package com.example.autotrade.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderResult {
    boolean success;
    String orderId;
    String statusMessage;
}
