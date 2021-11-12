package com.company.socialadapter.controller;

import com.company.socialadapter.config.SocialTokenProperties;
import com.company.socialadapter.dto.builder.Response;
import com.company.socialadapter.input.MessageDataInput;
import com.company.socialadapter.service.FacebookServiceImpl;
import com.company.socialadapter.service.ZaloServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;

@RestController
@RequestMapping("/sendMessage")
@Slf4j
public class SocialController {
    @Autowired
    private FacebookServiceImpl facebookService;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ZaloServiceImpl zaloService;
    private final Gson gson = new Gson();
    @PostMapping(
            path = "/facebook/text",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> sendMessageToFacebook(@RequestBody String mes){
        Response response;
        MessageDataInput data ;
        try {
            data = mapper.readValue(mes, MessageDataInput.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Response.Builder(400)
                    .buildMessage("request body input is invalid!")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        if(data.getChannel().equals("Facebook")){
            response = facebookService.sendText(data);
        }else{
            response = new Response.Builder(400)
                    .buildMessage("Channel name not available!")
                    .build();
        }
        log.debug(String.format("API response: code: %s, message: %s",response.getCode(),response.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));

    }
    @PostMapping(path = {"/facebook/attachment"})
    public ResponseEntity<Object> sendFileToFacebook(@RequestParam("filedata") MultipartFile filedata, @RequestParam("recipient") String recipient,
                                                     @RequestParam String senderId,
                                                     @RequestParam String message,
                                                     @RequestParam String type) {
        log.debug(String.format("Params: senderID: %s, recipient: %s, message: %s, type: %s",senderId,recipient,
                         message,type));
        Response response = facebookService.sendAttachment(filedata,senderId,recipient,message,type);
        log.debug(String.format("API response: code: %s, message: %s",response.getCode(),response.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
    @PostMapping(path = {"/zalo/text"}, produces = {"application/json"})
    public ResponseEntity<Object> sendTextOrImageToZalo(@RequestBody String mes) {
        Response response;
        MessageDataInput data = gson.fromJson(mes, MessageDataInput.class);
        log.info("data: {}",data);
        if(data.getChannel().equals("Zalo")){
            response = this.zaloService.sendText(data);
        }else{
            response = new Response.Builder(400)
                    .buildMessage("Channel name not available!")
                    .build();
        }
        log.debug(String.format("API response: code: %s, message: %s",response.getCode(),response.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping(path = {"/zalo/attachment"})
    public ResponseEntity<Object> sendFileToZalo(@RequestParam("attachment") MultipartFile attachment,
                                                 @RequestParam("receiveId") String receiveId,
                                                 @RequestParam String type,
                                                @RequestParam String textMessage) {
        log.debug(String.format("Params: receivedID: %s, text_message: %s, type: %s",receiveId,
                textMessage,type));
        Response response;
        if(attachment.getSize() > 5242880){
            response = new Response.Builder(400)
                    .buildMessage("File too large. File size is limited by 5MB")
                    .buildData(null)
                    .build();
        }else{
            if(type.equals("image")){
                response = zaloService.sendImage(attachment, textMessage, receiveId);
            }else{
                response = this.zaloService.sendFile(attachment, receiveId);
            }
        }
        log.debug(String.format("API response: code: %s, message: %s",response.getCode(),response.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

}
