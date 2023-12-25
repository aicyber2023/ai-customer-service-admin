package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.domain.BizKnowledgeBaseFile;
import com.digitalemployee.common.core.domain.AjaxResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 知识库Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizKnowledgeBaseService extends IService<BizKnowledgeBase> {
    /**
     * 查询知识库
     *
     * @param id 知识库主键
     * @return 知识库
     */
    BizKnowledgeBase selectBizKnowledgeBaseById(Long id);

    /**
     * 查询知识库列表
     *
     * @param bizKnowledgeBase 知识库
     * @return 知识库集合
     */
    List<BizKnowledgeBase> selectBizKnowledgeBaseList(BizKnowledgeBase bizKnowledgeBase);

    /**
     * 新增知识库
     *
     * @param bizKnowledgeBase 知识库
     * @return 结果
     */
    int insertBizKnowledgeBase(BizKnowledgeBase bizKnowledgeBase);

    /**
     * 修改知识库
     *
     * @param bizKnowledgeBase 知识库
     * @return 结果
     */
    int updateBizKnowledgeBase(BizKnowledgeBase bizKnowledgeBase);

    /**
     * 批量删除知识库
     *
     * @param ids 需要删除的知识库主键集合
     * @return 结果
     */
    int deleteBizKnowledgeBaseByIds(Long[] ids);

    /**
     * 删除知识库信息
     *
     * @param id 知识库主键
     * @return 结果
     */
    int deleteBizKnowledgeBaseById(Long id);

//    Boolean uploadFile(Long knowledgeBaseId, MultipartFile[] files);
    AjaxResult uploadFile(Long knowledgeBaseId, MultipartFile[] files);

    Boolean appendFileToKnowledgeBase(Long knowledgeFileId);

    @Async
    void appendFile(BizKnowledgeBaseFile knowledgeBaseFile, String collectionName);

    int removeFile(Long[] ids);

    BizKnowledgeBase initKnowledgeBase(BizDigitalEmployee bizDigitalEmployee);

    List<BizKnowledgeBaseFile> selectKbFileList(BizKnowledgeBaseFile knowledgeBaseFile);

    Long getKnowledgeBaseIdByDeId(Long digitalEmployeeId);

//    Boolean removeFile(Long knowledgeFileId);
}
