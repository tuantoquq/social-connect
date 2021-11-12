package com.company.socialadapter.service;

import com.company.socialadapter.dto.builder.Response;
import com.company.socialadapter.dto.response.FacebookResponse;
import com.company.socialadapter.input.MessageDataInput;
import com.company.socialadapter.utils.SocialUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FacebookServiceImpl implements SocialService{
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

    private final RestTemplate restTemplate = new RestTemplate();

    private final Gson gson = new Gson();

    @Override
    public Response sendText(MessageDataInput mes) {
        Response svResponse;
        try{
            String url;
            String senderId = mes.getSenderId();
            if(senderId.equals(idTUVANVAYVON)){
                url = String.format("https://graph.facebook.com/v11.0/%s/messages?access_token=%s",senderId, tokenTUVANVAYVON);
            }else if(senderId.equals(idVAYDUHOC)){
                url = String.format("https://graph.facebook.com/v11.0/%s/messages?access_token=%s",senderId, tokenVAYDUHOC);
            }else{
                url = String.format("https://graph.facebook.com/v11.0/%s/messages?access_token=%s",senderId, tokenTEAMCONTACTCENTER);
            }

            String json = String.format("{\"messaging_type\":\"RESPONSE\",\"recipient\":{\"id\":\"%s\"},\"message\":{\"text\":\"%s\"}}",
                    mes.getReceiveId(),mes.getText());

            ResponseEntity<String> response = SocialUtils.getRestTemplateResponse(url, json, restTemplate);
            log.info("response from facebook: {}",response.getBody());
            FacebookResponse facebookResponse = gson.fromJson(response.getBody(),FacebookResponse.class);
            svResponse = getResponseBuilder(response.getStatusCodeValue(),"send message to facebook successfully",facebookResponse);
        }catch(HttpStatusCodeException ex){
            svResponse = getResponseBuilder(ex.getStatusCode().value(),ex.getMessage(),null);
            log.error("error when send message to Facebook: {}",ex.getMessage());
        }
        return svResponse;
    }

    public Response sendAttachment(MultipartFile file,String senderId, String recipient, String message, String type) {
        Response svResponse;
        String accessToken = tokenTEAMCONTACTCENTER;
        if(senderId.equals(idTUVANVAYVON)){
            accessToken = tokenTUVANVAYVON;
        }else if(senderId.equals(idVAYDUHOC)){
            accessToken = tokenVAYDUHOC;
        }
        try{
            String uploadFileUrl = String.format("https://graph.facebook.com/v12.0/me/messages?access_token=%s",accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", file.getResource());
            map.add("recipient",recipient);
            map.add("message",message);
            map.add("type",type);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = this.restTemplate.postForEntity(uploadFileUrl, entity, String.class);
            log.info("response from facebook: {}",response.getBody());
            FacebookResponse facebookResponse = gson.fromJson(response.getBody(),FacebookResponse.class);
            svResponse = new Response.Builder(response.getStatusCodeValue())
                    .buildMessage("send message to facebook successfully")
                    .buildData(facebookResponse)
                    .build();
        }catch (HttpStatusCodeException ex){
            svResponse = getResponseBuilder(ex.getStatusCode().value(),ex.getMessage(),null);
            log.error("error when send message to Facebook: {}",ex.getMessage());
        }
        return svResponse;

    }
    private Response getResponseBuilder(int code, String message, Object data) {
        return new Response.Builder(code)
                .buildMessage(message)
                .buildData(data)
                .build();
    }
}
