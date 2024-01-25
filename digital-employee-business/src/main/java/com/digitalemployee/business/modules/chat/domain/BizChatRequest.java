package com.digitalemployee.business.modules.chat.domain;

import lombok.Data;

@Data
public class BizChatRequest {

    private Long digitalEmployeeId;

    private String apiKey;

    private String userInput;

}
