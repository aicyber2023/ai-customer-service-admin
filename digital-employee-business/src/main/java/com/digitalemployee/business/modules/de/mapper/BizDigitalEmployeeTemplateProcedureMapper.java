package com.digitalemployee.business.modules.de.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeTemplateProcedure;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数字员工模板话术Mapper接口
 *
 * @author aicyber
 * @date 2023-12-26
 */
@Mapper
public interface BizDigitalEmployeeTemplateProcedureMapper extends BaseMapper<BizDigitalEmployeeTemplateProcedure> {
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
     * 删除数字员工模板话术
     *
     * @param id 数字员工模板话术主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateProcedureById(Long id);

    /**
     * 批量删除数字员工模板话术
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateProcedureByIds(Long[] ids);
}
