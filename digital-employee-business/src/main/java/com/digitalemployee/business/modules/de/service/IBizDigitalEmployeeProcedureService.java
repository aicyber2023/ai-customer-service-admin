package com.digitalemployee.business.modules.de.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;

import java.util.List;


/**
 * 数字员工话术Service接口
 *
 * @author aicyber
 * @date 2023-12-26
 */
public interface IBizDigitalEmployeeProcedureService extends IService<BizDigitalEmployeeProcedure> {
    /**
     * 查询数字员工话术
     *
     * @param id 数字员工话术主键
     * @return 数字员工话术
     */
    BizDigitalEmployeeProcedure selectBizDigitalEmployeeProcedureById(Long id);

    /**
     * 查询数字员工话术列表
     *
     * @param bizDigitalEmployeeProcedure 数字员工话术
     * @return 数字员工话术集合
     */
    List<BizDigitalEmployeeProcedure> selectBizDigitalEmployeeProcedureList(BizDigitalEmployeeProcedure bizDigitalEmployeeProcedure);

    /**
     * 新增数字员工话术
     *
     * @param bizDigitalEmployeeProcedure 数字员工话术
     * @return 结果
     */
    int insertBizDigitalEmployeeProcedure(BizDigitalEmployeeProcedure bizDigitalEmployeeProcedure);

    /**
     * 修改数字员工话术
     *
     * @param bizDigitalEmployeeProcedure 数字员工话术
     * @return 结果
     */
    int updateBizDigitalEmployeeProcedure(BizDigitalEmployeeProcedure bizDigitalEmployeeProcedure);

    /**
     * 批量删除数字员工话术
     *
     * @param ids 需要删除的数字员工话术主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeProcedureByIds(Long[] ids);

    /**
     * 删除数字员工话术信息
     *
     * @param id 数字员工话术主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeProcedureById(Long id);

    BizDigitalEmployeeProcedure selectProcedureByDeId(Long deId);
}
