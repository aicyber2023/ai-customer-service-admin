package com.digitalemployee.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.domain.BizKnowledgeBaseFile;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.constant.HttpStatus;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import com.digitalemployee.common.exception.base.BaseException;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 知识库Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/knowledgeBase")
@RequiredArgsConstructor
public class BizKnowledgeBaseController extends BaseController {

    private final IBizKnowledgeBaseService bizKnowledgeBaseService;

    /**
     * 查询知识库列表
     */
    @PreAuthorize("@ss.hasPermi('check:base:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizKnowledgeBase bizKnowledgeBase) {
        startPage();
        List<BizKnowledgeBase> list = bizKnowledgeBaseService.selectBizKnowledgeBaseList(bizKnowledgeBase);
        return getDataTable(list);
    }

    /**
     * 获取知识库详细信息
     */
    @PreAuthorize("@ss.hasPermi('check:base:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizKnowledgeBaseService.selectBizKnowledgeBaseById(id));
    }

    /**
     * 新增知识库
     */
    @PreAuthorize("@ss.hasPermi('check:base:add')")
    @Log(title = "知识库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizKnowledgeBase bizKnowledgeBase) {
        Long userId = getUserId();

        bizKnowledgeBase.setCreateTime(new Date());
        bizKnowledgeBase.setCreateBy(String.valueOf(userId));

        bizKnowledgeBase.setStatus(0);
        bizKnowledgeBase.setSysUserId(userId);
        bizKnowledgeBase.setDeptId(getDeptId());
        bizKnowledgeBase.setUserId(getUserId());
        return toAjax(bizKnowledgeBaseService.insertBizKnowledgeBase(bizKnowledgeBase));
    }

    /**
     * 修改知识库
     */
    @PreAuthorize("@ss.hasPermi('check:base:edit')")
    @Log(title = "知识库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizKnowledgeBase bizKnowledgeBase) {
        bizKnowledgeBase.setUpdateTime(new Date());
        bizKnowledgeBase.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizKnowledgeBaseService.updateBizKnowledgeBase(bizKnowledgeBase));
    }

    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(Long digitalEmployeeId, MultipartFile[] files) {
        return bizKnowledgeBaseService.uploadFile(digitalEmployeeId, files);
    }

    @GetMapping("/appendFileToKnowledgeBase")
    public AjaxResult appendFileToKnowledgeBase(Long knowledgeFileId) {
        return success(bizKnowledgeBaseService.appendFileToKnowledgeBase(knowledgeFileId));
    }

    /**
     *  删除知识库文件
     */
    @Log(title = "知识库文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeFile/{ids}")
    public AjaxResult removeFile(@PathVariable Long[] ids) {
        return success(bizKnowledgeBaseService.removeFile(ids));
    }

    /**
     * 删除知识库
     */
    @PreAuthorize("@ss.hasPermi('check:base:remove')")
    @Log(title = "知识库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizKnowledgeBaseService.deleteBizKnowledgeBaseByIds(ids));
    }

    @GetMapping("/selectKbFileList")
    public TableDataInfo selectKbFileList(BizKnowledgeBaseFile knowledgeBaseFile) {
        Long digitalEmployeeId = knowledgeBaseFile.getDigitalEmployeeId();
        if (digitalEmployeeId == null) {
            throw new BaseException("参数：数字员工ID 不能为空");
        }
        Long knowledgeBaseId = bizKnowledgeBaseService.getKnowledgeBaseIdByDeId(digitalEmployeeId);
        knowledgeBaseFile.setKnowledgeBaseId(knowledgeBaseId);

        startPage();
        List<BizKnowledgeBaseFile> list = bizKnowledgeBaseService.selectKbFileList(knowledgeBaseFile);
        return getDataTable(list);
    }

}
