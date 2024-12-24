package com.example.crud_bd.Kafka;

import com.example.crud_bd.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerService {
    private final String TOPIC = "NewTopic";
    private final KafkaTemplate<String, User> template;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, User> template) {
        this.template = template;
    }

    public void sendMessage(String message, User user) {
        template.send(TOPIC, message, user);
    }
}