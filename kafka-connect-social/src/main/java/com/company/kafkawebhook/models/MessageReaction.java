package com.company.kafkawebhook.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MessageReaction extends MessageBase{
    private String messageId;
    private String action; // react or unreact
    private String emoji; // icon
    private String reaction;// icon's main
    public MessageReaction(String channel, Long time, String senderId, String senderName, String senderProfilePicture, String senderGender, String recipientId,
                             String messageId, String action, String emoji, String reaction){
        super(channel, time, senderId, senderName, senderProfilePicture, senderGender, recipientId);
        this.action = action;
        this.messageId = messageId;
        this.emoji = emoji;
        this.reaction = reaction;
    }

//    @Override
//    public String toString() {
//        return "{" +
//                "\"sendFrom\":\"" + sendFrom + "\"" +
//                ", \"time\":" + time +
//                ", \"senderId\":\"" + senderId + "\"" +
//                ", \"senderName\":\"" + senderName + "\"" +
//                ", \"senderProfilePicture\":\"" + senderProfilePicture + "\"" +
//                ", \"senderGender\":\"" + senderGender + "\"" +
//                ", \"recipientId\":\"" + recipientId + "\"" +
//                ", \"messageId\":\"" + messageId + "\"" +
//                ", \"action\":\"" + action + "\"" +
//                ", \"emoji\":\"" + emoji + "\"" +
//                ", \"reaction\":\"" + reaction + "\"" +
//                "}";
//    }
}
