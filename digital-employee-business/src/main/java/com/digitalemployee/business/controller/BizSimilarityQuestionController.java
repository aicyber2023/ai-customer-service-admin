package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizSimilarityQuestion;
import com.digitalemployee.business.service.IBizSimilarityQuestionService;
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
 * 相似问Controller
 *
 * @author aicyber
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/de/similarityQuestion")
@RequiredArgsConstructor
public class BizSimilarityQuestionController extends BaseController {

    private final IBizSimilarityQuestionService bizSimilarityQuestionService;

    /**
     * 查询相似问列表
     */
    @PreAuthorize("@ss.hasPermi('de:question:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSimilarityQuestion bizSimilarityQuestion) {
        startPage();
        List<BizSimilarityQuestion> list = bizSimilarityQuestionService.selectBizSimilarityQuestionList(bizSimilarityQuestion);
        return getDataTable(list);
    }


    /**
     * 获取相似问详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:question:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizSimilarityQuestionService.selectBizSimilarityQuestionById(id));
    }

    /**
     * 新增相似问
     */
    @PreAuthorize("@ss.hasPermi('de:question:add')")
    @Log(title = "相似问", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizSimilarityQuestion bizSimilarityQuestion) {
        return toAjax(bizSimilarityQuestionService.insertBizSimilarityQuestion(bizSimilarityQuestion));
    }

    /**
     * 修改相似问
     */
    @PreAuthorize("@ss.hasPermi('de:question:edit')")
    @Log(title = "相似问", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizSimilarityQuestion bizSimilarityQuestion) {
        return toAjax(bizSimilarityQuestionService.updateBizSimilarityQuestion(bizSimilarityQuestion));
    }

    /**
     * 删除相似问
     */
    @PreAuthorize("@ss.hasPermi('de:question:remove')")
    @Log(title = "相似问", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizSimilarityQuestionService.deleteBizSimilarityQuestionByIds(ids));
    }
}
