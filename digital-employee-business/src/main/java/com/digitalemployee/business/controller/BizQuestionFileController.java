package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizQuestionFile;
import com.digitalemployee.business.service.IBizQuestionFileService;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 问答库文档Controller
 * 
 * @author aicyber
 * @date 2023-11-29
 */
@RestController
@RequestMapping("/de/questionFile")
@RequiredArgsConstructor
public class BizQuestionFileController extends BaseController
{

    private final IBizQuestionFileService bizQuestionFileService;

    /**
     * 查询问答库文档列表
     */
    @PreAuthorize("@ss.hasPermi('check:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizQuestionFile bizQuestionFile)
    {
        startPage();
        List<BizQuestionFile> list = bizQuestionFileService.selectBizQuestionFileList(bizQuestionFile);
        return getDataTable(list);
    }


    /**
     * 查询问答库文档列表,无分页
     */
    @PreAuthorize("@ss.hasPermi('check:file:list')")
    @GetMapping("/selectList")
    public TableDataInfo selectList(BizQuestionFile bizQuestionFile)
    {
        List<BizQuestionFile> list = bizQuestionFileService.selectBizQuestionFileList(bizQuestionFile);
        return getDataTable(list);
    }
    /**
     * 获取问答库文档详细信息
     */
    @PreAuthorize("@ss.hasPermi('check:file:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bizQuestionFileService.selectBizQuestionFileById(id));
    }

    /**
     * 新增问答库文档
     */
    @PreAuthorize("@ss.hasPermi('check:file:add')")
    @Log(title = "问答库文档", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizQuestionFile bizQuestionFile)
    {
        return toAjax(bizQuestionFileService.insertBizQuestionFile(bizQuestionFile));
    }

    /**
     * 修改问答库文档
     */
    @PreAuthorize("@ss.hasPermi('check:file:edit')")
    @Log(title = "问答库文档", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizQuestionFile bizQuestionFile)
    {
        return toAjax(bizQuestionFileService.updateBizQuestionFile(bizQuestionFile));
    }

    /**
     * 删除问答库文档
     */
    @PreAuthorize("@ss.hasPermi('check:file:remove')")
    @Log(title = "问答库文档", businessType = BusinessType.DELETE)
	@DeleteMapping("/removeFile/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizQuestionFileService.deleteBizQuestionFileByIds(ids));
    }
    @ResponseBody
    @PostMapping(value = "/uploadTemplate")
    public AjaxResult uploadTemplate(MultipartFile file) throws IOException {
        bizQuestionFileService.uploadTemplate(file);
        return success();
    }

    @GetMapping("/downInChargeOfTemplate")
    public void downInChargeOfTemplate(HttpServletResponse response) {
        bizQuestionFileService.downInChargeOfTemplate(response);
    }

}
