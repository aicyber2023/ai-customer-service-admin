package com.digitalemployee.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 知识库文档对象 biz_knowledge_base_file
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Data
@TableName("biz_knowledge_base_file")
public class BizKnowledgeBaseFile implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 知识库ID
     */
    @Excel(name = "知识库ID")
    private Long knowledgeBaseId;

    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    private String fileName;

    /**
     * 文件路径
     */
    @Excel(name = "文件路径")
    private String filePath;

    /** 状态 0 未添加到知识库 1 已添加到知识库 2 添加中 3 添加时发生异常 */
    private Integer status;

    private String vectorIds;

    private Integer wordAmount;

    /**
     * 文件大小(k)
     */
    private Long fileSize;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人ID
     */
    private String createBy;

    @TableField(exist = false)
    private Long digitalEmployeeId;
    @TableField(exist = false)
    private Date startDateTime;
    @TableField(exist = false)
    private Date endDateTime;


}
