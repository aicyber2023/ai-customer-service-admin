package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizOrder;
import com.digitalemployee.business.service.IBizOrderService;
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
 * 订单Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/order")
@RequiredArgsConstructor
public class BizOrderController extends BaseController {

    private final IBizOrderService bizOrderService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasPermi('de:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizOrder bizOrder) {
        startPage();
        List<BizOrder> list = bizOrderService.selectBizOrderList(bizOrder);
        return getDataTable(list);
    }

    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizOrderService.selectBizOrderById(id));
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('de:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizOrder bizOrder) {
        bizOrder.setCreateTime(new Date());
        bizOrder.setCreateBy(String.valueOf(getUserId()));
        return toAjax(bizOrderService.insertBizOrder(bizOrder));
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasPermi('de:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizOrder bizOrder) {
        bizOrder.setUpdateTime(new Date());
        bizOrder.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizOrderService.updateBizOrder(bizOrder));
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('de:order:remove')")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizOrderService.deleteBizOrderByIds(ids));
    }


    /**
     * 执行订单，更新用户数字员工配置
     * @param id 订单ID
     * @return AjaxResult
     */
    @GetMapping(value = "/executeOrder/{id}")
    public AjaxResult executeOrder(@PathVariable("id") Long id) {
        return AjaxResult.success(bizOrderService.executeOrder(id));
    }

}
