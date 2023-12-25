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
 * 数字员工模板上下文对象 biz_digital_employee_template_context
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@TableName("biz_digital_employee_template_context")
public class BizDigitalEmployeeTemplateContext implements Serializable {
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
