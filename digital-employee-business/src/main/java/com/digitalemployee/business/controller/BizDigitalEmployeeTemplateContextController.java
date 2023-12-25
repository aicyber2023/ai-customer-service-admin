package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizDigitalEmployeeTemplateContext;
import com.digitalemployee.business.service.IBizDigitalEmployeeTemplateContextService;
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
 * 数字员工模板上下文Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/templateContext")
@RequiredArgsConstructor
public class BizDigitalEmployeeTemplateContextController extends BaseController {

    private final IBizDigitalEmployeeTemplateContextService bizDigitalEmployeeTemplateContextService;

    /**
     * 查询数字员工模板上下文列表
     */
    @PreAuthorize("@ss.hasPermi('de:templateContext:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext) {
        startPage();
        List<BizDigitalEmployeeTemplateContext> list = bizDigitalEmployeeTemplateContextService.selectBizDigitalEmployeeTemplateContextList(bizDigitalEmployeeTemplateContext);
        return getDataTable(list);
    }

    /**
     * 获取数字员工模板上下文详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:templateContext:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizDigitalEmployeeTemplateContextService.selectBizDigitalEmployeeTemplateContextById(id));
    }

    /**
     * 新增数字员工模板上下文
     */
    @PreAuthorize("@ss.hasPermi('de:templateContext:add')")
    @Log(title = "数字员工模板上下文", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext) {
        return toAjax(bizDigitalEmployeeTemplateContextService.insertBizDigitalEmployeeTemplateContext(bizDigitalEmployeeTemplateContext));
    }

    /**
     * 修改数字员工模板上下文
     */
    @PreAuthorize("@ss.hasPermi('de:templateContext:edit')")
    @Log(title = "数字员工模板上下文", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDigitalEmployeeTemplateContext bizDigitalEmployeeTemplateContext) {
        return toAjax(bizDigitalEmployeeTemplateContextService.updateBizDigitalEmployeeTemplateContext(bizDigitalEmployeeTemplateContext));
    }

    /**
     * 删除数字员工模板上下文
     */
    @PreAuthorize("@ss.hasPermi('de:templateContext:remove')")
    @Log(title = "数字员工模板上下文", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizDigitalEmployeeTemplateContextService.deleteBizDigitalEmployeeTemplateContextByIds(ids));
    }
}
