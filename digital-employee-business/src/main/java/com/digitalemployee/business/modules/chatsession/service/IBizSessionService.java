package com.digitalemployee.business.modules.chatsession.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.modules.chatsession.domain.BizSession;

import java.util.List;

/**
 * 会话sessionService接口
 *
 * @author aicyber
 * @date 2023-12-27
 */
public interface IBizSessionService extends IService<BizSession> {
    /**
     * 查询会话session
     *
     * @param id 会话session主键
     * @return 会话session
     */
    BizSession selectBizSessionById(Long id);

    /**
     * 查询会话session列表
     *
     * @param bizSession 会话session
     * @return 会话session集合
     */
    List<BizSession> selectBizSessionList(BizSession bizSession);

    /**
     * 新增会话session
     *
     * @param bizSession 会话session
     * @return 结果
     */
    int insertBizSession(BizSession bizSession);

    /**
     * 修改会话session
     *
     * @param bizSession 会话session
     * @return 结果
     */
    int updateBizSession(BizSession bizSession);

    /**
     * 批量删除会话session
     *
     * @param ids 需要删除的会话session主键集合
     * @return 结果
     */
    int deleteBizSessionByIds(Long[] ids);

    /**
     * 删除会话session信息
     *
     * @param id 会话session主键
     * @return 结果
     */
    int deleteBizSessionById(Long id);
}
