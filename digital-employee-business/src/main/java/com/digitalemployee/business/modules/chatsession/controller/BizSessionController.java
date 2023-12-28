package com.digitalemployee.business.modules.chatsession.controller;

import com.digitalemployee.business.modules.chatsession.domain.BizSession;
import com.digitalemployee.business.modules.chatsession.service.IBizSessionService;
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
 * 会话sessionController
 *
 * @author aicyber
 * @date 2023-12-27
 */
@RestController
@RequestMapping("/de/chatSession")
@RequiredArgsConstructor
public class BizSessionController extends BaseController {

    private final IBizSessionService bizSessionService;

    /**
     * 查询会话session列表
     */
    @PreAuthorize("@ss.hasPermi('check:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSession bizSession) {
        // TODO: 2023/12/27 查询条件 + 关联表数据查询
        startPage();
        List<BizSession> list = bizSessionService.selectBizSessionList(bizSession);
        return getDataTable(list);
    }

    /**
     * 删除会话session
     */
    @PreAuthorize("@ss.hasPermi('check:session:remove')")
    @Log(title = "会话session", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizSessionService.deleteBizSessionByIds(ids));
    }

/*
    *//**
     * 获取会话session详细信息
     *//*
    @PreAuthorize("@ss.hasPermi('check:session:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizSessionService.selectBizSessionById(id));
    }

    *//**
     * 新增会话session
     *//*
    @PreAuthorize("@ss.hasPermi('check:session:add')")
    @Log(title = "会话session", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizSession bizSession) {
        return toAjax(bizSessionService.insertBizSession(bizSession));
    }

    *//**
     * 修改会话session
     *//*
    @PreAuthorize("@ss.hasPermi('check:session:edit')")
    @Log(title = "会话session", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizSession bizSession) {
        return toAjax(bizSessionService.updateBizSession(bizSession));
    }
    */

}
