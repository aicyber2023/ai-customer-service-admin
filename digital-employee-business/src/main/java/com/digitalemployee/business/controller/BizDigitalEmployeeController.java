package com.digitalemployee.business.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.business.utils.HttpUtils;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 客户数字员工Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/digitalEmployee")
@RequiredArgsConstructor
public class BizDigitalEmployeeController extends BaseController {

    private final IBizDigitalEmployeeService bizDigitalEmployeeService;

    /**
     * 查询客户数字员工列表
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployee:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDigitalEmployee bizDigitalEmployee) {
        startPage();
        List<BizDigitalEmployee> list = bizDigitalEmployeeService.selectBizDigitalEmployeeList(bizDigitalEmployee);
        bizDigitalEmployeeService.findDigiEmpData(list);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('de:digitalEmployee:list')")
    @GetMapping("/selectList")
    public AjaxResult selectList(BizDigitalEmployee bizDigitalEmployee) {
        List<BizDigitalEmployee> data = bizDigitalEmployeeService.selectBizDigitalEmployeeList(bizDigitalEmployee);
        return success(data);
    }

    /**
     * 获取客户数字员工详细信息
     */
    // @PreAuthorize("@ss.hasPermi('de:digitalEmployee:query')")
    @GetMapping(value = "/{id}")
    @Anonymous
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizDigitalEmployeeService.selectBizDigitalEmployeeById(id));
    }

    /**
     * 新增客户数字员工
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployee:add')")
    @Log(title = "客户数字员工", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDigitalEmployee bizDigitalEmployee) {
        bizDigitalEmployee.setCreateTime(new Date());
        bizDigitalEmployee.setCreateBy(String.valueOf(getUserId()));
        bizDigitalEmployee.setDeptId(getDeptId());
        bizDigitalEmployee.setUserId(getUserId());
        return success(bizDigitalEmployeeService.insertBizDigitalEmployee(bizDigitalEmployee));
    }

    /**
     * 修改客户数字员工
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployee:edit')")
    @Log(title = "客户数字员工", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDigitalEmployee bizDigitalEmployee) {
        bizDigitalEmployee.setUpdateTime(new Date());
        bizDigitalEmployee.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizDigitalEmployeeService.updateBizDigitalEmployee(bizDigitalEmployee));
    }

    /**
     * 删除客户数字员工
     */
    @PreAuthorize("@ss.hasPermi('de:digitalEmployee:remove')")
    @Log(title = "客户数字员工", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizDigitalEmployeeService.deleteBizDigitalEmployeeByIdsLogic(ids));
    }

    /**
     * 上传头像
     *
     * @param file       头像文件
     * @param employeeId employeeId
     * @return AjaxResult
     */
    @PostMapping("/uploadAvatar")
    public AjaxResult uploadAvatar(@RequestParam(value = "file") MultipartFile file, @RequestParam(name = "employeeId") String employeeId) {
        bizDigitalEmployeeService.saveAvatar(file, employeeId);
        return success(Boolean.TRUE);
    }

    /**
     * 下载头像
     *
     * @param employeeId employeeId
     * @return ResponseEntity<byte [ ]>
     */
    @Anonymous
    @GetMapping("/showAvatar/{employeeId}")
    public ResponseEntity<byte[]> showAvatar(@PathVariable("employeeId") Long employeeId) {
        BizDigitalEmployee employee = bizDigitalEmployeeService.getById(employeeId);
        if (employee != null && employee.getAvatar() != null) {
            return HttpUtils.getResponseBody(employee.getAvatar(), employee.getAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 上传公司头像
     *
     * @param file       头像文件
     * @param employeeId employeeId
     * @return AjaxResult
     */
    @PostMapping("/uploadCompanyAvatar")
    public AjaxResult uploadCompanyAvatar(@RequestParam(value = "file") MultipartFile file, @RequestParam(name = "employeeId") String employeeId) {
        bizDigitalEmployeeService.saveCompanyAvatar(file, employeeId);
        return success(Boolean.TRUE);
    }

    /**
     * 下载公司头像
     *
     * @param employeeId employeeId
     * @return ResponseEntity<byte [ ]>
     */
    @Anonymous
    @GetMapping("/showCompanyAvatar/{employeeId}")
    public ResponseEntity<byte[]> showCompanyAvatar(@PathVariable("employeeId") Long employeeId) {
        BizDigitalEmployee employee = bizDigitalEmployeeService.getById(employeeId);
        if (employee != null && employee.getCompanyAvatar() != null) {
            return HttpUtils.getResponseBody(employee.getCompanyAvatar(), employee.getCompanyAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/updateApiKey")
    public AjaxResult updateApiKey(@RequestBody BizDigitalEmployee digitalEmployee) {
        if (digitalEmployee.getId() == null) {
            return error("更新apiKey异常，请联系管理员");
        }
        BizDigitalEmployee employee = bizDigitalEmployeeService.getById(digitalEmployee.getId());
        employee.setEmployeeKey(IdUtil.fastSimpleUUID());
        bizDigitalEmployeeService.updateById(employee);
        return AjaxResult.success(employee.getEmployeeKey());
    }

}
