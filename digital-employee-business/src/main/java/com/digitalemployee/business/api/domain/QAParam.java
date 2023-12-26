package com.digitalemployee.business.api.domain;

import com.digitalemployee.business.domain.BizSessionRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class QAParam {

    public static final double DEFAULT_RADIUS = 0.8;
    public static final double DEFAULT_TEMPERATURE = 2.0;
    public static final double DEFAULT_PRESENCE_PENALTY = 1.5;
    public static final double DEFAULT_FREQUENCY_PENALTY = 0.8;
    /** 集合名称 */
    private String collection;
    /** 问题 */
    private String question;

    private Double radius;
    private Double temperature;
    private Double presence_penalty;
    private Double frequency_penalty;

    private List<History> history;

    public QAParam() {
    }

    public QAParam(String collection, String question, Double radius, Double temperature, Double presence_penalty, Double frequency_penalty) {
        this.collection = collection;
        this.question = question;

        this.radius = radius;
        this.temperature = temperature;
        this.presence_penalty = presence_penalty;
        this.frequency_penalty = frequency_penalty;

        this.history = new ArrayList<>();
    }

    public QAParam(String collection, String question, BigDecimal radius, BigDecimal temperature, BigDecimal presence_penalty, BigDecimal frequency_penalty) {
        this.collection = collection;
        this.question = question;

        this.radius = radius == null ? DEFAULT_RADIUS : radius.doubleValue();
        this.temperature = temperature == null ? DEFAULT_TEMPERATURE : temperature.doubleValue();
        this.presence_penalty = presence_penalty == null ? DEFAULT_PRESENCE_PENALTY : presence_penalty.doubleValue();
        this.frequency_penalty = frequency_penalty == null ? DEFAULT_FREQUENCY_PENALTY : frequency_penalty.doubleValue();

        this.history = new ArrayList<>();
    }

    public void appendHistory(BizSessionRecord record) {
        history.add(new History(record.getInputText(), record.getOutputText()));
    }

    public void appendHistoryList(List<BizSessionRecord> sessionRecords) {
        sessionRecords.forEach(this::appendHistory);
    }

    @Data
    static class History {
        private String question;
        private String answer;

        public History(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    public static QAParam initQAParam(String collection, String question) {
        return new QAParam(collection, question, DEFAULT_RADIUS, DEFAULT_TEMPERATURE, DEFAULT_PRESENCE_PENALTY, DEFAULT_FREQUENCY_PENALTY);
    }
}
