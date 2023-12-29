package com.digitalemployee.business.domain;

import com.digitalemployee.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 问答库对象 biz_question_answer
 * 
 * @author aicyber
 * @date 2023-11-27
 */
@Data
@TableName("biz_question_answer")
public class BizQuestionAnswer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 数字员工ID */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

    /** 数字员工ID */
    @Excel(name = "远程知识库名称")
    private String collectionId;

    /** 问题 */
    @Excel(name = "问题")
    private String question;

    /** 回答 */
    @Excel(name = "回答")
    private String answer;

    /** 状态 0草稿 1发布 2逻辑删除 */
    @Excel(name = "状态 0草稿 1发布 2逻辑删除")
    private Integer status;

    /** 创建方式 */
    @Excel(name = "创建方式")
    private Integer createType;

    /** 相似问 */

    private List<BizSimilarityQuestion> similarityQuestionList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDigitalEmployeeId(Long digitalEmployeeId) 
    {
        this.digitalEmployeeId = digitalEmployeeId;
    }

    public Long getDigitalEmployeeId() 
    {
        return digitalEmployeeId;
    }
    public void setQuestion(String question) 
    {
        this.question = question;
    }

    public String getQuestion() 
    {
        return question;
    }
    public void setAnswer(String answer) 
    {
        this.answer = answer;
    }

    public String getAnswer() 
    {
        return answer;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setCreateType(Integer createType) 
    {
        this.createType = createType;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getCreateType()
    {
        return createType;
    }


    public List<BizSimilarityQuestion> getSimilarityQuestionList() {
        return similarityQuestionList;
    }

    public void setSimilarityQuestionList(List<BizSimilarityQuestion> similarityQuestionList) {
        this.similarityQuestionList = similarityQuestionList;
    }
}
