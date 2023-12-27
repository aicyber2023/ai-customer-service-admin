package com.digitalemployee.business.modules.chatsession.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Long recordAmount;

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

}
