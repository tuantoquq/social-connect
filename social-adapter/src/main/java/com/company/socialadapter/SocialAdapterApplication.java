package com.company.socialadapter;

import com.company.socialadapter.config.SocialTokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SocialTokenProperties.class)
public class SocialAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialAdapterApplication.class, args);
    }

}
