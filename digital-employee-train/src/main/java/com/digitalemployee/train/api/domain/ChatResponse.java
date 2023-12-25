package com.digitalemployee.train.api.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatResponse {

    private String id;

    private String object;

    private Long created;

    private String model;

    private Map<String, Object> usage;

    private List<ChatChoice> choices;

}
