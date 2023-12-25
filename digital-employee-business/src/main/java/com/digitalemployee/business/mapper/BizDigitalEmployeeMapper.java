package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户数字员工Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizDigitalEmployeeMapper extends BaseMapper<BizDigitalEmployee> {
    /**
     * 查询客户数字员工
     *
     * @param id 客户数字员工主键
     * @return 客户数字员工
     */
    BizDigitalEmployee selectBizDigitalEmployeeById(Long id);

    /**
     * 查询客户数字员工列表
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 客户数字员工集合
     */
    List<BizDigitalEmployee> selectBizDigitalEmployeeList(BizDigitalEmployee bizDigitalEmployee);

    /**
     * 新增客户数字员工
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 结果
     */
    int insertBizDigitalEmployee(BizDigitalEmployee bizDigitalEmployee);

    /**
     * 修改客户数字员工
     *
     * @param bizDigitalEmployee 客户数字员工
     * @return 结果
     */
    int updateBizDigitalEmployee(BizDigitalEmployee bizDigitalEmployee);

    /**
     * 删除客户数字员工
     *
     * @param id 客户数字员工主键
     * @return 结果
     */
    int deleteBizDigitalEmployeeById(Long id);

    /**
     * 批量删除客户数字员工
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizDigitalEmployeeByIds(Long[] ids);
}
