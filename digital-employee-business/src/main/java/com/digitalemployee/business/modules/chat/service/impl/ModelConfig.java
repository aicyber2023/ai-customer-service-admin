package com.digitalemployee.business.modules.chat.service.impl;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ModelConfig {

    private String model;
    private BigDecimal temperature;
    private Long maxTokens;
    private BigDecimal presencePenalty;
    private BigDecimal frequencyPenalty;
    private Integer sendMemory;
    private Long historyMessageCount;
    private Long compressMessageLengthThreshold;

}
