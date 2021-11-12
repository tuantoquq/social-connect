package com.company.kafkawebhook.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocialUserInformation {
    private String id;
    private String name;
    private String profile_pic;
    private String gender;
}
