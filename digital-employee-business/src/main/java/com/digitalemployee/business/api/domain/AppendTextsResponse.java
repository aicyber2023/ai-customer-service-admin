package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.List;
@Data
public class AppendTextsResponse extends BaseResponse{
    /**
     * 向量id数组
     */
    private List<String> ids;
}
