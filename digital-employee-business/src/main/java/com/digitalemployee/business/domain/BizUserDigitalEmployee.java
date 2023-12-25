package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户数字员工对象 biz_user_digital_employee
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@TableName("biz_user_digital_employee")
public class BizUserDigitalEmployee implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 数字员工ID
     */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

}
