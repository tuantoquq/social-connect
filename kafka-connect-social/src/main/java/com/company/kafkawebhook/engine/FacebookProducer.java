package com.company.kafkawebhook.engine;

import com.company.kafkawebhook.webhook.MessengerWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class FacebookProducer {
    private static final Logger logger = LoggerFactory.getLogger(FacebookProducer.class);

    @Value("${social.kafka-topic.facebook}")
    public String topicName;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String message){
        logger.info(String.format("#### -> producing facebook message -> %s",message));
        this.kafkaTemplate.send(topicName,message);
    }

}
