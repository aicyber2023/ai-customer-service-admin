package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class QuestionAnswerVo {
    private Long digitalEmployeeId;
    private String question;
    private String answer;

}