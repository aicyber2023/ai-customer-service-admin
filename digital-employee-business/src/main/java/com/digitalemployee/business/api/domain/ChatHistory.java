package com.digitalemployee.business.api.domain;

import lombok.Data;

@Data
public class ChatHistory {

    private String question;
    private String answer;

    public ChatHistory(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

}
