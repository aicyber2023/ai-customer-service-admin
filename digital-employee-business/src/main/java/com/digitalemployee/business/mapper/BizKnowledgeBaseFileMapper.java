package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizKnowledgeBaseFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库文档Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizKnowledgeBaseFileMapper extends BaseMapper<BizKnowledgeBaseFile> {
    /**
     * 查询知识库文档
     *
     * @param id 知识库文档主键
     * @return 知识库文档
     */
    BizKnowledgeBaseFile selectBizKnowledgeBaseFileById(Long id);

    /**
     * 查询知识库文档列表
     *
     * @param bizKnowledgeBaseFile 知识库文档
     * @return 知识库文档集合
     */
    List<BizKnowledgeBaseFile> selectBizKnowledgeBaseFileList(BizKnowledgeBaseFile bizKnowledgeBaseFile);

    /**
     * 新增知识库文档
     *
     * @param bizKnowledgeBaseFile 知识库文档
     * @return 结果
     */
    int insertBizKnowledgeBaseFile(BizKnowledgeBaseFile bizKnowledgeBaseFile);

    /**
     * 修改知识库文档
     *
     * @param bizKnowledgeBaseFile 知识库文档
     * @return 结果
     */
    int updateBizKnowledgeBaseFile(BizKnowledgeBaseFile bizKnowledgeBaseFile);

    /**
     * 删除知识库文档
     *
     * @param id 知识库文档主键
     * @return 结果
     */
    int deleteBizKnowledgeBaseFileById(Long id);

    /**
     * 批量删除知识库文档
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizKnowledgeBaseFileByIds(Long[] ids);

    Long selectSysUserKbDocCount(@Param("sysUserId") Long sysUserId);


    BizKnowledgeBaseFile selectBizKnowledgeBaseFileIds(BizKnowledgeBaseFile bizKnowledgeBaseFile);
}
