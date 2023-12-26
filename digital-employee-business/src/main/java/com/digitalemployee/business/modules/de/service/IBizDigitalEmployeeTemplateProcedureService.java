package com.digitalemployee.business.modules.de.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeTemplateProcedure;

import java.util.List;

/**
 * 数字员工模板话术Service接口
 *
 * @author aicyber
 * @date 2023-12-26
 */
public interface IBizDigitalEmployeeTemplateProcedureService extends IService<BizDigitalEmployeeTemplateProcedure> {
    /**
     * 查询数字员工模板话术
     *
     * @param id 数字员工模板话术主键
     * @return 数字员工模板话术
     */
    BizDigitalEmployeeTemplateProcedure selectBizDigitalEmployeeTemplateProcedureById(Long id);

    /**
     * 查询数字员工模板话术列表
     *
     * @param bizDigitalEmployeeTemplateProcedure 数字员工模板话术
     * @return 数字员工模板话术集合
     */
    List<BizDigitalEmployeeTemplateProcedure> selectBizDigitalEmployeeTemplateProcedureList(BizDigitalEmployeeTemplateProcedure bizDigitalEmployeeTemplateProcedure);

    /**
     * 新增数字员工模板话术
     *
     * @param bizDigitalEmployeeTemplateProcedure 数字员工模板话术
     * @return 结果
     */
    int insertBizDigitalEmployeeTemplateProcedure(BizDigitalEmployeeTemplateProcedure bizDigitalEmployeeTemplateProcedure);

    /**
     * 修改数字员工模板话术
     *
     * @param bizDigitalEmployeeTemplateProcedure 数字员工模板话术
     * @return 结果
     */
    int updateBizDigitalEmployeeTemplateProcedure(BizDigitalEmployeeTemplateProcedure bizDigitalEmployeeTemplateProcedure);

    /**
     * 批量删除数字员工模板话术
     *
     * @param ids 需要删除的数字员工模板话术主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateProcedureByIds(Long[] ids);

    /**
     * 删除数字员工模板话术信息
     *
     * @param id 数字员工模板话术主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateProcedureById(Long id);
}
