package com.company.socialadapter.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookResponse {
    @JsonProperty("userId")
    private String recipient_id;

    @JsonProperty("messageId")
    private String message_id;
}
