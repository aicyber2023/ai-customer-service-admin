package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数字员工模板Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizDigitalEmployeeTemplateMapper extends BaseMapper<BizDigitalEmployeeTemplate> {
    /**
     * 查询数字员工模板
     *
     * @param id 数字员工模板主键
     * @return 数字员工模板
     */
    BizDigitalEmployeeTemplate selectBizDigitalEmployeeTemplateById(Long id);

    /**
     * 查询数字员工模板列表
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 数字员工模板集合
     */
    List<BizDigitalEmployeeTemplate> selectBizDigitalEmployeeTemplateList(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate);

    /**
     * 新增数字员工模板
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 结果
     */
    int insertBizDigitalEmployeeTemplate(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate);

    /**
     * 修改数字员工模板
     *
     * @param bizDigitalEmployeeTemplate 数字员工模板
     * @return 结果
     */
    int updateBizDigitalEmployeeTemplate(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate);

    /**
     * 删除数字员工模板
     *
     * @param id 数字员工模板主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateById(Long id);

    /**
     * 批量删除数字员工模板
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeTemplateByIds(Long[] ids);

    List<BizDigitalEmployeeTemplate> selectUsedTemplateList(@Param("sysUserId") Long sysUserId);
}
