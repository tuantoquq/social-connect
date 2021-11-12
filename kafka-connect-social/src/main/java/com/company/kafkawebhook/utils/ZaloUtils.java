package com.company.kafkawebhook.utils;

import com.company.kafkawebhook.models.SocialUserInformation;
import com.google.gson.JsonParser;

public class ZaloUtils {
    private static  final JsonParser jsonParser = new JsonParser();
    public static String getSenderId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("sender").getAsJsonObject()
                .get("id")).replace("\"", "");
    }
    public static String getRecipientId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("recipient").getAsJsonObject()
                .get("id")).replace("\"", "");
    }
    public static Long getTime(String json){
        return Long.valueOf(String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("timestamp")).replace("\"",""));
    }
    public static String getMessageId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("message").getAsJsonObject()
                .get("msg_id")).replace("\"", "");
    }
    public static String getMessageText(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("message").getAsJsonObject()
                .get("text")).replace("\"", "");
    }
    public static Object getMessageAttachment(String json){
        return jsonParser.parse(json)
                .getAsJsonObject()
                .get("message").getAsJsonObject()
                .get("attachments");
    }
    public static String getUserGender(String json){
        String genderCode =  String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("user_gender"));
        if(genderCode.equals("1")) return "male";
        else return "female";
    }
    public static String getUserAvatarURL(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("avatars").getAsJsonObject()
                .get("240")).replace("\"", "");
    }
    public static String getUserNickName(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("display_name")).replace("\"", "");
    }
    public static String getErrorCodeFromGetUserInfoApi(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("error"));
    }
    public static String getMessageFromGetUserInfoApi(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("message"));
    }
    public static SocialUserInformation getZaloUserInformation(String json, String senderId){
        SocialUserInformation userRes = new SocialUserInformation();
        userRes.setId(senderId);
        userRes.setName(getUserNickName(json));
        userRes.setGender(getUserGender(json));
        userRes.setProfile_pic(getUserAvatarURL(json));
        return userRes;
    }
}
