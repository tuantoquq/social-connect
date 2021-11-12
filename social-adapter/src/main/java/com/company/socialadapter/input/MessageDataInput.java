package com.company.socialadapter.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDataInput {
    private String channel;

    private String receiveId;

    private String senderId;

    private String text;

}
