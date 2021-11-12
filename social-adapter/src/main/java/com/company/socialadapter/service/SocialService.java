package com.company.socialadapter.service;

import com.company.socialadapter.dto.builder.Response;
import com.company.socialadapter.input.MessageDataInput;

public interface SocialService {
    Response sendText(MessageDataInput mes);
}
