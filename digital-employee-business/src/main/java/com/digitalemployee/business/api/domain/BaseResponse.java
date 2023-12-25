package com.digitalemployee.business.api.domain;

import lombok.Data;

@Data
public class BaseResponse {

    /**
     * 是否成功
     */
    private Boolean successful;
    /**
     * 消息
     */
    private String message;

}
