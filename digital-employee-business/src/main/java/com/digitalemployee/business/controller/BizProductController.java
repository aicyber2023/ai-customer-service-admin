package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizProduct;
import com.digitalemployee.business.service.IBizProductService;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 产品Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/product")
@RequiredArgsConstructor
public class BizProductController extends BaseController {

    private final IBizProductService bizProductService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('de:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizProduct bizProduct) {
        startPage();
        List<BizProduct> list = bizProductService.selectBizProductList(bizProduct);
        return getDataTable(list);
    }

    /**
     * 查询产品列表 - 无分页
     */
    @PreAuthorize("@ss.hasPermi('de:product:list')")
    @GetMapping("/selectList")
    public AjaxResult selectList(BizProduct bizProduct) {
        return success(bizProductService.selectBizProductList(bizProduct));
    }


    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizProductService.getById(id));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('de:product:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizProduct bizProduct) {
        bizProduct.setCreateTime(new Date());
        bizProduct.setCreateBy(String.valueOf(getUserId()));
        return toAjax(bizProductService.save(bizProduct));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('de:product:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizProduct bizProduct) {
        bizProduct.setUpdateTime(new Date());
        bizProduct.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizProductService.updateById(bizProduct));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('de:product:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizProductService.deleteBizProductByIds(ids));
    }

}
