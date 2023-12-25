package com.digitalemployee.business.api.domain;

import com.digitalemployee.business.domain.BizSessionRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QAParam {

    private String collection;
    private String question;

    private Double radius;
    private Double temperature;
    private Double presence_penalty;
    private Double frequency_penalty;

    private List<History> history;

    public QAParam() {
    }

    public QAParam(String collection, String question, Double radius, Double temperature, Double presence_penalty, Double frequency_penalty) {
        this.radius = radius;
        this.temperature = temperature;
        this.presence_penalty = presence_penalty;
        this.frequency_penalty = frequency_penalty;
        this.collection = collection;
        this.question = question;
        this.history = new ArrayList<>();
    }

    public static QAParam initQAParam(String collection, String question) {
        final double default_radius = 0.8;
        final double default_temperature = 2.0;
        final double default_presence_penalty = 1.5;
        final double default_frequency_penalty = 0.8;
        return new QAParam(collection, question, default_radius, default_temperature, default_presence_penalty, default_frequency_penalty);
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
}
