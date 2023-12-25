package com.digitalemployee.train.utils.doc.domain;

import lombok.Data;

import java.util.List;

/**
 * PDF结构解析用
 *
 * @author aicyber
 */

@Data
public class PDFView {

    /**
     * extraction_method
     */

    private String extraction_method;

    /**
     * top
     */

    private double top;

    /**
     * left
     */

    private double left;

    /**
     * width
     */

    private double width;

    /**
     * height
     */

    private double height;

    /**
     * right
     */

    private double right;

    /**
     * bottom
     */

    private double bottom;

    /**
     * data
     */

    private List<List<DataBean>> data;


}
