package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizKnowledgeBaseFile;

import java.util.List;

/**
 * 知识库文档Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizKnowledgeBaseFileService extends IService<BizKnowledgeBaseFile> {
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
     * 批量删除知识库文档
     *
     * @param ids 需要删除的知识库文档主键集合
     * @return 结果
     */
    int deleteBizKnowledgeBaseFileByIds(Long[] ids);

    /**
     * 删除知识库文档信息
     *
     * @param id 知识库文档主键
     * @return 结果
     */
    int deleteBizKnowledgeBaseFileById(Long id);

    BizKnowledgeBaseFile selectBizKnowledgeBaseFileIds(BizKnowledgeBaseFile bizKnowledgeBaseFile);

}
