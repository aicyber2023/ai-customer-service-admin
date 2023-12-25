package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户账户Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizUserMapper extends BaseMapper<BizUser> {
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

    List<BizUser> selectBizUserListNew(BizUser bizUser);

    /**
     * 新增用户账户
     *
     * @param bizUser 用户账户
     * @return 结果
     */
    int insertBizUser(BizUser bizUser);

    /**
     * 修改用户账户
     *
     * @param bizUser 用户账户
     * @return 结果
     */
    int updateBizUser(BizUser bizUser);

    /**
     * 删除用户账户
     *
     * @param id 用户账户主键
     * @return 结果
     */
    int deleteBizUserById(Long id);

    /**
     * 批量删除用户账户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizUserByIds(Long[] ids);
}
