package com.digitalemployee.business.modules.chatsession.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 会话session对象 biz_session
 *
 * @author aicyber
 * @date 2023-12-27
 */
@Data
@TableName("biz_session")
@EqualsAndHashCode(callSuper = true)
public class BizSession extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数字员工ID
     */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

    /**
     * 会话名称
     */
    @Excel(name = "会话名称")
    private String topic;

    /**
     * 会话类型 0-测试 1-匿名
     */
    @Excel(name = "会话类型 0-测试 1-匿名")
    private Integer sessionType;

    /**
     * 测试用户id
     */
    @Excel(name = "测试用户id")
    private Long testUserId;

    /**
     * ip
     */
    @Excel(name = "ip")
    private String ip;

    /**
     * amount
     */
    @Excel(name = "amount")
    private String cookie;

    /**
     * 对话条数
     */
    @Excel(name = "对话条数")
    private Integer recordAmount;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 企业id
     */
    @Excel(name = "企业id")
    private Long deptId;

    @TableField(exist = false)
    private List<BizSessionRecord> recordList;

    /**  */
    @TableField(exist = false)
    private String testUserName;

    /** 数字客服名称 */
    @TableField(exist = false)
    private String deName;

    // 查询条件 START ----------

    // 对话条数范围
    @TableField(exist = false)
    private Integer recordAmountStart;
    @TableField(exist = false)
    private Integer recordAmountEnd;

    // 创建时间范围
    @TableField(exist = false)
    private Date createTimeStart;
    @TableField(exist = false)
    private Date createTimeEnd;

    // 最近对话时间范围
    @TableField(exist = false)
    private Date updateTimeStart;
    @TableField(exist = false)
    private Date updateTimeEnd;

    // 查询条件 END ----------
}
