package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizOrderMapper extends BaseMapper<BizOrder> {
    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    BizOrder selectBizOrderById(Long id);

    /**
     * 查询订单列表
     *
     * @param bizOrder 订单
     * @return 订单集合
     */
    List<BizOrder> selectBizOrderList(BizOrder bizOrder);

    /**
     * 新增订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    int insertBizOrder(BizOrder bizOrder);

    /**
     * 修改订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    int updateBizOrder(BizOrder bizOrder);

    /**
     * 删除订单
     *
     * @param id 订单主键
     * @return 结果
     */
    int deleteBizOrderById(Long id);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizOrderByIds(Long[] ids);
}
