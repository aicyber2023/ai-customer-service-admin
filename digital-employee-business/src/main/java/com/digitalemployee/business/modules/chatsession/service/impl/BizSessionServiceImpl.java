package com.digitalemployee.business.modules.chatsession.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.modules.chatsession.domain.BizSession;
import com.digitalemployee.business.modules.chatsession.mapper.BizSessionMapper;
import com.digitalemployee.business.modules.chatsession.service.IBizSessionService;
import com.digitalemployee.common.annotation.DataScope;
import com.digitalemployee.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会话sessionService业务层处理
 *
 * @author aicyber
 * @date 2023-12-27
 */
@Service
@RequiredArgsConstructor
public class BizSessionServiceImpl
        extends ServiceImpl<BizSessionMapper, BizSession>
        implements IBizSessionService {

    private final BizSessionMapper bizSessionMapper;

    /**
     * 查询会话session
     *
     * @param id 会话session主键
     * @return 会话session
     */
    @Override
    public BizSession selectBizSessionById(Long id) {
        return bizSessionMapper.selectBizSessionById(id);
    }

    /**
     * 查询会话session列表
     *
     * @param bizSession 会话session
     * @return 会话session
     */
    @Override
    @DataScope(deptAlias = "s", userAlias = "s")
    public List<BizSession> selectBizSessionList(BizSession bizSession) {
        return bizSessionMapper.selectBizSessionList(bizSession);
    }

    /**
     * 新增会话session
     *
     * @param bizSession 会话session
     * @return 结果
     */
    @Override
    public int insertBizSession(BizSession bizSession) {
        bizSession.setCreateTime(DateUtils.getNowDate());
        return bizSessionMapper.insertBizSession(bizSession);
    }

    /**
     * 修改会话session
     *
     * @param bizSession 会话session
     * @return 结果
     */
    @Override
    public int updateBizSession(BizSession bizSession) {
        bizSession.setUpdateTime(DateUtils.getNowDate());
        return bizSessionMapper.updateBizSession(bizSession);
    }

    /**
     * 批量删除会话session
     *
     * @param ids 需要删除的会话session主键
     * @return 结果
     */
    @Override
    public int deleteBizSessionByIds(Long[] ids) {
        return bizSessionMapper.deleteBizSessionByIds(ids);
    }

    /**
     * 删除会话session信息
     *
     * @param id 会话session主键
     * @return 结果
     */
    @Override
    public int deleteBizSessionById(Long id) {
        return bizSessionMapper.deleteBizSessionById(id);
    }
}
