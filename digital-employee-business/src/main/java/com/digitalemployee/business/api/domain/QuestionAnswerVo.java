package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class QuestionAnswerVo {
    private String question;
    private String answer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAnswerVo questionAnswerVo = (QuestionAnswerVo) o;
        return Objects.equals(question, questionAnswerVo.question) && Objects.equals(answer, questionAnswerVo.answer) ;
    }

    public QuestionAnswerVo() {
    }

    public QuestionAnswerVo(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public int hashCode() {
        int result = 5;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }
}