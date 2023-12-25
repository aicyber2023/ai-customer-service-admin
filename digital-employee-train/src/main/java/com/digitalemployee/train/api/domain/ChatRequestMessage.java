package com.digitalemployee.train.api.domain;

import lombok.Data;

@Data
public class ChatRequestMessage {

    private String role;

    private String content;

    public ChatRequestMessage() {}

    public ChatRequestMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public static ChatRequestMessage system(String content) {
        return new ChatRequestMessage("system", content);
    }

    public static ChatRequestMessage user(String content) {
        return new ChatRequestMessage("user", content);
    }

    public static ChatRequestMessage assistant(String content) {
        return new ChatRequestMessage("assistant", content);
    }

}
