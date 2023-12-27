package com.digitalemployee.business.modules.chatsession.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 对话详单Mapper接口
 *
 * @author aicyber
 * @date 2023-12-27
 */
@Mapper
public interface BizSessionRecordMapper extends BaseMapper<BizSessionRecord> {
    /**
     * 查询对话详单
     *
     * @param id 对话详单主键
     * @return 对话详单
     */
    BizSessionRecord selectBizSessionRecordById(Long id);

    /**
     * 查询对话详单列表
     *
     * @param bizSessionRecord 对话详单
     * @return 对话详单集合
     */
    List<BizSessionRecord> selectBizSessionRecordList(BizSessionRecord bizSessionRecord);

    /**
     * 新增对话详单
     *
     * @param bizSessionRecord 对话详单
     * @return 结果
     */
    int insertBizSessionRecord(BizSessionRecord bizSessionRecord);

    /**
     * 修改对话详单
     *
     * @param bizSessionRecord 对话详单
     * @return 结果
     */
    int updateBizSessionRecord(BizSessionRecord bizSessionRecord);

    /**
     * 删除对话详单
     *
     * @param id 对话详单主键
     * @return 结果
     */
    int deleteBizSessionRecordById(Long id);

    /**
     * 批量删除对话详单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizSessionRecordByIds(Long[] ids);
}
