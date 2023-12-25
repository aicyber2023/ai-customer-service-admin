package com.digitalemployee.train.api.domain;

import lombok.Data;

@Data
public class ChatChoice {

    private Integer index;

    private String finish_reason;

    private ChatResponseMessage message;

}
