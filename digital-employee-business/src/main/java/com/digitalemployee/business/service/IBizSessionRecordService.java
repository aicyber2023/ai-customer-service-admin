package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizSessionRecord;

import java.util.List;

/**
 * 对话详单Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizSessionRecordService extends IService<BizSessionRecord> {
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

    List<BizSessionRecord> selectBizSessionRecordListNew(BizSessionRecord bizSessionRecord);

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
     * 批量删除对话详单
     *
     * @param ids 需要删除的对话详单主键集合
     * @return 结果
     */
    int deleteBizSessionRecordByIds(Long[] ids);

    /**
     * 删除对话详单信息
     *
     * @param id 对话详单主键
     * @return 结果
     */
    int deleteBizSessionRecordById(Long id);
}
