package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.ByteArrayTypeHandler;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数字员工模板对象 biz_digital_employee_template
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_digital_employee_template")
public class BizDigitalEmployeeTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板头像
     */
    @Excel(name = "模板头像")
    @TableField(typeHandler = ByteArrayTypeHandler.class)
    private byte[] avatar;

    /**
     * 模板头像ContentType
     */
    @Excel(name = "模板头像ContentType")
    private String avatarContentType;

    /**
     * 模板名称
     */
    @Excel(name = "模板名称")
    private String name;

    /**
     * 模型名称
     */
    @Excel(name = "模型名称")
    private String model;

    /**
     * temperature
     */
    private BigDecimal temperature;

    /**
     * 最多单词/标记数
     */
    @Excel(name = "最多单词/标记数")
    private Long maxTokens;

    /**
     * 存在惩罚
     */
    @Excel(name = "存在惩罚")
    private BigDecimal presencePenalty;

    /**
     * 频率惩罚
     */
    @Excel(name = "频率惩罚")
    private BigDecimal frequencyPenalty;

    /**
     * sendMemory
     */
    private Integer sendMemory;

    /**
     * historyMessageCount
     */
    private Long historyMessageCount;

    /**
     * compressMessageLengthThreshold
     */
    private Long compressMessageLengthThreshold;

    /**
     * lang
     */
    private String lang;

    /**
     * builtin
     */
    private Boolean builtin;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /** 招呼语 */
    private String greeting;

    /** 介绍 */
    private String introduction;

    /**
     * 公司头像
     */
    @Excel(name = "公司头像")
    @TableField(typeHandler = ByteArrayTypeHandler.class)
    private byte[] companyAvatar;

    /** 公司头像ContentType */
    private String companyAvatarContentType;

    /** 公司名称 */
    private String companyName;

    @Excel(name = "用户id")
    private Long userId;

    /** 企业id */
    @Excel(name = "企业id")
    private Long deptId;

    /** 是否主动打招呼 */
    private Integer proactivelyGreet;

    /** contextList */
    @TableField(exist = false)
    private List<BizDigitalEmployeeTemplateContext> context;

    public Integer getProactivelyGreet() {
        if (proactivelyGreet == null) {
            return 0;
        }
        return  proactivelyGreet;
    }

}
