package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizSimilarityQuestion;

import java.util.List;

/**
 * 相似问Service接口
 * 
 * @author aicyber
 * @date 2023-11-27
 */
public interface IBizSimilarityQuestionService extends IService<BizSimilarityQuestion>
{
    /**
     * 查询相似问
     * 
     * @param id 相似问主键
     * @return 相似问
     */
    BizSimilarityQuestion selectBizSimilarityQuestionById(Long id);

    /**
     * 查询相似问列表
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 相似问集合
     */
    List<BizSimilarityQuestion> selectBizSimilarityQuestionList(BizSimilarityQuestion bizSimilarityQuestion);

    /**
     * 新增相似问
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 结果
     */
    int insertBizSimilarityQuestion(BizSimilarityQuestion bizSimilarityQuestion);

    /**
     * 修改相似问
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 结果
     */
    int updateBizSimilarityQuestion(BizSimilarityQuestion bizSimilarityQuestion);

    /**
     * 批量删除相似问
     * 
     * @param ids 需要删除的相似问主键集合
     * @return 结果
     */
    int deleteBizSimilarityQuestionByIds(Long[] ids);

    /**
     * 删除相似问信息
     * 
     * @param id 相似问主键
     * @return 结果
     */
    int deleteBizSimilarityQuestionById(Long id);
}
