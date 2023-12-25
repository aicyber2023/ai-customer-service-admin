package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.common.core.domain.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户账户Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizUserService extends IService<BizUser> {
    /**
     * 查询用户账户
     *
     * @param id 用户账户主键
     * @return 用户账户
     */
    BizUser selectBizUserById(Long id);

    /**
     * 查询用户账户列表
     *
     * @param bizUser 用户账户
     * @return 用户账户集合
     */
    List<BizUser> selectBizUserList(BizUser bizUser);

    /**
     * 新增用户账户
     *
     * @param bizUser 用户账户
     * @return 结果
     */
    BizUser insertBizUser(BizUser bizUser);

    /**
     * 修改用户账户
     *
     * @param bizUser 用户账户
     * @return 结果
     */
    int updateBizUser(BizUser bizUser);

    /**
     * 批量删除用户账户
     *
     * @param ids 需要删除的用户账户主键集合
     * @return 结果
     */
    int deleteBizUserByIds(Long[] ids);

    /**
     * 删除用户账户信息
     *
     * @param id 用户账户主键
     * @return 结果
     */
    int deleteBizUserById(Long id);

    void saveAvatar(MultipartFile file, Long userId);

    void createDefaultBizUser(SysUser user, Long createUserId);

    boolean checkBizUserUnique(BizUser bizUser);
}
