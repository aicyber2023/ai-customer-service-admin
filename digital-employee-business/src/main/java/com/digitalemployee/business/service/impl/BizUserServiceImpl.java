package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.mapper.BizUserMapper;
import com.digitalemployee.business.service.IBizUserService;
import com.digitalemployee.common.annotation.DataScope;
import com.digitalemployee.common.core.domain.entity.SysUser;
import com.digitalemployee.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户账户Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizUserServiceImpl extends ServiceImpl<BizUserMapper, BizUser> implements IBizUserService {

    private final BizUserMapper bizUserMapper;

    /**
     * 查询用户账户
     *
     * @param id 用户账户主键
     * @return 用户账户
     */
    @Override
    public BizUser selectBizUserById(Long id) {
        return bizUserMapper.selectBizUserById(id);
    }

    /**
     * 查询用户账户列表
     *
     * @param bizUser 用户账户
     * @return 用户账户
     */
    @Override
    @DataScope(deptAlias = "u", userAlias = "u")
    public List<BizUser> selectBizUserList(BizUser bizUser) {
        // return bizUserMapper.selectBizUserList(bizUser);
        return bizUserMapper.selectBizUserListNew(bizUser);
    }

    /**
     * 新增用户账户
     *
     * @param bizUser 用户账户
     * @return 结果
     */
    @Override
    public BizUser insertBizUser(BizUser bizUser) {
        bizUserMapper.insert(bizUser);
        return bizUser;
    }

    /**
     * 修改用户账户
     *
     * @param bizUser 用户账户
     * @return 结果
     */
    @Override
    public int updateBizUser(BizUser bizUser) {
        return bizUserMapper.updateById(bizUser);
    }

    /**
     * 批量删除用户账户
     *
     * @param ids 需要删除的用户账户主键
     * @return 结果
     */
    @Override
    public int deleteBizUserByIds(Long[] ids) {
        return bizUserMapper.deleteBizUserByIds(ids);
    }

    /**
     * 删除用户账户信息
     *
     * @param id 用户账户主键
     * @return 结果
     */
    @Override
    public int deleteBizUserById(Long id) {
        return bizUserMapper.deleteBizUserById(id);
    }

    @Override
    public void saveAvatar(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("不能上传空的头像文件");
        }
        BizUser one = this.getById(userId);
        if (one == null) {
            throw new RuntimeException("未找到指定用户");
        }
        try {
            one.setAvatar(file.getBytes());
            one.setAvatarContentType(file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.updateById(one);
    }

    @Override
    public void createDefaultBizUser(SysUser user, Long createUserId) {
        BizUser bizUser = new BizUser();
        bizUser.setSysUserId(user.getUserId());
        bizUser.setNickName(user.getNickName());
        bizUser.setName(user.getUserName());
        bizUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        bizUser.setCreateBy(String.valueOf(createUserId));
        bizUser.setCreateTime(new Date());
        bizUser.setStatus(0);
        bizUser.setUserId(createUserId);
        bizUser.setDeptId(user.getDeptId());
        this.save(bizUser);
    }

    /**
     * 验证用户的服务对象用户名是否唯一
     * @param bizUser BizUser
     * @return true 唯一 false 不唯一
     */
    @Override
    public boolean checkBizUserUnique(BizUser bizUser) {
        // 查询
        LambdaQueryWrapper<BizUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizUser::getSysUserId, bizUser.getSysUserId()).eq(BizUser::getName, bizUser.getName()).eq(BizUser::getStatus, 1).last("limit 1");
        BizUser one = this.getOne(wrapper);

        // 不存在相同的服务对象时 - 唯一
        if (one == null) {
            return true;
        }

        // 新增（bizUser.getUserId() == null）时，但是存在相同的服务对象 - 不唯一
        if (bizUser.getUserId() == null) {
            return false;
        }

        // 修改（bizUser.getUserId() != null）时，参数的id和数据库中的id一致 - 唯一，否则不唯一
        return Objects.equals(one.getUserId(), bizUser.getUserId());
    }
}
