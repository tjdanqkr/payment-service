package com.example.pay.service;

import com.example.pay.client.api.CustomerClient;
import com.example.pay.client.api.MenuClient;
import com.example.pay.client.api.OrderCommandClient;
import com.example.pay.domain.kafka.OrderKafkaData;
import com.example.pay.config.TokenInfo;
import com.example.pay.domain.dto.Customer;
import com.example.pay.domain.dto.Menu;
import com.example.pay.domain.entity.Payment;
import com.example.pay.domain.request.PaymentRequest;
import com.example.pay.domain.response.PaymentResponse;
import com.example.pay.kafka.OrderCommandProducer;
import com.example.pay.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerClient customerClient;
    private final MenuClient menuClient;
    private final OrderCommandProducer orderCommandProducer;
    //  등록하는 것
    public void save(PaymentRequest request, TokenInfo tokenInfo){
        Payment save = paymentRepository.save(request.toEntity(tokenInfo));
        OrderKafkaData orderKafkaData = new OrderKafkaData();
        orderKafkaData.setPrice(save.getPrice());
        orderKafkaData.setStoreId(save.getStoreId());
        orderKafkaData.setCustomerId(save.getCustomerId().toString());
        orderCommandProducer.send(orderKafkaData);
    }

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
        Map<UUID, Customer> customerMap = new HashMap<>();
        return all.stream().map(payment -> {
            Customer customer = customerMap.getOrDefault(
                    payment.getCustomerId(), null);
            if(customer == null) {
                customer = customerClient.getById(
                        payment.getCustomerId().toString());
                customerMap.put(customer.getId(), customer);
            }
            List<Menu> menus = menuClient.getAllByIds(
                    Arrays.stream(payment.getMenuIds().split(","))
                            .map(Long::parseLong).toList()
            );
            return new PaymentResponse(payment, menus, customer);
        }).toList();
    }

}
