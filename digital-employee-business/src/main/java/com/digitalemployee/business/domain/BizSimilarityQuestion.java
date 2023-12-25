package com.digitalemployee.business.domain;

import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;

/**
 * 相似问对象 biz_similarity_question
 * 
 * @author aicyber
 * @date 2023-11-27
 */
@Data
@TableName("biz_similarity_question")
public class BizSimilarityQuestion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 问答ID */
    @Excel(name = "问答ID")
    private Long questionAnswerId;

    /** 相似问 */
    @Excel(name = "相似问")
    private String similarityQuestion;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setQuestionAnswerId(Long questionAnswerId) 
    {
        this.questionAnswerId = questionAnswerId;
    }

    public Long getQuestionAnswerId() 
    {
        return questionAnswerId;
    }
    public void setSimilarityQuestion(String similarityQuestion) 
    {
        this.similarityQuestion = similarityQuestion;
    }

    public String getSimilarityQuestion() 
    {
        return similarityQuestion;
    }


}
