package com.digitalemployee.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplate;
import com.digitalemployee.business.domain.BizDigitalEmployeeTemplateContext;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeTemplateProcedure;
import com.digitalemployee.business.modules.de.service.IBizDigitalEmployeeTemplateProcedureService;
import com.digitalemployee.business.service.IBizDigitalEmployeeTemplateContextService;
import com.digitalemployee.business.service.IBizDigitalEmployeeTemplateService;
import com.digitalemployee.business.utils.HttpUtils;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数字员工模板Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/employeeTemplate")
@RequiredArgsConstructor
public class BizDigitalEmployeeTemplateController extends BaseController {

    private final IBizDigitalEmployeeTemplateService bizDigitalEmployeeTemplateService;

    private final IBizDigitalEmployeeTemplateContextService contextService;
    private final IBizDigitalEmployeeTemplateProcedureService procedureService;

    /**
     * 查询数字员工模板列表
     */
    // @PreAuthorize("@ss.hasPermi('de:employeeTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        startPage();
        List<BizDigitalEmployeeTemplate> list = bizDigitalEmployeeTemplateService.selectBizDigitalEmployeeTemplateList(bizDigitalEmployeeTemplate);
        return getDataTable(list);
    }

    // @PreAuthorize("@ss.hasPermi('de:employeeTemplate:list')")
    @GetMapping("/selectList")
    public AjaxResult selectList(BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        List<BizDigitalEmployeeTemplate> data = bizDigitalEmployeeTemplateService.selectBizDigitalEmployeeTemplateList(bizDigitalEmployeeTemplate);
        this.selectTemplateData(data);
        return success(data);
    }

    @GetMapping("/selectUsedTemplateList")
    public AjaxResult selectList() {
        Long userId = getUserId();
        if (getLoginUser().getUser().isAdmin()) {
            userId = null;
        }
        return success(bizDigitalEmployeeTemplateService.selectUsedTemplateList(userId));
    }

    private void selectTemplateData(List<BizDigitalEmployeeTemplate> data) {
        List<Long> idList = data.stream().map(BizDigitalEmployeeTemplate::getId).collect(Collectors.toList());

        LambdaQueryWrapper<BizDigitalEmployeeTemplateContext> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BizDigitalEmployeeTemplateContext::getTemplateId, idList);
        List<BizDigitalEmployeeTemplateContext> contexts = contextService.list(wrapper);
        Map<Long, List<BizDigitalEmployeeTemplateContext>> map = contexts.stream().collect(Collectors.groupingBy(BizDigitalEmployeeTemplateContext::getTemplateId));
        data.forEach(template -> template.setContext(map.get(template.getId())));

        LambdaQueryWrapper<BizDigitalEmployeeTemplateProcedure> procedureWrapper = Wrappers.lambdaQuery();
        procedureWrapper.in(BizDigitalEmployeeTemplateProcedure::getTemplateId, idList);
        List<BizDigitalEmployeeTemplateProcedure> procedureList = procedureService.list(procedureWrapper);
        Map<Long, List<BizDigitalEmployeeTemplateProcedure>> procedureMap = procedureList.stream().collect(Collectors.groupingBy(BizDigitalEmployeeTemplateProcedure::getTemplateId));
        data.forEach(template -> template.setProcedureList(procedureMap.get(template.getId())));
    }

    /**
     * 获取数字员工模板详细信息
     */
    // @PreAuthorize("@ss.hasPermi('de:employeeTemplate:query')")
    @GetMapping(value = "/{id}")
    @Anonymous
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizDigitalEmployeeTemplateService.selectBizDigitalEmployeeTemplateById(id));
    }

    /**
     * 新增数字员工模板
     */
    // @PreAuthorize("@ss.hasPermi('de:employeeTemplate:add')")
    @Log(title = "数字员工模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        bizDigitalEmployeeTemplate.setCreateTime(new Date());
        bizDigitalEmployeeTemplate.setCreateBy(String.valueOf(getUserId()));
        bizDigitalEmployeeTemplate.setDeptId(getDeptId());
        bizDigitalEmployeeTemplate.setUserId(getUserId());
        return success(bizDigitalEmployeeTemplateService.insertBizDigitalEmployeeTemplate(bizDigitalEmployeeTemplate));
    }

    /**
     * 修改数字员工模板
     */
    // @PreAuthorize("@ss.hasPermi('de:employeeTemplate:edit')")
    @Log(title = "数字员工模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDigitalEmployeeTemplate bizDigitalEmployeeTemplate) {
        bizDigitalEmployeeTemplate.setUpdateTime(new Date());
        bizDigitalEmployeeTemplate.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizDigitalEmployeeTemplateService.updateBizDigitalEmployeeTemplate(bizDigitalEmployeeTemplate));
    }

    @GetMapping("/updateTemplateStatus")
    public AjaxResult updateTemplateStatus(Long templateId, Integer status) {
        BizDigitalEmployeeTemplate template = new BizDigitalEmployeeTemplate();
        template.setId(templateId);
        template.setStatus(status);
        return AjaxResult.success(bizDigitalEmployeeTemplateService.updateById(template));
    }

    /**
     * 删除数字员工模板
     */
    // @PreAuthorize("@ss.hasPermi('de:employeeTemplate:remove')")
    @Log(title = "数字员工模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizDigitalEmployeeTemplateService.deleteBizDigitalEmployeeTemplateByIds(ids));
    }

    /**
     * 上传头像
     *
     * @param file       头像文件
     * @param templateId templateId
     * @return AjaxResult
     */
    @PostMapping("/uploadAvatar")
    public AjaxResult uploadAvatar(@RequestParam(value = "file") MultipartFile file, @RequestParam(name = "templateId") String templateId) {
        bizDigitalEmployeeTemplateService.saveAvatar(file, templateId);
        return success(Boolean.TRUE);
    }

    /**
     * 下载头像
     *
     * @param templateId templateId
     * @return ResponseEntity<byte [ ]>
     */
    @Anonymous
    @GetMapping("/showAvatar/{templateId}")
    public ResponseEntity<byte[]> showAvatar(@PathVariable("templateId") Long templateId) {
        BizDigitalEmployeeTemplate template = bizDigitalEmployeeTemplateService.getById(templateId);
        if (template != null && template.getAvatar() != null) {
            return HttpUtils.getResponseBody(template.getAvatar(), template.getAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 上传公司头像
     *
     * @param file       头像文件
     * @param templateId templateId
     * @return AjaxResult
     */
    @PostMapping("/uploadCompanyAvatar")
    public AjaxResult uploadCompanyAvatar(@RequestParam(value = "file") MultipartFile file, @RequestParam(name = "templateId") String templateId) {
        bizDigitalEmployeeTemplateService.saveCompanyAvatar(file, templateId);
        return success(Boolean.TRUE);
    }

    /**
     * 下载公司头像
     *
     * @param templateId templateId
     * @return ResponseEntity<byte [ ]>
     */
    @Anonymous
    @GetMapping("/showCompanyAvatar/{templateId}")
    public ResponseEntity<byte[]> showCompanyAvatar(@PathVariable("templateId") Long templateId) {
        BizDigitalEmployeeTemplate template = bizDigitalEmployeeTemplateService.getById(templateId);
        if (template != null && template.getCompanyAvatar() != null) {
            return HttpUtils.getResponseBody(template.getCompanyAvatar(), template.getCompanyAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

}
