package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizUserDigitalEmployee;

import java.util.List;

/**
 * 用户数字员工Service接口
 * 
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizUserDigitalEmployeeService extends IService<BizUserDigitalEmployee>
{
    /**
     * 查询用户数字员工
     * 
     * @param id 用户数字员工主键
     * @return 用户数字员工
     */
    BizUserDigitalEmployee selectBizUserDigitalEmployeeById(Long id);

    /**
     * 查询用户数字员工列表
     * 
     * @param bizUserDigitalEmployee 用户数字员工
     * @return 用户数字员工集合
     */
    List<BizUserDigitalEmployee> selectBizUserDigitalEmployeeList(BizUserDigitalEmployee bizUserDigitalEmployee);

    /**
     * 新增用户数字员工
     * 
     * @param bizUserDigitalEmployee 用户数字员工
     * @return 结果
     */
    int insertBizUserDigitalEmployee(BizUserDigitalEmployee bizUserDigitalEmployee);

    /**
     * 修改用户数字员工
     * 
     * @param bizUserDigitalEmployee 用户数字员工
     * @return 结果
     */
    int updateBizUserDigitalEmployee(BizUserDigitalEmployee bizUserDigitalEmployee);

    /**
     * 批量删除用户数字员工
     * 
     * @param ids 需要删除的用户数字员工主键集合
     * @return 结果
     */
    int deleteBizUserDigitalEmployeeByIds(Long[] ids);

    /**
     * 删除用户数字员工信息
     * 
     * @param id 用户数字员工主键
     * @return 结果
     */
    int deleteBizUserDigitalEmployeeById(Long id);
}
