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

/**
 * 用户账户对象 biz_user
 * 
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_user")
public class BizUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long sysUserId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 头像 */
    @Excel(name = "头像")
    @TableField(typeHandler = ByteArrayTypeHandler.class)
    private byte[] avatar;

    /** 头像ContentType */
    @Excel(name = "头像ContentType")
    private String avatarContentType;

    /** 用户名 */
    private String name;

    @Excel(name = "用户id")
    private Long userId;

    /** 企业id */
    @Excel(name = "企业id")
    private Long deptId;
}
