package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 知识库对象 biz_knowledge_base
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_knowledge_base")
public class BizKnowledgeBase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客户ID
     */
    @Excel(name = "客户ID")
    private Long sysUserId;

    /** 远程知识库名称，不可修改，唯一 */
    private String collectionName;

    /**
     * 知识库名称
     */
    @Excel(name = "知识库名称")
    private String name;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    @TableField(exist = false)
    private List<BizKnowledgeBaseFile> fileList;

    @Excel(name = "用户id")
    private Long userId;

    /** 企业id */
    @Excel(name = "企业id")
    private Long deptId;

}
