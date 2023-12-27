package com.digitalemployee.business.modules.de.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import lombok.Data;

/**
 * 数字员工模板话术对象 biz_digital_employee_template_procedure
 *
 * @author aicyber
 * @date 2023-12-26
 */
@Data
@TableName("biz_digital_employee_template_procedure")
public class BizDigitalEmployeeTemplateProcedure {

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
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 类型
     */
    @Excel(name = "类型")
    private Long type;

    /**
     * 是否启用 0-未启用 1-启用
     */
    @Excel(name = "是否启用 0-未启用 1-启用")
    private Integer enable;

}
