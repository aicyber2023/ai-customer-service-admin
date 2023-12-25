package com.digitalemployee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;

import java.util.List;

/**
 * 用户数字员工配置Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface ISysUserDeConfigService extends IService<SysUserDeConfig> {
    /**
     * 查询用户数字员工配置
     *
     * @param id 用户数字员工配置主键
     * @return 用户数字员工配置
     */
    SysUserDeConfig selectSysUserDeConfigById(Long id);

    /**
     * 查询用户数字员工配置列表
     *
     * @param sysUserDeConfig 用户数字员工配置
     * @return 用户数字员工配置集合
     */
    List<SysUserDeConfig> selectSysUserDeConfigList(SysUserDeConfig sysUserDeConfig);

    /**
     * 新增用户数字员工配置
     *
     * @param sysUserDeConfig 用户数字员工配置
     * @return 结果
     */
    int insertSysUserDeConfig(SysUserDeConfig sysUserDeConfig);

    /**
     * 修改用户数字员工配置
     *
     * @param sysUserDeConfig 用户数字员工配置
     * @return 结果
     */
    int updateSysUserDeConfig(SysUserDeConfig sysUserDeConfig);

    /**
     * 批量删除用户数字员工配置
     *
     * @param ids 需要删除的用户数字员工配置主键集合
     * @return 结果
     */
    int deleteSysUserDeConfigByIds(Long[] ids);

    /**
     * 删除用户数字员工配置信息
     *
     * @param id 用户数字员工配置主键
     * @return 结果
     */
    int deleteSysUserDeConfigById(Long id);
}
