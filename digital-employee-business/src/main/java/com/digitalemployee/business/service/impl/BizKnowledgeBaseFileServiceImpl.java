package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizKnowledgeBaseFile;
import com.digitalemployee.business.mapper.BizKnowledgeBaseFileMapper;
import com.digitalemployee.business.service.IBizKnowledgeBaseFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 知识库文档Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizKnowledgeBaseFileServiceImpl extends ServiceImpl<BizKnowledgeBaseFileMapper, BizKnowledgeBaseFile> implements IBizKnowledgeBaseFileService {

    private final BizKnowledgeBaseFileMapper bizKnowledgeBaseFileMapper;

    /**
     * 查询知识库文档
     *
     * @param id 知识库文档主键
     * @return 知识库文档
     */
    @Override
    public BizKnowledgeBaseFile selectBizKnowledgeBaseFileById(Long id) {
        return bizKnowledgeBaseFileMapper.selectBizKnowledgeBaseFileById(id);
    }

    /**
     * 查询知识库文档列表
     *
     * @param bizKnowledgeBaseFile 知识库文档
     * @return 知识库文档
     */
    @Override
    public List<BizKnowledgeBaseFile> selectBizKnowledgeBaseFileList(BizKnowledgeBaseFile bizKnowledgeBaseFile) {
        return bizKnowledgeBaseFileMapper.selectBizKnowledgeBaseFileList(bizKnowledgeBaseFile);
    }

    /**
     * 新增知识库文档
     *
     * @param bizKnowledgeBaseFile 知识库文档
     * @return 结果
     */
    @Override
    public int insertBizKnowledgeBaseFile(BizKnowledgeBaseFile bizKnowledgeBaseFile) {
        return bizKnowledgeBaseFileMapper.insertBizKnowledgeBaseFile(bizKnowledgeBaseFile);
    }

    /**
     * 修改知识库文档
     *
     * @param bizKnowledgeBaseFile 知识库文档
     * @return 结果
     */
    @Override
    public int updateBizKnowledgeBaseFile(BizKnowledgeBaseFile bizKnowledgeBaseFile) {
        return bizKnowledgeBaseFileMapper.updateBizKnowledgeBaseFile(bizKnowledgeBaseFile);
    }

    /**
     * 批量删除知识库文档
     *
     * @param ids 需要删除的知识库文档主键
     * @return 结果
     */
    @Override
    public int deleteBizKnowledgeBaseFileByIds(Long[] ids) {
        return bizKnowledgeBaseFileMapper.deleteBizKnowledgeBaseFileByIds(ids);
    }

    /**
     * 删除知识库文档信息
     *
     * @param id 知识库文档主键
     * @return 结果
     */
    @Override
    public int deleteBizKnowledgeBaseFileById(Long id) {
        return bizKnowledgeBaseFileMapper.deleteBizKnowledgeBaseFileById(id);
    }

    @Override
    public BizKnowledgeBaseFile selectBizKnowledgeBaseFileIds(BizKnowledgeBaseFile bizKnowledgeBaseFile) {
        return bizKnowledgeBaseFileMapper.selectBizKnowledgeBaseFileIds(bizKnowledgeBaseFile);
    }
}
