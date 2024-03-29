package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizDigitalEmployeeContext;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户数字员工上下文Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizDigitalEmployeeContextMapper extends BaseMapper<BizDigitalEmployeeContext> {
    /**
     * 查询客户数字员工上下文
     *
     * @param id 客户数字员工上下文主键
     * @return 客户数字员工上下文
     */
    BizDigitalEmployeeContext selectBizDigitalEmployeeContextById(Long id);

    /**
     * 查询客户数字员工上下文列表
     *
     * @param bizDigitalEmployeeContext 客户数字员工上下文
     * @return 客户数字员工上下文集合
     */
    List<BizDigitalEmployeeContext> selectBizDigitalEmployeeContextList(BizDigitalEmployeeContext bizDigitalEmployeeContext);

    /**
     * 新增客户数字员工上下文
     *
     * @param bizDigitalEmployeeContext 客户数字员工上下文
     * @return 结果
     */
    int insertBizDigitalEmployeeContext(BizDigitalEmployeeContext bizDigitalEmployeeContext);

    /**
     * 修改客户数字员工上下文
     *
     * @param bizDigitalEmployeeContext 客户数字员工上下文
     * @return 结果
     */
    int updateBizDigitalEmployeeContext(BizDigitalEmployeeContext bizDigitalEmployeeContext);

    /**
     * 删除客户数字员工上下文
     *
     * @param id 客户数字员工上下文主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeContextById(Long id);

    /**
     * 批量删除客户数字员工上下文
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeContextByIds(Long[] ids);
}
