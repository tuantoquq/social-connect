package com.company.kafkawebhook;

import com.company.kafkawebhook.config.SocialConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SocialConfig.class)
public class KafkaWebhookApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaWebhookApplication.class, args);
    }

}
