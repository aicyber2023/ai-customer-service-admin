package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizDigitalEmployeeContext;
import com.digitalemployee.business.service.IBizDigitalEmployeeContextService;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户数字员工上下文Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/digitalEmployeeContext")
@RequiredArgsConstructor
public class BizDigitalEmployeeContextController extends BaseController {

    private final IBizDigitalEmployeeContextService bizDigitalEmployeeContextService;

    /**
     * 查询客户数字员工上下文列表
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployeeContext:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDigitalEmployeeContext bizDigitalEmployeeContext) {
        startPage();
        List<BizDigitalEmployeeContext> list = bizDigitalEmployeeContextService.selectBizDigitalEmployeeContextList(bizDigitalEmployeeContext);
        return getDataTable(list);
    }

    /**
     * 获取客户数字员工上下文详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployeeContext:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizDigitalEmployeeContextService.selectBizDigitalEmployeeContextById(id));
    }

    /**
     * 新增客户数字员工上下文
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployeeContext:add')")
    @Log(title = "客户数字员工上下文", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDigitalEmployeeContext bizDigitalEmployeeContext) {
        return toAjax(bizDigitalEmployeeContextService.insertBizDigitalEmployeeContext(bizDigitalEmployeeContext));
    }

    /**
     * 修改客户数字员工上下文
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployeeContext:edit')")
    @Log(title = "客户数字员工上下文", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDigitalEmployeeContext bizDigitalEmployeeContext) {
        return toAjax(bizDigitalEmployeeContextService.updateBizDigitalEmployeeContext(bizDigitalEmployeeContext));
    }

    /**
     * 删除客户数字员工上下文
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployeeContext:remove')")
    @Log(title = "客户数字员工上下文", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizDigitalEmployeeContextService.deleteBizDigitalEmployeeContextByIds(ids));
    }
}
