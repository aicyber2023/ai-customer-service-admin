package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单对象 biz_order
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_order")
public class BizOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long sysUserId;

    /**
     * 产品ID
     */
    @Excel(name = "产品ID")
    private Long productId;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal amount;

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
