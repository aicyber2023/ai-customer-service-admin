package com.digitalemployee.business.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.digitalemployee.common.annotation.Excel;

import java.util.List;

public class QuestionAnswersVo  implements Cloneable{
    /** 问答ID */
    private Long questionAnswerId;

    /** 数字员工ID */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

    /** 问题 */
    @ExcelProperty("标准问")
    @Excel(name = "问题")
    private String question;

    /** 回答 */
    @ExcelProperty("回答")
    @Excel(name = "回答")
    private String answer;
    /** 相似问 */
    @ExcelProperty("相似问")
    @Excel(name = "相似问")
    private List<String> similarityQuestion;

    public Long getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(Long questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }

    public Long getDigitalEmployeeId() {
        return digitalEmployeeId;
    }

    public void setDigitalEmployeeId(Long digitalEmployeeId) {
        this.digitalEmployeeId = digitalEmployeeId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getSimilarityQuestion() {
        return similarityQuestion;
    }

    public void setSimilarityQuestion(List<String> similarityQuestion) {
        this.similarityQuestion = similarityQuestion;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
