package com.digitalemployee.business.domain;

import com.digitalemployee.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;

import java.util.Date;

/**
 * 问答库文档对象 biz_question_file
 * 
 * @author aicyber
 * @date 2023-11-29
 */
@Data
@TableName("biz_question_file")
public class BizQuestionFile
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 数字员工ID */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String questionId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
