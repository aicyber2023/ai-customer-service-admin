package com.digitalemployee.train.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {

    private List<ChatRequestMessage> messages;

    private Boolean stream;

    private String model;

    private Double temperature;

    private Double presence_penalty;

    private Double frequency_penalty;

    private Double top_p;

    public ChatRequest() {}

    public ChatRequest(Boolean stream, String model, Double temperature, Double presence_penalty, Double frequency_penalty, Double top_p) {
        this.stream = stream;
        this.model = model;
        this.temperature = temperature;
        this.presence_penalty = presence_penalty;
        this.frequency_penalty = frequency_penalty;
        this.top_p = top_p;
    }

    public static ChatRequest getDefaultChatRequest() {
        return new ChatRequest(false, "gpt-3.5-turbo", 0.5, 0d, 0d, 1d);
    }

}
