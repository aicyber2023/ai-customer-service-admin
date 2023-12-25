package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户数字员工上下文对象 biz_digital_employee_context
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@TableName("biz_digital_employee_context")
public class BizDigitalEmployeeContext implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private Long templateId;

    /**
     * 数字员工ID
     */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

    /**
     * 角色
     */
    @Excel(name = "角色")
    private String role;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;



}
