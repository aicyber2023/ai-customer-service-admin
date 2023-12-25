package com.digitalemployee.business.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QAResponse extends BaseResponse{
    /**
     * 回答
     */
    private String answer;
    /**
     * 是否来自知识库
     */
    private Boolean from_knowledge_base;
    /**
     * 大模型prompt
     */
    private String prompt;

    /** 模型名称 */
    private String model_name;

}
