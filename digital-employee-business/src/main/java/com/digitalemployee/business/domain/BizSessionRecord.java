package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 对话详单对象 biz_session_record
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_session_record")
public class BizSessionRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 角色ID
     */
    @Excel(name = "角色ID")
    private Long digitalEmployeeId;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /**
     * 返回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "返回时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnTime;

    /**
     * 响应间隔
     */
    @Excel(name = "响应间隔")
    private Long responseInterval;

    /**
     * 输入文本
     */
    @Excel(name = "输入文本")
    private String inputText;

    /**
     * 输出文本
     */
    @Excel(name = "输出文本")
    private String outputText;

    /**
     * Token数
     */
    @Excel(name = "Token数")
    private Long tokens;

    /**
     * 是否缓存（0：是 1：否）
     */
    @Excel(name = "是否缓存", readConverterExp = "0=：是,1=：否")
    private Long cacheFlag;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Long status;

    /**
     * 错误码
     */
    @Excel(name = "错误码")
    private Long errorCode;

    private String ip;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private Long sysUserId;
}
