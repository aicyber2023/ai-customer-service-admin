package com.digitalemployee.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalemployee.business.domain.BizProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品Mapper接口
 *
 * @author aicyber
 * @date 2023-08-22
 */
@Mapper
public interface BizProductMapper extends BaseMapper<BizProduct> {
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
     * 删除产品
     *
     * @param id 产品主键
     * @return 结果
     */
    int deleteBizProductById(Long id);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBizProductByIds(Long[] ids);
}
