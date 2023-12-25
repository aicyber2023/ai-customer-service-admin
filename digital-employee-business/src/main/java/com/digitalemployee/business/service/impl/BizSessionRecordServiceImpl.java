package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizSessionRecord;
import com.digitalemployee.business.mapper.BizSessionRecordMapper;
import com.digitalemployee.business.service.IBizSessionRecordService;
import com.digitalemployee.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对话详单Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizSessionRecordServiceImpl extends ServiceImpl<BizSessionRecordMapper, BizSessionRecord> implements IBizSessionRecordService {

    private final BizSessionRecordMapper bizSessionRecordMapper;

    /**
     * 查询对话详单
     *
     * @param id 对话详单主键
     * @return 对话详单
     */
    @Override
    public BizSessionRecord selectBizSessionRecordById(Long id) {
        return bizSessionRecordMapper.selectBizSessionRecordById(id);
    }

    /**
     * 查询对话详单列表
     *
     * @param bizSessionRecord 对话详单
     * @return 对话详单
     */
    @Override
    public List<BizSessionRecord> selectBizSessionRecordList(BizSessionRecord bizSessionRecord) {
        return bizSessionRecordMapper.selectBizSessionRecordList(bizSessionRecord);
    }

    @Override
    public List<BizSessionRecord> selectBizSessionRecordListNew(BizSessionRecord bizSessionRecord) {
        return bizSessionRecordMapper.selectBizSessionRecordListNew(bizSessionRecord);
    }

    /**
     * 新增对话详单
     *
     * @param bizSessionRecord 对话详单
     * @return 结果
     */
    @Override
    public int insertBizSessionRecord(BizSessionRecord bizSessionRecord) {
        bizSessionRecord.setCreateTime(DateUtils.getNowDate());
        return bizSessionRecordMapper.insertBizSessionRecord(bizSessionRecord);
    }

    /**
     * 修改对话详单
     *
     * @param bizSessionRecord 对话详单
     * @return 结果
     */
    @Override
    public int updateBizSessionRecord(BizSessionRecord bizSessionRecord) {
        bizSessionRecord.setUpdateTime(DateUtils.getNowDate());
        return bizSessionRecordMapper.updateBizSessionRecord(bizSessionRecord);
    }

    /**
     * 批量删除对话详单
     *
     * @param ids 需要删除的对话详单主键
     * @return 结果
     */
    @Override
    public int deleteBizSessionRecordByIds(Long[] ids) {
        return bizSessionRecordMapper.deleteBizSessionRecordByIds(ids);
    }

    /**
     * 删除对话详单信息
     *
     * @param id 对话详单主键
     * @return 结果
     */
    @Override
    public int deleteBizSessionRecordById(Long id) {
        return bizSessionRecordMapper.deleteBizSessionRecordById(id);
    }
}
