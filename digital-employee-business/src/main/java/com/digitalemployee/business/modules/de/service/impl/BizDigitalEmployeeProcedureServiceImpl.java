package com.digitalemployee.business.modules.de.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;
import com.digitalemployee.business.modules.de.mapper.BizDigitalEmployeeProcedureMapper;
import com.digitalemployee.business.modules.de.service.IBizDigitalEmployeeProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数字员工话术Service业务层处理
 *
 * @author aicyber
 * @date 2023-12-26
 */
@Service
@RequiredArgsConstructor
public class BizDigitalEmployeeProcedureServiceImpl
        extends ServiceImpl<BizDigitalEmployeeProcedureMapper, BizDigitalEmployeeProcedure>
        implements IBizDigitalEmployeeProcedureService {

    private final BizDigitalEmployeeProcedureMapper bizDigitalEmployeeProcedureMapper;

    /**
     * 查询数字员工话术
     *
     * @param id 数字员工话术主键
     * @return 数字员工话术
     */
    @Override
    public BizDigitalEmployeeProcedure selectBizDigitalEmployeeProcedureById(Long id) {
        return bizDigitalEmployeeProcedureMapper.selectBizDigitalEmployeeProcedureById(id);
    }

    /**
     * 查询数字员工话术列表
     *
     * @param bizDigitalEmployeeProcedure 数字员工话术
     * @return 数字员工话术
     */
    @Override
    public List<BizDigitalEmployeeProcedure> selectBizDigitalEmployeeProcedureList(BizDigitalEmployeeProcedure bizDigitalEmployeeProcedure) {
        return bizDigitalEmployeeProcedureMapper.selectBizDigitalEmployeeProcedureList(bizDigitalEmployeeProcedure);
    }

    /**
     * 新增数字员工话术
     *
     * @param bizDigitalEmployeeProcedure 数字员工话术
     * @return 结果
     */
    @Override
    public int insertBizDigitalEmployeeProcedure(BizDigitalEmployeeProcedure bizDigitalEmployeeProcedure) {
        return bizDigitalEmployeeProcedureMapper.insertBizDigitalEmployeeProcedure(bizDigitalEmployeeProcedure);
    }

    /**
     * 修改数字员工话术
     *
     * @param bizDigitalEmployeeProcedure 数字员工话术
     * @return 结果
     */
    @Override
    public int updateBizDigitalEmployeeProcedure(BizDigitalEmployeeProcedure bizDigitalEmployeeProcedure) {
        return bizDigitalEmployeeProcedureMapper.updateBizDigitalEmployeeProcedure(bizDigitalEmployeeProcedure);
    }

    /**
     * 批量删除数字员工话术
     *
     * @param ids 需要删除的数字员工话术主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeProcedureByIds(Long[] ids) {
        return bizDigitalEmployeeProcedureMapper.deleteBizDigitalEmployeeProcedureByIds(ids);
    }

    /**
     * 删除数字员工话术信息
     *
     * @param id 数字员工话术主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeProcedureById(Long id) {
        return bizDigitalEmployeeProcedureMapper.deleteBizDigitalEmployeeProcedureById(id);
    }
}
