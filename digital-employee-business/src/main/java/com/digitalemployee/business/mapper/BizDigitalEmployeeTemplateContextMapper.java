package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplateContext;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数字员工模板上下文Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizDigitalEmployeeTemplateContextMapper extends BaseMapper<BizDigitalEmployeeTemplateContext> {
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
     * 删除数字员工模板上下文
     *
     * @param id 数字员工模板上下文主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateContextById(Long id);

    /**
     * 批量删除数字员工模板上下文
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateContextByIds(Long[] ids);
}
