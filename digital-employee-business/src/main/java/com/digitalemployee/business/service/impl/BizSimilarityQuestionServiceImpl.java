package com.digitalemployee.business.service.impl;

import java.util.List;

import com.digitalemployee.business.domain.BizSimilarityQuestion;
import com.digitalemployee.business.mapper.BizSimilarityQuestionMapper;
import com.digitalemployee.business.service.IBizSimilarityQuestionService;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 相似问Service业务层处理
 * 
 * @author aicyber
 * @date 2023-11-27
 */
@Service
@RequiredArgsConstructor
public class BizSimilarityQuestionServiceImpl extends ServiceImpl<BizSimilarityQuestionMapper, BizSimilarityQuestion> implements IBizSimilarityQuestionService
{

    private final BizSimilarityQuestionMapper bizSimilarityQuestionMapper;

    /**
     * 查询相似问
     * 
     * @param id 相似问主键
     * @return 相似问
     */
    @Override
    public BizSimilarityQuestion selectBizSimilarityQuestionById(Long id)
    {
        return bizSimilarityQuestionMapper.selectBizSimilarityQuestionById(id);
    }

    /**
     * 查询相似问列表
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 相似问
     */
    @Override
    public List<BizSimilarityQuestion> selectBizSimilarityQuestionList(BizSimilarityQuestion bizSimilarityQuestion)
    {
        return bizSimilarityQuestionMapper.selectBizSimilarityQuestionList(bizSimilarityQuestion);
    }

    /**
     * 新增相似问
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 结果
     */
    @Override
    public int insertBizSimilarityQuestion(BizSimilarityQuestion bizSimilarityQuestion)
    {
        return bizSimilarityQuestionMapper.insertBizSimilarityQuestion(bizSimilarityQuestion);
    }

    /**
     * 修改相似问
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 结果
     */
    @Override
    public int updateBizSimilarityQuestion(BizSimilarityQuestion bizSimilarityQuestion)
    {
        return bizSimilarityQuestionMapper.updateBizSimilarityQuestion(bizSimilarityQuestion);
    }

    /**
     * 批量删除相似问
     * 
     * @param ids 需要删除的相似问主键
     * @return 结果
     */
    @Override
    public int deleteBizSimilarityQuestionByIds(Long[] ids)
    {
        return bizSimilarityQuestionMapper.deleteBizSimilarityQuestionByIds(ids);
    }

    /**
     * 删除相似问信息
     * 
     * @param id 相似问主键
     * @return 结果
     */
    @Override
    public int deleteBizSimilarityQuestionById(Long id)
    {
        return bizSimilarityQuestionMapper.deleteBizSimilarityQuestionById(id);
    }
}
