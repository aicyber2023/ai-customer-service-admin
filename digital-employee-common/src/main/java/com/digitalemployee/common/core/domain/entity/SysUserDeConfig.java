package com.digitalemployee.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户数字员工配置对象 sys_user_de_config
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@TableName("sys_user_de_config")
public class SysUserDeConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统用户ID
     */
    @Excel(name = "系统用户ID")
    private Long sysUserId;

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

    /** 配置开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /** 配置过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireDate;

    /** 配置状态 0-生效 1-失效 */
    private Integer status;

    /** 配置来源类型 0-试用 1-订单 */
    private Integer type;

    /** 天数 */
    private Integer days;

    /** 订单ID */
    private Long orderId;

}
