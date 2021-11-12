package com.company.kafkawebhook.webhook;

import com.company.kafkawebhook.builder.Response;
import com.company.kafkawebhook.models.MessageAttachment;
import com.company.kafkawebhook.models.MessageBase;
import com.company.kafkawebhook.models.SocialUserInformation;
import com.company.kafkawebhook.utils.SocialUtils;
import com.company.kafkawebhook.utils.ZaloUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping(
        path = "/api/zalo-webhook",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Slf4j
public class ZaloWebhook {
    private static final Logger logger = LoggerFactory.getLogger(ZaloWebhook.class);

    //cache list user
    private final List<SocialUserInformation> usersInformation = new ArrayList<>();
    @Value("${social.zalo.token}")
    private String zaloToken;

    private Gson gson = new Gson();

    @PostMapping
    public ResponseEntity<Void> webhook(@RequestBody String data){

        log.debug("Event from zalo: {}",data);
        if(data.contains("\"event_name\"")){
            Response response;
            String channel = "Zalo";
            String charset = "UTF-8";

            String senderId = ZaloUtils.getSenderId(data);
            MessageBase messageBase = null;
            String responseCode = "0";

            //get user from cache list
            SocialUserInformation sender = SocialUtils.checkUserInfoInList(senderId,usersInformation);

            String apiZaloResponse = null;
            try {
                if( sender == null){ //if sender not in cache list, we will call API zalo to get sender info

                    //get user information
                    String url = String.format("https://openapi.zalo.me/v2.0/oa/getprofile?data={\"user_id\":\"%s\"}&access_token=%s",
                            senderId,zaloToken);
                    URLConnection connection = new URL(url).openConnection();
                    connection.setRequestProperty("Accept-Charset",charset);
                    connection.setRequestProperty("Accept","application/json");
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    apiZaloResponse = br.readLine();
                    sender = ZaloUtils.getZaloUserInformation(apiZaloResponse,senderId);
                    responseCode = ZaloUtils.getErrorCodeFromGetUserInfoApi(apiZaloResponse);
                    usersInformation.add(sender);
                }else {
                    logger.info("check user in cache list");
                }
                if(responseCode.equals("0")) {
                    if (!data.contains("\"attachments\"")) { //message with text only
                        messageBase = new MessageAttachment(channel, ZaloUtils.getTime(data), senderId,sender.getName(),
                                sender.getProfile_pic(), sender.getGender(), ZaloUtils.getRecipientId(data),
                                ZaloUtils.getMessageId(data), ZaloUtils.getMessageText(data), "");
                    } else if(!data.contains("\"text\"")){ // message with attachment only
                        messageBase = new MessageAttachment(channel, ZaloUtils.getTime(data), senderId, sender.getName(),
                                sender.getProfile_pic(), sender.getGender(), ZaloUtils.getRecipientId(data),
                                ZaloUtils.getMessageId(data), "", ZaloUtils.getMessageAttachment(data));
                    }else{ //message with text + attachment
                        messageBase = new MessageAttachment(channel, ZaloUtils.getTime(data), senderId, sender.getName(),
                                sender.getProfile_pic(), sender.getGender(), ZaloUtils.getRecipientId(data),
                                ZaloUtils.getMessageId(data), ZaloUtils.getMessageText(data), ZaloUtils.getMessageAttachment(data));
                    }
                    response = new Response.Builder(200)
                            .buildMessage("successfully")
                            .buildData(messageBase)
                            .build();
                }else{ //response code from zalo != "0" -> has error
                    response = new Response.Builder(Integer.parseInt(responseCode))
                            .buildMessage(ZaloUtils.getMessageFromGetUserInfoApi(apiZaloResponse))
                            .build();
                }
            }catch (IOException e){
                response = new Response.Builder(500)
                        .buildMessage("error when get user information from zalo api")
                        .build();
            }
            //call producer API to publish message -> kafka
            try {
                RestTemplate template = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("message", gson.toJson(response));
                HttpEntity<?> entity = new HttpEntity<>(headers);
                template.postForEntity("http://localhost:9090/kafka/zalo/events",entity,String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().build();
    }

}
