package com.company.kafkawebhook.utils;

import com.company.kafkawebhook.builder.Response;
import com.company.kafkawebhook.models.SocialUserInformation;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SocialUtils {
    public static SocialUserInformation checkUserInfoInList(String userId, List<SocialUserInformation> usersInformation){
        for(SocialUserInformation u: usersInformation){
            if(u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }
}
