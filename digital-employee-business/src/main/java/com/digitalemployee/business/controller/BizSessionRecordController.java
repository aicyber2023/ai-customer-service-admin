package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizSessionRecord;
import com.digitalemployee.business.service.IBizSessionRecordService;
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
 * 对话详单Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/sessionRecord")
@RequiredArgsConstructor
public class BizSessionRecordController extends BaseController {

    private final IBizSessionRecordService bizSessionRecordService;

    /**
     * 查询对话详单列表
     */
    @PreAuthorize("@ss.hasPermi('de:sessionRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSessionRecord bizSessionRecord) {
        Long userId = getUserId();
        bizSessionRecord.getParams().put("sysUserId", userId);
        startPage();
        List<BizSessionRecord> list = bizSessionRecordService.selectBizSessionRecordListNew(bizSessionRecord);
        return getDataTable(list);
    }

    /**
     * 获取对话详单详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:sessionRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizSessionRecordService.selectBizSessionRecordById(id));
    }

    /**
     * 新增对话详单
     */
    @PreAuthorize("@ss.hasPermi('de:sessionRecord:add')")
    @Log(title = "对话详单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizSessionRecord bizSessionRecord) {
        bizSessionRecord.setCreateTime(new Date());
        bizSessionRecord.setCreateBy(String.valueOf(getUserId()));
        return toAjax(bizSessionRecordService.insertBizSessionRecord(bizSessionRecord));
    }

    /**
     * 修改对话详单
     */
    @PreAuthorize("@ss.hasPermi('de:sessionRecord:edit')")
    @Log(title = "对话详单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizSessionRecord bizSessionRecord) {
        bizSessionRecord.setUpdateTime(new Date());
        bizSessionRecord.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizSessionRecordService.updateBizSessionRecord(bizSessionRecord));
    }

    /**
     * 删除对话详单
     */
    @PreAuthorize("@ss.hasPermi('de:sessionRecord:remove')")
    @Log(title = "对话详单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizSessionRecordService.deleteBizSessionRecordByIds(ids));
    }
}
