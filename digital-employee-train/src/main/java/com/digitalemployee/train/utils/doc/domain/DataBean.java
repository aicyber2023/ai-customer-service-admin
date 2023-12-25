package com.digitalemployee.train.utils.doc.domain;

import lombok.Data;

/**
 * PDF结构解析用（DataBean）
 *
 * @author aicyber
 */

@Data
public class DataBean {

    /**
     * top
     */

    private int top;

    /**
     * left
     */

    private int left;

    /**
     * width
     */

    private int width;

    /**
     * height
     */

    private int height;

    /**
     * text
     */

    private String text;


    /**
     * 判断是否是空层
     */

    public boolean isEmpty() {
        // 所有值都不存在的时候，返回TRUE
        if (top == 0 && left == 0 && width == 0 && height == 0 && "".equals(text)) {
            return true;
        }
        return false;
    }
}
