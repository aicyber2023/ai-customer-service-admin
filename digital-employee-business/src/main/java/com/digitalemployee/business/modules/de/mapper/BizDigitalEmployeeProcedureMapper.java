package com.digitalemployee.business.modules.de.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 数字员工话术Mapper接口
 *
 * @author aicyber
 * @date 2023-12-26
 */
@Mapper
public interface BizDigitalEmployeeProcedureMapper extends BaseMapper<BizDigitalEmployeeProcedure> {
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
     * 删除数字员工话术
     *
     * @param id 数字员工话术主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeProcedureById(Long id);

    /**
     * 批量删除数字员工话术
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeProcedureByIds(Long[] ids);
}
