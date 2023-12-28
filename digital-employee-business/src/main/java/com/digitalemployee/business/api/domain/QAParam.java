package com.digitalemployee.business.api.domain;


import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class QAParam extends BaseModelParam{

    public static final double DEFAULT_RADIUS = 0.8;

    /**
     * 集合名称
     */
    private String collection;
    /**
     * 问题
     */
    private String question;

    private Double radius;

    public QAParam() {
    }

    public QAParam(String collection, String question, Double radius, Double temperature, Double presence_penalty, Double frequency_penalty) {
        this.collection = collection;
        this.question = question;

        this.radius = radius;
        this.temperature = temperature;
        this.presence_penalty = presence_penalty;
        this.frequency_penalty = frequency_penalty;
    }

    public QAParam(String collection, String question, BigDecimal radius, BigDecimal temperature, BigDecimal presence_penalty, BigDecimal frequency_penalty) {
        super(temperature, presence_penalty, frequency_penalty);
        this.collection = collection;
        this.question = question;
        this.radius = radius == null ? DEFAULT_RADIUS : radius.doubleValue();
    }

    public static QAParam initQAParam(String collection, String question) {
        return new QAParam(collection, question, DEFAULT_RADIUS, DEFAULT_TEMPERATURE, DEFAULT_PRESENCE_PENALTY, DEFAULT_FREQUENCY_PENALTY);
    }
}
