package com.company.kafkawebhook.webhook;

import com.company.kafkawebhook.builder.Response;
import com.company.kafkawebhook.models.MessageAttachment;
import com.company.kafkawebhook.models.MessageBase;
import com.company.kafkawebhook.models.MessageReaction;
import com.company.kafkawebhook.models.SocialUserInformation;
import com.company.kafkawebhook.utils.FacebookUtils;
import com.company.kafkawebhook.utils.SocialUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@RestController
@RequestMapping(
        path = "/api/messenger-webhook",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class  MessengerWebhook {
    private static final Logger logger = LoggerFactory.getLogger(MessengerWebhook.class);

    //cache list user
    private final List<SocialUserInformation> usersInformation = new ArrayList<>();

    @Value("${social.facebook.page1.token}")
    private String tokenTUVANVAYVON;

    @Value("${social.facebook.page1.id}")
    private String idTUVANVAYVON;

    @Value("${social.facebook.page2.token}")
    private String tokenVAYDUHOC;

    @Value("${social.facebook.page2.id}")
    private String idVAYDUHOC;

    @Value("${social.facebook.page3.token}")
    private String tokenTEAMCONTACTCENTER;

    @Value("${social.facebook.page3.id}")
    private String idTEAMCONTACTCENTER;


    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam Map<String, String> params){
        String challenge = params.get("hub.challenge");
        String verifyToken =params.get("hub.verify_token");
        if(!verifyToken.equals("11111")){
            return ResponseEntity.status(422).body("Cant verify token");
        }
        return ResponseEntity.ok(challenge);
    }

    @PostMapping
    public ResponseEntity<Void> webhook(@RequestBody String data){
        Response response;
        Gson gson = new Gson();
        logger.debug("Event from facebook: {}", data);
        String charset= "UTF-8";
        String channel = "Facebook";

        //get sender id
        String senderId = FacebookUtils.getSenderId(data).replace("\"", "");
        String recipientId = FacebookUtils.getRecipientId(data).replace("\"", "");
        String accessToken;
        if(recipientId.equals(idTUVANVAYVON)){
            accessToken = tokenTUVANVAYVON;
        }else if(recipientId.equals(idVAYDUHOC)){
            accessToken = tokenVAYDUHOC;
        }else {
            accessToken = tokenTEAMCONTACTCENTER;
        }
        MessageBase mes = null;
        try {
            //get sender from cache list
            SocialUserInformation sender = SocialUtils.checkUserInfoInList(senderId,usersInformation);
            if(sender == null){ //if sender not in cache list, we will call API Facebook to get sender info

                //get sender information using Graph API Facebook
                String fields = "id,name,profile_pic,gender";
                String url = String.format("https://graph.facebook.com/v11.0/%s?fields=%s&access_token=%s", senderId, fields, accessToken);

                URLConnection connection = new URL(url).openConnection();
                connection.setRequestProperty("Accept-Charset",charset);
                connection.setRequestProperty("Accept","application/json");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output = br.readLine();
                sender = gson.fromJson(output, SocialUserInformation.class);
                usersInformation.add(sender);
            }else{
                logger.info("check user in cache list");
            }
            //extract data to Message Object (MessageAttachment or MessageReaction
            if(!data.contains("\"reaction\"")){
                if(data.contains("\"text\"")){
                    //message with text + attachment
                    if(data.contains("\"attachments\"")){
                        mes = new MessageAttachment(channel, FacebookUtils.getTime(data),
                                FacebookUtils.getSenderId(data),sender.getName(),sender.getProfile_pic(),
                                sender.getGender(), FacebookUtils.getRecipientId(data), FacebookUtils.getMessageId(data),
                                FacebookUtils.getMessageText(data), FacebookUtils.getMessageAttachment(data));
                    }
                    //message with text only
                    else {
                        mes = new MessageAttachment(channel, FacebookUtils.getTime(data),
                                FacebookUtils.getSenderId(data), sender.getName(), sender.getProfile_pic(),
                                sender.getGender(), FacebookUtils.getRecipientId(data), FacebookUtils.getMessageId(data),
                                FacebookUtils.getMessageText(data), "");
                    }
                    //message with attachment only
                }else{
                    mes = new MessageAttachment(channel, FacebookUtils.getTime(data),
                            FacebookUtils.getSenderId(data),sender.getName(),sender.getProfile_pic(),
                            sender.getGender(), FacebookUtils.getRecipientId(data), FacebookUtils.getMessageId(data),
                            "", FacebookUtils.getMessageAttachment(data));
                }
                //message with reaction
            }else{
                mes = new MessageReaction(channel, FacebookUtils.getTime(data),
                        FacebookUtils.getSenderId(data),sender.getName(),sender.getProfile_pic(),
                        sender.getGender(), FacebookUtils.getRecipientId(data), FacebookUtils.getReactMessageId(data),
                        FacebookUtils.getReactAction(data), FacebookUtils.getReactEmoji(data), FacebookUtils.getReaction(data));
            }
            response = new Response.Builder(200)
                    .buildMessage("successfully")
                    .buildData(mes)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            response = new Response.Builder(500)
                    .buildMessage("could not connect to facebook api to get user information")
                    .build();
        }

        //call producer API to publish message -> kafka
        try {
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", gson.toJson(response));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            template.postForEntity("http://localhost:9090/kafka/facebook/events",entity,String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

}
