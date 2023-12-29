package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class CollectionVo {
    private Long digitalEmployeeId;
    private List<String> ids;
}
