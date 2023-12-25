package com.digitalemployee.business.domain;

import com.digitalemployee.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalemployee.common.annotation.Excel;

/**
 * 问答库文档对象 biz_question_file
 * 
 * @author aicyber
 * @date 2023-11-29
 */
@Data
@TableName("biz_question_file")
public class BizQuestionFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 数字员工ID */
    @Excel(name = "数字员工ID")
    private Long digitalEmployeeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long questionId;

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

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDigitalEmployeeId(Long digitalEmployeeId) 
    {
        this.digitalEmployeeId = digitalEmployeeId;
    }

    public Long getDigitalEmployeeId() 
    {
        return digitalEmployeeId;
    }
    public void setQuestionId(Long questionId) 
    {
        this.questionId = questionId;
    }

    public Long getQuestionId() 
    {
        return questionId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }


}
