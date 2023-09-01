package com.example.pay.service;

import com.example.pay.client.api.CustomerClient;
import com.example.pay.client.api.MenuClient;
import com.example.pay.config.TokenInfo;
import com.example.pay.domain.dto.Customer;
import com.example.pay.domain.dto.Menu;
import com.example.pay.domain.entity.Payment;
import com.example.pay.domain.request.PaymentRequest;
import com.example.pay.domain.response.PaymentResponse;
import com.example.pay.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerClient customerClient;
    private final MenuClient menuClient;
//  내꺼 보는거

    public List<PaymentResponse> getByCustomerId(TokenInfo info){
        List<Payment> all = paymentRepository.findAllByCustomerId(info.getId());
        return getPaymentResponseList(all);
    }

//  전체 결제내역 보는거
    public List<PaymentResponse> getAll(){
        List<Payment> all = paymentRepository.findAll();
        return getPaymentResponseList(all);
    }

    private List<PaymentResponse> getPaymentResponseList(List<Payment> all) {
        return all.stream().map(payment -> {
            Customer customer = customerClient.getById(
                    payment.getCustomerId().toString());
            List<Menu> menus = menuClient.getAllByIds(
                    Arrays.stream(payment.getMenuIds().split(","))
                            .map(Long::parseLong).toList()
            );
            return new PaymentResponse(payment, menus, customer);
        }).toList();
    }

    //  등록하는 것
    public void save(PaymentRequest request, TokenInfo tokenInfo){
        paymentRepository.save(request.toEntity(tokenInfo));
    }
}
