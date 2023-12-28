package com.digitalemployee.business.modules.chatsession.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.modules.chatsession.domain.BizSession;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会话sessionMapper接口
 *
 * @author aicyber
 * @date 2023-12-27
 */
@Mapper
public interface BizSessionMapper extends BaseMapper<BizSession> {
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
    List<BizSession> selectBizSessionListNew(BizSession bizSession);

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
     * 删除会话session
     *
     * @param id 会话session主键
     * @return 结果
     */
    int deleteBizSessionById(Long id);

    /**
     * 批量删除会话session
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizSessionByIds(Long[] ids);
}
