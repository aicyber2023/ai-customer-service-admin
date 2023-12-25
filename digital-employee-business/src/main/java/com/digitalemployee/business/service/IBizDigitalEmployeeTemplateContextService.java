package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplateContext;

import java.util.List;

/**
 * 数字员工模板上下文Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizDigitalEmployeeTemplateContextService extends IService<BizDigitalEmployeeTemplateContext> {
    /**
     * 查询数字员工模板上下文
     *
     * @param id 数字员工模板上下文主键
     * @return 数字员工模板上下文
     */
    BizDigitalEmployeeTemplateContext selectBizDigitalEmployeeTemplateContextById(Long id);

    /**
     * 查询数字员工模板上下文列表
     *
     * @param bizDigitalEmployeeTemplateContext 数字员工模板上下文
     * @return 数字员工模板上下文集合
     */
    List<BizDigitalEmployeeTemplateContext> selectBizDigitalEmployeeTemplateContextList(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext);

    /**
     * 新增数字员工模板上下文
     *
     * @param bizDigitalEmployeeTemplateContext 数字员工模板上下文
     * @return 结果
     */
    int insertBizDigitalEmployeeTemplateContext(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext);

    /**
     * 修改数字员工模板上下文
     *
     * @param bizDigitalEmployeeTemplateContext 数字员工模板上下文
     * @return 结果
     */
    int updateBizDigitalEmployeeTemplateContext(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext);

    /**
     * 批量删除数字员工模板上下文
     *
     * @param ids 需要删除的数字员工模板上下文主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateContextByIds(Long[] ids);

    /**
     * 删除数字员工模板上下文信息
     *
     * @param id 数字员工模板上下文主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateContextById(Long id);
}
