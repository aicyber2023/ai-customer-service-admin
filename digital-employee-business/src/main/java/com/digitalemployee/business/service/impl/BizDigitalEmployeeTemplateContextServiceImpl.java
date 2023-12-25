package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplateContext;
import com.digitalemployee.business.mapper.BizDigitalEmployeeTemplateContextMapper;
import com.digitalemployee.business.service.IBizDigitalEmployeeTemplateContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数字员工模板上下文Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizDigitalEmployeeTemplateContextServiceImpl extends ServiceImpl<BizDigitalEmployeeTemplateContextMapper, BizDigitalEmployeeTemplateContext> implements IBizDigitalEmployeeTemplateContextService {

    private final BizDigitalEmployeeTemplateContextMapper bizDigitalEmployeeTemplateContextMapper;

    /**
     * 查询数字员工模板上下文
     *
     * @param id 数字员工模板上下文主键
     * @return 数字员工模板上下文
     */
    @Override
    public BizDigitalEmployeeTemplateContext selectBizDigitalEmployeeTemplateContextById(Long id) {
        return bizDigitalEmployeeTemplateContextMapper.selectBizDigitalEmployeeTemplateContextById(id);
    }

    /**
     * 查询数字员工模板上下文列表
     *
     * @param bizDigitalEmployeeTemplateContext 数字员工模板上下文
     * @return 数字员工模板上下文
     */
    @Override
    public List<BizDigitalEmployeeTemplateContext> selectBizDigitalEmployeeTemplateContextList(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext) {
        return bizDigitalEmployeeTemplateContextMapper.selectBizDigitalEmployeeTemplateContextList(bizDigitalEmployeeTemplateContext);
    }

    /**
     * 新增数字员工模板上下文
     *
     * @param bizDigitalEmployeeTemplateContext 数字员工模板上下文
     * @return 结果
     */
    @Override
    public int insertBizDigitalEmployeeTemplateContext(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext) {
        return bizDigitalEmployeeTemplateContextMapper.insertBizDigitalEmployeeTemplateContext(bizDigitalEmployeeTemplateContext);
    }

    /**
     * 修改数字员工模板上下文
     *
     * @param bizDigitalEmployeeTemplateContext 数字员工模板上下文
     * @return 结果
     */
    @Override
    public int updateBizDigitalEmployeeTemplateContext(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext) {
        return bizDigitalEmployeeTemplateContextMapper.updateBizDigitalEmployeeTemplateContext(bizDigitalEmployeeTemplateContext);
    }

    /**
     * 批量删除数字员工模板上下文
     *
     * @param ids 需要删除的数字员工模板上下文主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeTemplateContextByIds(Long[] ids) {
        return bizDigitalEmployeeTemplateContextMapper.deleteBizDigitalEmployeeTemplateContextByIds(ids);
    }

    /**
     * 删除数字员工模板上下文信息
     *
     * @param id 数字员工模板上下文主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeTemplateContextById(Long id) {
        return bizDigitalEmployeeTemplateContextMapper.deleteBizDigitalEmployeeTemplateContextById(id);
    }
}
