package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizQuestionAnswer;
import com.digitalemployee.business.domain.BizSimilarityQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 相似问Mapper接口
 * 
 * @author aicyber
 * @date 2023-11-27
 */
@Mapper
public interface BizSimilarityQuestionMapper  extends BaseMapper<BizSimilarityQuestion>
{
    /**
     * 查询相似问
     * 
     * @param id 相似问主键
     * @return 相似问
     */
    BizSimilarityQuestion selectBizSimilarityQuestionById(Long id);

    /**
     * 根据问答id和相似问查询单条相似问数据
     * @param questionAnswerId
     * @param similarityQuestion
     * @return
     */
    BizSimilarityQuestion selectBizSimilarityQuestion(@Param("questionAnswerId") Long questionAnswerId, @Param("similarityQuestion") String similarityQuestion);

    /**
     * 查询相似问列表
     * 
     * @param bizSimilarityQuestion 相似问
     * @return 相似问集合
     */
    List<BizSimilarityQuestion> selectBizSimilarityQuestionList(BizSimilarityQuestion bizSimilarityQuestion);

    /**
     * 根据问答id查询相似问集合
     * @param questionAnswerId
     * @return
     */
    List<BizSimilarityQuestion> selectBizSimilarityQuestionListById(Long questionAnswerId);

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
     * 删除相似问
     * 
     * @param id 相似问主键
     * @return 结果
     */
    int deleteBizSimilarityQuestionById(Long id);

    /**
     * 批量删除相似问
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizSimilarityQuestionByIds(Long[] ids);

    /**
     * 批量插入形似问
     * @param list
     * @return
     */

    int batchSimilarityQuestion(List<BizSimilarityQuestion> list);
}
