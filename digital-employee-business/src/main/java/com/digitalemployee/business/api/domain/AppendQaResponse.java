package com.digitalemployee.business.api.domain;

import lombok.Data;

@Data
public class AppendQaResponse extends BaseResponse{
    /**
     * 向量id数组
     */
    private String id;
}
