package com.example.pay.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.NewTopic;

@Configuration
public class TopicConfig {
    public static final String orderCommand = "order-command";
    @Bean
    public NewTopic orderCommand(){
        return new NewTopic(orderCommand, 1, (short) 1);
    }
}
