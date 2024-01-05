package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizQuestionAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 问答库Mapper接口
 * 
 * @author aicyber
 * @date 2023-11-27
 */
@Mapper
public interface BizQuestionAnswerMapper  extends BaseMapper<BizQuestionAnswer>
{
    /**
     * 查询问答库
     * 
     * @param id 问答库主键
     * @return 问答库
     */
    BizQuestionAnswer selectBizQuestionAnswerById(Long id);

    /**
     * 查询问答库列表
     * 
     * @param bizQuestionAnswer 问答库
     * @return 问答库集合
     */
    List<BizQuestionAnswer> selectBizQuestionAnswerList(@Param("bizQuestionAnswer") BizQuestionAnswer bizQuestionAnswer,
                                                        @Param("startTime") Date startTime,
                                                        @Param("endTime") Date endTime);
    List<BizQuestionAnswer> selectBizQuestionAnswerListBydeId(Long digitalEmployeeId);

    /**
     * 根据条件查询单个问答库
     * @param bizQuestionAnswer
     * @return
     */
    BizQuestionAnswer selectOneBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer);

    /**
     * 根据条件查询单个问答库，除去本身
     * @param bizQuestionAnswer
     * @return
     */
    BizQuestionAnswer selectBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer);

    /**
     * 根据问题和回答两个条件查询，用来查询数据库是否存在该数据
     * @param question
     * @param answer
     * @return
     */
    BizQuestionAnswer queryBizQuestionAnswerList(@Param("digitalEmployeeId") Long digitalEmployeeId,@Param("question") String question, @Param("answer") String answer);

    /**
     * 新增问答库
     * 
     * @param bizQuestionAnswer 问答库
     * @return 结果
     */
    int insertBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer);

    /**
     * 修改问答库
     * 
     * @param bizQuestionAnswer 问答库
     * @return 结果
     */
    int updateBizQuestionAnswer(BizQuestionAnswer bizQuestionAnswer);

    /**
     * 删除问答库
     * 
     * @param id 问答库主键
     * @return 结果
     */
    int deleteBizQuestionAnswerById(Long id);

    /**
     * 批量删除问答库
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizQuestionAnswerByIds(Long[] ids);


    void insertBatchQuestionAnswer(List<BizQuestionAnswer> list);


    List<BizQuestionAnswer> querySimilarQuestionList(@Param("bizQuestionAnswer") BizQuestionAnswer bizQuestionAnswer,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);

    List<BizQuestionAnswer> getQuestionAnswerByCollectionId(@Param("collectionId")String collectionId,@Param("digitalEmployeeId") Long digitalEmployeeId);

}
