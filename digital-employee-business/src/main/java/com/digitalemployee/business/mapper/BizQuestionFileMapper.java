package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizQuestionFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问答库文档Mapper接口
 * 
 * @author aicyber
 * @date 2023-11-29
 */
@Mapper
public interface BizQuestionFileMapper  extends BaseMapper<BizQuestionFile>
{
    /**
     * 查询问答库文档
     * 
     * @param id 问答库文档主键
     * @return 问答库文档
     */
    BizQuestionFile selectBizQuestionFileById(Long id);

    /**
     * 查询问答库文档列表
     * 
     * @param bizQuestionFile 问答库文档
     * @return 问答库文档集合
     */
    List<BizQuestionFile> selectBizQuestionFileList(BizQuestionFile bizQuestionFile);

    /**
     * 根据条件查询单条问答库数据
     * @param bizQuestionFile
     * @return
     */
    BizQuestionFile selectOneBizQuestionFile(BizQuestionFile bizQuestionFile);

    /**
     * 新增问答库文档
     * 
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    int insertBizQuestionFile(BizQuestionFile bizQuestionFile);

    /**
     * 修改问答库文档
     * 
     * @param bizQuestionFile 问答库文档
     * @return 结果
     */
    int updateBizQuestionFile(BizQuestionFile bizQuestionFile);

    /**
     * 删除问答库文档
     * 
     * @param id 问答库文档主键
     * @return 结果
     */
    int deleteBizQuestionFileById(Long id);

    /**
     * 批量删除问答库文档
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizQuestionFileByIds(Long[] ids);

    long selectSysUserKbQaDocCount(@Param("sysUserId") Long sysUserId);
}
