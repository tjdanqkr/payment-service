package com.example.pay.client.request;


import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {
    private Long storeId;
    private String customerId;
    private Integer price;
}
