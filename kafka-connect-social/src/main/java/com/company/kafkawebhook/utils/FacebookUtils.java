package com.company.kafkawebhook.utils;

import com.google.gson.JsonParser;

public class FacebookUtils {
    private static  final JsonParser jsonParser = new JsonParser();
    public static String getSenderId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("sender").getAsJsonObject()
                .get("id")).replace("\"", "");
    }
    public static String getRecipientId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("recipient").getAsJsonObject()
                .get("id")).replace("\"", "");
    }

    public static Long getTime(String json){
        return Long.valueOf(String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .get("time")));
    }
    public static String getReactMessageId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("reaction").getAsJsonObject()
                .get("mid")).replace("\"", "");
    }
    public static String getReactAction(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("reaction").getAsJsonObject()
                .get("action")).replace("\"", "");
    }
    public static String getReactEmoji(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("reaction").getAsJsonObject()
                .get("emoji")).replace("\"", "");
    }
    public static String getReaction(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("reaction").getAsJsonObject()
                .get("reaction")).replace("\"", "");
    }
    public static String getMessageId(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("message").getAsJsonObject()
                .get("mid")).replace("\"", "");
    }
    public static String getMessageText(String json){
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("message").getAsJsonObject()
                .get("text")).replace("\"", "");
    }
    public static Object getMessageAttachment(String json){
        return jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonArray("entry").get(0)
                .getAsJsonObject()
                .getAsJsonArray("messaging").get(0)
                .getAsJsonObject().get("message").getAsJsonObject()
                .get("attachments");
    }

}
