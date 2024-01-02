package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class CollectionParam {
    private String collection;
    private List<String> ids;
}
