package com.example.pay.service;

import com.example.pay.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
//  내꺼 보는거
//  전체 결제내역 보는거
//  등록하는 것
}
