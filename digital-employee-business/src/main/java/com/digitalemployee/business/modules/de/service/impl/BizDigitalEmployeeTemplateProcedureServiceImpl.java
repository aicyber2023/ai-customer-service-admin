package com.digitalemployee.business.modules.de.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeTemplateProcedure;
import com.digitalemployee.business.modules.de.mapper.BizDigitalEmployeeTemplateProcedureMapper;
import com.digitalemployee.business.modules.de.service.IBizDigitalEmployeeTemplateProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数字员工模板话术Service业务层处理
 *
 * @author aicyber
 * @date 2023-12-26
 */
@Service
@RequiredArgsConstructor
public class BizDigitalEmployeeTemplateProcedureServiceImpl
        extends ServiceImpl<BizDigitalEmployeeTemplateProcedureMapper, BizDigitalEmployeeTemplateProcedure>
        implements IBizDigitalEmployeeTemplateProcedureService {

    private final BizDigitalEmployeeTemplateProcedureMapper bizDigitalEmployeeTemplateProcedureMapper;

    /**
     * 查询数字员工模板话术
     *
     * @param id 数字员工模板话术主键
     * @return 数字员工模板话术
     */
    @Override
    public BizDigitalEmployeeTemplateProcedure selectBizDigitalEmployeeTemplateProcedureById(Long id) {
        return bizDigitalEmployeeTemplateProcedureMapper.selectBizDigitalEmployeeTemplateProcedureById(id);
    }

    /**
     * 查询数字员工模板话术列表
     *
     * @param bizDigitalEmployeeTemplateProcedure 数字员工模板话术
     * @return 数字员工模板话术
     */
    @Override
    public List<BizDigitalEmployeeTemplateProcedure> selectBizDigitalEmployeeTemplateProcedureList(BizDigitalEmployeeTemplateProcedure bizDigitalEmployeeTemplateProcedure) {
        return bizDigitalEmployeeTemplateProcedureMapper.selectBizDigitalEmployeeTemplateProcedureList(bizDigitalEmployeeTemplateProcedure);
    }

    /**
     * 新增数字员工模板话术
     *
     * @param bizDigitalEmployeeTemplateProcedure 数字员工模板话术
     * @return 结果
     */
    @Override
    public int insertBizDigitalEmployeeTemplateProcedure(BizDigitalEmployeeTemplateProcedure bizDigitalEmployeeTemplateProcedure) {
        return bizDigitalEmployeeTemplateProcedureMapper.insertBizDigitalEmployeeTemplateProcedure(bizDigitalEmployeeTemplateProcedure);
    }

    /**
     * 修改数字员工模板话术
     *
     * @param bizDigitalEmployeeTemplateProcedure 数字员工模板话术
     * @return 结果
     */
    @Override
    public int updateBizDigitalEmployeeTemplateProcedure(BizDigitalEmployeeTemplateProcedure bizDigitalEmployeeTemplateProcedure) {
        return bizDigitalEmployeeTemplateProcedureMapper.updateBizDigitalEmployeeTemplateProcedure(bizDigitalEmployeeTemplateProcedure);
    }

    /**
     * 批量删除数字员工模板话术
     *
     * @param ids 需要删除的数字员工模板话术主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeTemplateProcedureByIds(Long[] ids) {
        return bizDigitalEmployeeTemplateProcedureMapper.deleteBizDigitalEmployeeTemplateProcedureByIds(ids);
    }

    /**
     * 删除数字员工模板话术信息
     *
     * @param id 数字员工模板话术主键
     * @return 结果
     */
    @Override
    public int deleteBizDigitalEmployeeTemplateProcedureById(Long id) {
        return bizDigitalEmployeeTemplateProcedureMapper.deleteBizDigitalEmployeeTemplateProcedureById(id);
    }
}
