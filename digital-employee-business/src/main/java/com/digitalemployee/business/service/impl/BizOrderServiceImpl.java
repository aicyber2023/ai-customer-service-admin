package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizOrder;
import com.digitalemployee.business.mapper.BizOrderMapper;
import com.digitalemployee.business.service.IBizOrderService;
import com.digitalemployee.common.core.domain.entity.SysUserDeConfig;
import com.digitalemployee.common.utils.DateUtils;
import com.digitalemployee.system.mapper.SysUserDeConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 订单Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizOrderServiceImpl extends ServiceImpl<BizOrderMapper, BizOrder> implements IBizOrderService {

    private final BizOrderMapper bizOrderMapper;

    private final SysUserDeConfigMapper sysUserDeConfigMapper;

    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public BizOrder selectBizOrderById(Long id) {
        return bizOrderMapper.selectBizOrderById(id);
    }

    /**
     * 查询订单列表
     *
     * @param bizOrder 订单
     * @return 订单
     */
    @Override
    public List<BizOrder> selectBizOrderList(BizOrder bizOrder) {
        return bizOrderMapper.selectBizOrderList(bizOrder);
    }

    /**
     * 新增订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    @Override
    public int insertBizOrder(BizOrder bizOrder) {
        bizOrder.setCreateTime(DateUtils.getNowDate());
        return bizOrderMapper.insertBizOrder(bizOrder);
    }

    /**
     * 修改订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    @Override
    public int updateBizOrder(BizOrder bizOrder) {
        bizOrder.setUpdateTime(DateUtils.getNowDate());
        return bizOrderMapper.updateBizOrder(bizOrder);
    }

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteBizOrderByIds(Long[] ids) {
        return bizOrderMapper.deleteBizOrderByIds(ids);
    }

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    @Override
    public int deleteBizOrderById(Long id) {
        return bizOrderMapper.deleteBizOrderById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeOrder(Long id) {
        BizOrder order = this.getById(id);
        // 修改订单状态为已生效
        order.setStatus(2);
        this.updateById(order);
        // 更新用户数字员工配置
        this.updateUserDeLimit(order);
        return Boolean.TRUE;
    }

    public void updateUserDeLimit(BizOrder order) {
        Long sysUserId = order.getSysUserId();
        // 更新其他配置状态为 1-失效
        this.disableUserDeConfig(sysUserId);

        // 保存用户最新配置
        SysUserDeConfig sysUserDeConfig = new SysUserDeConfig();
        sysUserDeConfig.setOrderId(order.getId());
        sysUserDeConfig.setEmployeeAmount(order.getEmployeeAmount());
        sysUserDeConfig.setSessionAmount(order.getSessionAmount());
        sysUserDeConfig.setKnowledgeBaseAmount(order.getKnowledgeBaseAmount());
        sysUserDeConfig.setKnowledgeBaseDocAmount(order.getKnowledgeBaseDocAmount());
        sysUserDeConfig.setKnowledgeBaseDocWordAmount(order.getKnowledgeBaseDocWordAmount());

        // 天数
        Integer days = order.getDays();
        sysUserDeConfig.setDays(days);
        // 开始、过期时间
        LocalDate startDate = LocalDate.now();
        sysUserDeConfig.setStartDate(startDate);
        sysUserDeConfig.setExpireDate(startDate.plusDays(days));
        sysUserDeConfigMapper.insert(sysUserDeConfig);
    }

    /**
     * 更新用户其他配置状态为 1-失效
     *
     * @param sysUserId 用户Id
     */
    private void disableUserDeConfig(Long sysUserId) {
        SysUserDeConfig config = new SysUserDeConfig();
        config.setSysUserId(sysUserId);
        config.setStatus(1);
        sysUserDeConfigMapper.updateById(config);
    }

}
