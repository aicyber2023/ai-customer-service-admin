package com.digitalemployee.business.modules.chatsession.controller;

import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import com.digitalemployee.business.modules.chatsession.service.IBizSessionRecordService;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对话详单Controller
 *
 * @author aicyber
 * @date 2023-12-27
 */
@RestController
@RequestMapping("/de/seesionRecord")
@RequiredArgsConstructor
public class BizSessionRecordController extends BaseController {

    private final IBizSessionRecordService bizSessionRecordService;

    @GetMapping("/list")
    public TableDataInfo list(BizSessionRecord bizSessionRecord) {
        // TODO: 2023/12/27 查询条件
        startPage();
        List<BizSessionRecord> list = bizSessionRecordService.selectBizSessionRecordList(bizSessionRecord);
        return getDataTable(list);
    }

/*

    @PreAuthorize("@ss.hasPermi('check:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizSessionRecordService.selectBizSessionRecordById(id));
    }

    @PreAuthorize("@ss.hasPermi('check:record:add')")
    @Log(title = "对话详单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizSessionRecord bizSessionRecord) {
        return toAjax(bizSessionRecordService.insertBizSessionRecord(bizSessionRecord));
    }

    @PreAuthorize("@ss.hasPermi('check:record:edit')")
    @Log(title = "对话详单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizSessionRecord bizSessionRecord) {
        return toAjax(bizSessionRecordService.updateBizSessionRecord(bizSessionRecord));
    }

    @PreAuthorize("@ss.hasPermi('check:record:remove')")
    @Log(title = "对话详单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizSessionRecordService.deleteBizSessionRecordByIds(ids));
    }
*/

}
