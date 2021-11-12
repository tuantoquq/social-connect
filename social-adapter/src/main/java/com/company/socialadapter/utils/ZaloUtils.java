package com.company.socialadapter.utils;

import com.google.gson.JsonParser;

public class ZaloUtils {
    private static final JsonParser jsonParser = new JsonParser();
    public static String getResponseFromZalo(String json){
        return jsonParser.parse(json)
                .getAsJsonObject()
                .get("data").toString();
    }
    public static String getFileTokenFromZalo(String json) throws NullPointerException {
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("token")).replace("\"", "");
    }
    public static String getImageAttachmentId(String json) throws NullPointerException {
        return String.valueOf(jsonParser.parse(json)
                .getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("attachment_id")).replace("\"", "");
    }
}
