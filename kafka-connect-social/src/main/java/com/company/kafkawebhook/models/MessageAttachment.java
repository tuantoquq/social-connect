package com.company.kafkawebhook.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class MessageAttachment extends MessageBase{
    private String messageId;
    private String text; // text content
    private Object attachment; //include type(image,audio,..), url, ..
    public MessageAttachment(String channel, Long time, String senderId, String senderName, String senderProfilePicture, String senderGender, String recipientId,
                             String messageId, String text, Object attachment){
        super(channel, time, senderId, senderName, senderProfilePicture, senderGender, recipientId);
        this.attachment = attachment;
        this.messageId = messageId;
        this.text = text;
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
//                ", \"text\":\"" + text + "\"" +
//                ", \"attachment\":" + attachment +
//                "}";
//    }
}
