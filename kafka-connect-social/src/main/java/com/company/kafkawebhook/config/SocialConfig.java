package com.company.kafkawebhook.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "social.facebook")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialConfig {

    private String tokenTUVANVAYVON;

    private String idTUVANVAYVON;

    private String tokenVAYDUHOC;

    private String idVAYDUHOC;
}
