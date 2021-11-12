package com.company.socialadapter.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "social.token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialTokenProperties {

    private String facebook;

    private String zalo;
}
