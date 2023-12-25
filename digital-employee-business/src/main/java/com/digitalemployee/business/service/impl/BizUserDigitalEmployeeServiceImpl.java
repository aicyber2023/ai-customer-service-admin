package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizUserDigitalEmployee;
import com.digitalemployee.business.mapper.BizUserDigitalEmployeeMapper;
import com.digitalemployee.business.service.IBizUserDigitalEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户数字员工Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizUserDigitalEmployeeServiceImpl extends ServiceImpl<BizUserDigitalEmployeeMapper, BizUserDigitalEmployee> implements IBizUserDigitalEmployeeService {

    private final BizUserDigitalEmployeeMapper bizUserDigitalEmployeeMapper;

    /**
     * 查询用户数字员工
     *
     * @param id 用户数字员工主键
     * @return 用户数字员工
     */
    @Override
    public BizUserDigitalEmployee selectBizUserDigitalEmployeeById(Long id) {
        return bizUserDigitalEmployeeMapper.selectBizUserDigitalEmployeeById(id);
    }

    /**
     * 查询用户数字员工列表
     *
     * @param bizUserDigitalEmployee 用户数字员工
     * @return 用户数字员工
     */
    @Override
    public List<BizUserDigitalEmployee> selectBizUserDigitalEmployeeList(BizUserDigitalEmployee bizUserDigitalEmployee) {
        return bizUserDigitalEmployeeMapper.selectBizUserDigitalEmployeeList(bizUserDigitalEmployee);
    }

    /**
     * 新增用户数字员工
     *
     * @param bizUserDigitalEmployee 用户数字员工
     * @return 结果
     */
    @Override
    public int insertBizUserDigitalEmployee(BizUserDigitalEmployee bizUserDigitalEmployee) {
        return bizUserDigitalEmployeeMapper.insertBizUserDigitalEmployee(bizUserDigitalEmployee);
    }

    /**
     * 修改用户数字员工
     *
     * @param bizUserDigitalEmployee 用户数字员工
     * @return 结果
     */
    @Override
    public int updateBizUserDigitalEmployee(BizUserDigitalEmployee bizUserDigitalEmployee) {
        return bizUserDigitalEmployeeMapper.updateBizUserDigitalEmployee(bizUserDigitalEmployee);
    }

    /**
     * 批量删除用户数字员工
     *
     * @param ids 需要删除的用户数字员工主键
     * @return 结果
     */
    @Override
    public int deleteBizUserDigitalEmployeeByIds(Long[] ids) {
        return bizUserDigitalEmployeeMapper.deleteBizUserDigitalEmployeeByIds(ids);
    }

    /**
     * 删除用户数字员工信息
     *
     * @param id 用户数字员工主键
     * @return 结果
     */
    @Override
    public int deleteBizUserDigitalEmployeeById(Long id) {
        return bizUserDigitalEmployeeMapper.deleteBizUserDigitalEmployeeById(id);
    }
}
