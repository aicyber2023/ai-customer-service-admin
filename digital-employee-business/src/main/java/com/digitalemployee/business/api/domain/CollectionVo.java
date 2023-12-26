package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class CollectionVo {
    private String collection;
    private List<String> ids;
}
