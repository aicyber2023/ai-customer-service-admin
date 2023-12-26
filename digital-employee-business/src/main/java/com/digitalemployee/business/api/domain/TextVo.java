package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class TextVo {
    private String collection;
    private List<String> text_list;
}
