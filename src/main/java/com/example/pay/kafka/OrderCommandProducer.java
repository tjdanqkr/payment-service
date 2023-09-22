package com.example.pay.kafka;


import com.example.pay.domain.kafka.OrderKafkaData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCommandProducer {
    private final KafkaTemplate<String, OrderKafkaData> template;
    public void send(OrderKafkaData data){
        template.send(TopicConfig.orderCommand, data);
    }

}
