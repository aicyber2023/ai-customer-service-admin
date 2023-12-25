package com.digitalemployee.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalemployee.business.domain.BizProduct;
import com.digitalemployee.business.mapper.BizProductMapper;
import com.digitalemployee.business.service.IBizProductService;
import com.digitalemployee.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品Service业务层处理
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Service
@RequiredArgsConstructor
public class BizProductServiceImpl extends ServiceImpl<BizProductMapper, BizProduct> implements IBizProductService {

    private final BizProductMapper bizProductMapper;

    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public BizProduct selectBizProductById(Long id) {
        return bizProductMapper.selectBizProductById(id);
    }

    /**
     * 查询产品列表
     *
     * @param bizProduct 产品
     * @return 产品
     */
    @Override
    public List<BizProduct> selectBizProductList(BizProduct bizProduct) {
        return bizProductMapper.selectBizProductList(bizProduct);
    }

    /**
     * 新增产品
     *
     * @param bizProduct 产品
     * @return 结果
     */
    @Override
    public int insertBizProduct(BizProduct bizProduct) {
        bizProduct.setCreateTime(DateUtils.getNowDate());
        return bizProductMapper.insertBizProduct(bizProduct);
    }

    /**
     * 修改产品
     *
     * @param bizProduct 产品
     * @return 结果
     */
    @Override
    public int updateBizProduct(BizProduct bizProduct) {
        bizProduct.setUpdateTime(DateUtils.getNowDate());
        return bizProductMapper.updateBizProduct(bizProduct);
    }

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品主键
     * @return 结果
     */
    @Override
    public int deleteBizProductByIds(Long[] ids) {
        return bizProductMapper.deleteBizProductByIds(ids);
    }

    /**
     * 删除产品信息
     *
     * @param id 产品主键
     * @return 结果
     */
    @Override
    public int deleteBizProductById(Long id) {
        return bizProductMapper.deleteBizProductById(id);
    }
}
