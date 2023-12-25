package com.digitalemployee.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalemployee.business.domain.BizProduct;

import java.util.List;

/**
 * 产品Service接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
public interface IBizProductService extends IService<BizProduct> {
    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    BizProduct selectBizProductById(Long id);

    /**
     * 查询产品列表
     *
     * @param bizProduct 产品
     * @return 产品集合
     */
    List<BizProduct> selectBizProductList(BizProduct bizProduct);

    /**
     * 新增产品
     *
     * @param bizProduct 产品
     * @return 结果
     */
    int insertBizProduct(BizProduct bizProduct);

    /**
     * 修改产品
     *
     * @param bizProduct 产品
     * @return 结果
     */
    int updateBizProduct(BizProduct bizProduct);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品主键集合
     * @return 结果
     */
    int deleteBizProductByIds(Long[] ids);

    /**
     * 删除产品信息
     *
     * @param id 产品主键
     * @return 结果
     */
    int deleteBizProductById(Long id);
}
