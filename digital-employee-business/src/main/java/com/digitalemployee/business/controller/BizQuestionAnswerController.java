package com.digitalemployee.business.controller;

import com.digitalemployee.business.domain.BizQuestionAnswer;
import com.digitalemployee.business.mapper.BizQuestionAnswerMapper;
import com.digitalemployee.business.service.IBizQuestionAnswerService;
import com.digitalemployee.business.vo.DigitalEmployeeIdVo;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 问答库Controller
 *
 * @author aicyber
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/de/questionAnswer")
@RequiredArgsConstructor
public class BizQuestionAnswerController extends BaseController {

    private final IBizQuestionAnswerService bizQuestionAnswerService;


    private final BizQuestionAnswerMapper bizQuestionAnswerMapper;

    /**
     * 查询问答库列表
     */
    @PreAuthorize("@ss.hasPermi('de:answer:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizQuestionAnswer bizQuestionAnswer,Date startTime, Date endTime) {
        startPage();
        List<BizQuestionAnswer> list = bizQuestionAnswerService.selectBizQuestionAnswerList(bizQuestionAnswer,startTime,endTime);
        List<BizQuestionAnswer> bizQuestionAnswerList = bizQuestionAnswerMapper.selectBizQuestionAnswerList(bizQuestionAnswer, startTime, endTime);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(bizQuestionAnswerList.size());
        return dataTable;
    }


    /**
     * 获取问答库详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:answer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizQuestionAnswerService.selectBizQuestionAnswerById(id));
    }

    /**
     * 新增问答库
     */
    @PreAuthorize("@ss.hasPermi('de:answer:add')")
    @Log(title = "问答库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizQuestionAnswer bizQuestionAnswer) {
        bizQuestionAnswer.setCreateBy(getUsername());
        return toAjax(bizQuestionAnswerService.insertBizQuestionAnswer(bizQuestionAnswer));
    }

    /**
     * 修改问答库
     */
    @PreAuthorize("@ss.hasPermi('de:answer:edit')")
    @Log(title = "问答库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizQuestionAnswer bizQuestionAnswer) {
        bizQuestionAnswer.setUpdateBy(getUsername());
        return toAjax(bizQuestionAnswerService.updateBizQuestionAnswer(bizQuestionAnswer));
    }

    /**
     * 删除问答库
     */
    @PreAuthorize("@ss.hasPermi('de:answer:remove')")
    @Log(title = "问答库", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeQuestionAnswer/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizQuestionAnswerService.deleteBizQuestionAnswerByIds(ids));
    }

    /**
     * 问答库文件导入
     */

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult upload(MultipartFile[] files, Long digitalEmployeeId, HttpServletRequest request) throws IOException {
        String username = getUsername();
        String result = bizQuestionAnswerService.readExcelFile(files, username, digitalEmployeeId,request);
        return success(result);
    }
    @PostMapping("/querySimilarQuestionList")
    public TableDataInfo querySimilarQuestionList(@RequestBody DigitalEmployeeIdVo digitalEmployeeIdVo) {
        startPage();
        List<BizQuestionAnswer> list = bizQuestionAnswerService.querySimilarQuestionList(digitalEmployeeIdVo);
        return getDataTable(list);
    }

}
