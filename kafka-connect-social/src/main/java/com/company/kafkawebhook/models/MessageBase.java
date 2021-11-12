package com.company.kafkawebhook.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class MessageBase {
    protected String channel;// from Facebook or Zalo
    protected Long time;//time send message
    protected String senderId; //page-scope id of user for page
    protected String senderName; //facebook's username
    protected String senderProfilePicture;//facebook's avatar url
    protected String senderGender;// user's gender
    protected String recipientId;// id of page

    public MessageBase(String channel, Long time, String senderId, String senderName, String senderProfilePicture, String senderGender, String recipientId) {
        this.channel = channel;
        this.time = time;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfilePicture = senderProfilePicture;
        this.senderGender = senderGender;
        this.recipientId = recipientId;
    }
}
