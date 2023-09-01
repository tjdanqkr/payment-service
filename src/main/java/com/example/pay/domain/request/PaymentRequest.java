package com.example.pay.domain.request;

import com.example.pay.config.TokenInfo;
import com.example.pay.domain.entity.Payment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentRequest {
    private final Integer price;
    private final String menuIds;
//    1, 2, 3
    public Payment toEntity(TokenInfo info){
        return Payment.builder()
                .createAt(LocalDateTime.now())
                .customerId(info.getId())
                .menuIds(menuIds)
                .price(price)
                .build();
    }
}
