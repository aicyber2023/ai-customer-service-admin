package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 知识库Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizKnowledgeBaseMapper extends BaseMapper<BizKnowledgeBase> {
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
     * 删除知识库
     *
     * @param id 知识库主键
     * @return 结果
     */
    int deleteBizKnowledgeBaseById(Long id);

    /**
     * 批量删除知识库
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizKnowledgeBaseByIds(Long[] ids);
}
