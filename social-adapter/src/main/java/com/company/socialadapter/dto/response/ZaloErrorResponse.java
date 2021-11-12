package com.company.socialadapter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZaloErrorResponse {
    private String message;
    private int error;
}
