package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 产品对象 biz_product
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_product")
public class BizProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String name;

    /**
     * 展示原价
     */
    @Excel(name = "展示原价")
    private BigDecimal priceShow;

    /**
     * 实际单价
     */
    @Excel(name = "实际单价")
    private BigDecimal priceReal;

    /**
     * 数字员工个数
     */
    @Excel(name = "数字员工个数")
    private Long employeeAmount;

    /**
     * 单个数字员工对话数
     */
    @Excel(name = "单个数字员工对话数")
    private Long sessionAmount;

    /**
     * 知识库个数
     */
    @Excel(name = "知识库个数")
    private Long knowledgeBaseAmount;

    /**
     * 可上传文档数
     */
    @Excel(name = "可上传文档数")
    private Long knowledgeBaseDocAmount;

    /**
     * 文档字数
     */
    @Excel(name = "文档字数")
    private Long knowledgeBaseDocWordAmount;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /** 天数 */
    private Integer days;
}
