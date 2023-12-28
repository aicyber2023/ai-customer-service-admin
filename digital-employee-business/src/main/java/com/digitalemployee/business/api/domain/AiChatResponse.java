package com.digitalemployee.business.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AiChatResponse extends BaseResponse{

    /** 对话输出 */
    private String output;

}
