package com.example.userservice.Kafka;


import com.example.userservice.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * если нужно более тонко настроить кафку, то надо делать KafkaConfig и плясать с ним
 */
@Component
public class KafkaProducerService {
    private final String TOPIC = "newTopic";
    private final KafkaTemplate<String, User> template;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, User> template) {
        this.template = template;
    }

    public void sendMessage(String message, User user) {
        template.send(TOPIC, message, user);
    }
}