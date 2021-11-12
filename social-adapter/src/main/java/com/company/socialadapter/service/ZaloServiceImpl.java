package com.company.socialadapter.service;

import com.company.socialadapter.dto.builder.Response;
import com.company.socialadapter.dto.response.ZaloErrorResponse;
import com.company.socialadapter.dto.response.ZaloResponse;
import com.company.socialadapter.input.MessageDataInput;
import com.company.socialadapter.utils.SocialUtils;
import com.company.socialadapter.utils.ZaloUtils;
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
public class ZaloServiceImpl implements SocialService{

    private final Gson gson = new Gson();

    @Value("${social.zalo.token}")
    private String zaloToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Response sendText(MessageDataInput mes) {
        Response svResponse;
        try {
            String url = String.format("https://openapi.zalo.me/v2.0/oa/message?access_token=%s", this.zaloToken);
            String json = String.format("{\"recipient\":{\"user_id\":\"%s\"},\"message\":{\"text\":\"%s\"}}",mes.getReceiveId(), mes.getText());
            ResponseEntity<String> response = SocialUtils.getRestTemplateResponse(url, json, this.restTemplate);
            log.info("response from zalo: {}", response.getBody());
            try {
                ZaloResponse zaloResponse = gson.fromJson(ZaloUtils.getResponseFromZalo(response.getBody()), ZaloResponse.class);
                svResponse = new Response.Builder(response.getStatusCodeValue())
                        .buildMessage("send message to zalo successfully")
                        .buildData(zaloResponse)
                        .build();
            } catch (NullPointerException ex) {
                ZaloErrorResponse errorResponse = gson.fromJson(response.getBody(), ZaloErrorResponse.class);
                svResponse = new Response.Builder(500)
                        .buildMessage("error when sent to zalo")
                        .buildData(errorResponse)
                        .build();
                log.error("error when sent to zalo: {}",errorResponse);
            }
        } catch (HttpStatusCodeException ex) {
            svResponse = new Response.Builder(ex.getStatusCode().value())
                    .buildMessage(ex.getMessage())
                    .buildData(null)
                    .build();

            log.error("error when sent to zalo: {}",ex.getMessage());
        }
        return svResponse;
    }

    public Response sendImage(MultipartFile file,String textMessage, String receiveId) {
        Response svResponse;
        try {
            try {
                String fileToken = ZaloUtils.getImageAttachmentId(getImageAttachmentId(file));
                String sendFileURL = String.format("https://openapi.zalo.me/v2.0/oa/message?access_token=%s",this.zaloToken);
                String jsonBody = String.format("{\"recipient\":{\"user_id\": \"%s\"},\"message\":{\"text\": \"%s\",\"attachment\":{\"type\": \"template\"," +
                        "\"payload\": {" +
                        "\"template_type\": \"media\"," +
                        "\"elements\": [{\n" +
                        "\"media_type\": \"image\"," +
                        "\"attachment_id\": \"%s\"}]}}}}", receiveId, textMessage, fileToken);
                ResponseEntity<String> response = SocialUtils.getRestTemplateResponse(sendFileURL, jsonBody, this.restTemplate);
                log.info("response from zalo: {}", response.getBody());
                try {
                    ZaloResponse zaloResponse = gson.fromJson(ZaloUtils.getResponseFromZalo(response.getBody()), ZaloResponse.class);
                    svResponse = new Response.Builder(response.getStatusCodeValue())
                            .buildMessage("send message to zalo successfully")
                            .buildData(zaloResponse)
                            .build();
                } catch (NullPointerException ex) {
                    ZaloErrorResponse errorResponse = gson.fromJson(response.getBody(), ZaloErrorResponse.class);
                    svResponse = new Response.Builder(500)
                            .buildMessage("error when sent file to zalo")
                            .buildData(errorResponse)
                            .build();
                    log.error("error when sent to zalo: {}",errorResponse);
                }
            } catch (NullPointerException e) {
                ZaloErrorResponse errorResponse = gson.fromJson(getFileToken(file), ZaloErrorResponse.class);
                svResponse = new Response.Builder(500)
                        .buildMessage("error when get file token")
                        .buildData(errorResponse)
                        .build();
                log.error("error when sent to zalo: {}",errorResponse);
            }
        } catch (HttpStatusCodeException ex) {
            svResponse = new Response.Builder(ex.getStatusCode().value())
                    .buildMessage(ex.getMessage())
                    .buildData(null)
                    .build();
            log.error("error when sent to zalo: {}",ex.getMessage());
        }
        return svResponse;
    }

    public Response sendFile(MultipartFile file, String receiveId) {
        Response svResponse;
        try {
            try {
                String fileToken = ZaloUtils.getFileTokenFromZalo(getFileToken(file));
                String sendFileURL = String.format("https://openapi.zalo.me/v2.0/oa/message?access_token=%s",this.zaloToken);
                String jsonBody = String.format("{\"recipient\":{\"user_id\": \"%s\"},\"message\":{\"attachment\":{\"type\": \"file\",\"payload\":{\"token\": \"%s\"}}}}", receiveId, fileToken);
                ResponseEntity<String> response = SocialUtils.getRestTemplateResponse(sendFileURL, jsonBody, this.restTemplate);
                log.info("response from zalo: {}", response.getBody());
                try {
                    ZaloResponse zaloResponse = gson.fromJson(ZaloUtils.getResponseFromZalo(response.getBody()), ZaloResponse.class);
                    svResponse = new Response.Builder(response.getStatusCodeValue())
                            .buildMessage("send message to zalo successfully")
                            .buildData(zaloResponse)
                            .build();
                } catch (NullPointerException ex) {
                    ZaloErrorResponse errorResponse = gson.fromJson(response.getBody(), ZaloErrorResponse.class);
                    svResponse = new Response.Builder(500)
                            .buildMessage("error when sent file to zalo")
                            .buildData(errorResponse)
                            .build();
                    log.error("error when sent to zalo: {}",errorResponse);
                }
            } catch (NullPointerException e) {
                ZaloErrorResponse errorResponse = gson.fromJson(getFileToken(file), ZaloErrorResponse.class);
                svResponse = new Response.Builder(500)
                        .buildMessage("error when get file token")
                        .buildData(errorResponse)
                        .build();
                log.error("error when sent to zalo: {}",errorResponse);
            }
        } catch (HttpStatusCodeException ex) {
            svResponse = new Response.Builder(ex.getStatusCode().value())
                    .buildMessage(ex.getMessage())
                    .buildData(null)
                    .build();
            log.error("error when sent to zalo: {}",ex.getMessage());
        }
        return svResponse;
    }

    public String getFileToken(MultipartFile file) throws NullPointerException {
        String uploadFileUrl = String.format("https://openapi.zalo.me/v2.0/oa/upload/file?access_token=%s", this.zaloToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uploadFileUrl, entity, String.class);
        return response.getBody();
    }
    public String getImageAttachmentId(MultipartFile file) throws NullPointerException{
        String uploadFileUrl = String.format("https://openapi.zalo.me/v2.0/oa/upload/image?access_token=%s", this.zaloToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uploadFileUrl, entity, String.class);
        return response.getBody();
    }
}
