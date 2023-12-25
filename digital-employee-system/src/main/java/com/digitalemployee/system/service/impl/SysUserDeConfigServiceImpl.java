package com.digitalemployee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;
import com.digitalemployee.system.mapper.SysUserDeConfigMapper;
import com.digitalemployee.system.service.ISysUserDeConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户数字员工配置Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class SysUserDeConfigServiceImpl extends ServiceImpl<SysUserDeConfigMapper, SysUserDeConfig> implements ISysUserDeConfigService {

    private final SysUserDeConfigMapper sysUserDeConfigMapper;

    /**
     * 查询用户数字员工配置
     *
     * @param id 用户数字员工配置主键
     * @return 用户数字员工配置
     */
    @Override
    public SysUserDeConfig selectSysUserDeConfigById(Long id) {
        return sysUserDeConfigMapper.selectSysUserDeConfigById(id);
    }

    /**
     * 查询用户数字员工配置列表
     *
     * @param sysUserDeConfig 用户数字员工配置
     * @return 用户数字员工配置
     */
    @Override
    public List<SysUserDeConfig> selectSysUserDeConfigList(SysUserDeConfig sysUserDeConfig) {
        return sysUserDeConfigMapper.selectSysUserDeConfigList(sysUserDeConfig);
    }

    /**
     * 新增用户数字员工配置
     *
     * @param sysUserDeConfig 用户数字员工配置
     * @return 结果
     */
    @Override
    public int insertSysUserDeConfig(SysUserDeConfig sysUserDeConfig) {
        return sysUserDeConfigMapper.insertSysUserDeConfig(sysUserDeConfig);
    }

    /**
     * 修改用户数字员工配置
     *
     * @param sysUserDeConfig 用户数字员工配置
     * @return 结果
     */
    @Override
    public int updateSysUserDeConfig(SysUserDeConfig sysUserDeConfig) {
        return sysUserDeConfigMapper.updateSysUserDeConfig(sysUserDeConfig);
    }

    /**
     * 批量删除用户数字员工配置
     *
     * @param ids 需要删除的用户数字员工配置主键
     * @return 结果
     */
    @Override
    public int deleteSysUserDeConfigByIds(Long[] ids) {
        return sysUserDeConfigMapper.deleteSysUserDeConfigByIds(ids);
    }

    /**
     * 删除用户数字员工配置信息
     *
     * @param id 用户数字员工配置主键
     * @return 结果
     */
    @Override
    public int deleteSysUserDeConfigById(Long id) {
        return sysUserDeConfigMapper.deleteSysUserDeConfigById(id);
    }
}
