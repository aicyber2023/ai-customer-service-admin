package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizDigitalEmployeeContext;
import com.digitalemployee.business.mapper.BizDigitalEmployeeContextMapper;
import com.digitalemployee.business.service.IBizDigitalEmployeeContextService;
import com.digitalemployee.common.annotation.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户数字员工上下文Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizDigitalEmployeeContextServiceImpl extends ServiceImpl<BizDigitalEmployeeContextMapper, BizDigitalEmployeeContext> implements IBizDigitalEmployeeContextService {

    private final BizDigitalEmployeeContextMapper bizDigitalEmployeeContextMapper;

    /**
     * 查询客户数字员工上下文
     *
     * @param id 客户数字员工上下文主键
     * @return 客户数字员工上下文
     */
    @Override
    public BizDigitalEmployeeContext selectBizDigitalEmployeeContextById(Long id) {
        return bizDigitalEmployeeContextMapper.selectBizDigitalEmployeeContextById(id);
    }

    /**
     * 查询客户数字员工上下文列表
     *
     * @param bizDigitalEmployeeContext 客户数字员工上下文
     * @return 客户数字员工上下文
     */
    @Override
    public List<BizDigitalEmployeeContext> selectBizDigitalEmployeeContextList(BizDigitalEmployeeContext bizDigitalEmployeeContext) {
        return bizDigitalEmployeeContextMapper.selectBizDigitalEmployeeContextList(bizDigitalEmployeeContext);
    }

    /**
     * 新增客户数字员工上下文
     *
     * @param bizDigitalEmployeeContext 客户数字员工上下文
     * @return 结果
     */
    @Override
    public int insertBizDigitalEmployeeContext(BizDigitalEmployeeContext bizDigitalEmployeeContext) {
        return bizDigitalEmployeeContextMapper.insertBizDigitalEmployeeContext(bizDigitalEmployeeContext);
    }

    /**
     * 修改客户数字员工上下文
     *
     * @param bizDigitalEmployeeContext 客户数字员工上下文
     * @return 结果
     */
    @Override
    public int updateBizDigitalEmployeeContext(BizDigitalEmployeeContext bizDigitalEmployeeContext) {
        return bizDigitalEmployeeContextMapper.updateBizDigitalEmployeeContext(bizDigitalEmployeeContext);
    }

    /**
     * 批量删除客户数字员工上下文
     *
     * @param ids 需要删除的客户数字员工上下文主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeContextByIds(Long[] ids) {
        return bizDigitalEmployeeContextMapper.deleteBizDigitalEmployeeContextByIds(ids);
    }

    /**
     * 删除客户数字员工上下文信息
     *
     * @param id 客户数字员工上下文主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeContextById(Long id) {
        return bizDigitalEmployeeContextMapper.deleteBizDigitalEmployeeContextById(id);
    }
}
