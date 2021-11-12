package com.company.kafkawebhook.controller;

import com.company.kafkawebhook.engine.FacebookProducer;
import com.company.kafkawebhook.engine.ZaloProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/kafka")
public class KafkaController {
    @Autowired
    private FacebookProducer facebookProducer;

    @Autowired
    private ZaloProducer zaloProducer;

    @PostMapping(
            path = "/facebook/events"
    )
    public void sendFaceBookMessage(@RequestHeader String message){
        this.facebookProducer.sendMessage(message);
    }

    @PostMapping(
            path = "/zalo/events"
    )
    public void sendZaloMessage(@RequestHeader String message){
        this.zaloProducer.sendMessage(message);
    }
}
