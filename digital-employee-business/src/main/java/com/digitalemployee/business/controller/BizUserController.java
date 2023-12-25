package com.digitalemployee.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.service.IBizUserService;
import com.digitalemployee.business.utils.HttpUtils;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.annotation.Log;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.core.page.TableDataInfo;
import com.digitalemployee.common.enums.BusinessType;
import com.digitalemployee.common.exception.base.BaseException;
import com.digitalemployee.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 用户账户Controller
 *
 * @author aicyber
 * @date 2023-08-22
 */
@RestController
@RequestMapping("/de/bizUser")
@RequiredArgsConstructor
public class BizUserController extends BaseController {

    private final IBizUserService bizUserService;

    /**
     * 查询用户账户列表
     */
    @PreAuthorize("@ss.hasPermi('de:bizUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUser bizUser) {
        startPage();
        List<BizUser> list = bizUserService.selectBizUserList(bizUser);
        return getDataTable(list);
    }

    /**
     * 获取用户账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('de:bizUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizUserService.selectBizUserById(id));
    }

    /**
     * 新增用户账户
     */
    @PreAuthorize("@ss.hasPermi('de:bizUser:add')")
    @Log(title = "用户账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizUser bizUser) {
        if(!bizUserService.checkBizUserUnique(bizUser)) {
            throw new BaseException("客户服务对象的用户名已存在，请重新填写");
        }

        bizUser.setCreateTime(new Date());
        bizUser.setCreateBy(String.valueOf(getUserId()));
        bizUser.setDeptId(getDeptId());
        bizUser.setUserId(getUserId());
        String password = bizUser.getPassword();
        if (password != null && !password.isEmpty()) {
            bizUser.setPassword(SecurityUtils.encryptPassword(password));
        }
        return success(bizUserService.insertBizUser(bizUser));
    }

    /**
     * 修改用户账户
     */
    @PreAuthorize("@ss.hasPermi('de:bizUser:edit')")
    @Log(title = "用户账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizUser bizUser) {
        if(!bizUserService.checkBizUserUnique(bizUser)) {
            throw new BaseException("客户服务对象的用户名已存在，请重新填写");
        }
        bizUser.setUpdateTime(new Date());
        bizUser.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(bizUserService.updateBizUser(bizUser));
    }

    /**
     * 删除用户账户
     */
    @PreAuthorize("@ss.hasPermi('de:bizUser:remove')")
    @Log(title = "用户账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizUserService.deleteBizUserByIds(ids));
    }

    /**
     * 重置密码
     *
     * @param userId      主键
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return AjaxResult
     */
    @GetMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestParam Long userId, @RequestParam String newPassword) {
        BizUser user = bizUserService.selectBizUserById(userId);

        /*String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新密码不能与旧密码相同");
        }*/

        user.setPassword(SecurityUtils.encryptPassword(newPassword));
        if (bizUserService.updateById(user)) {
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }



    /**
     * 上传头像
     *
     * @param file   头像文件
     * @param userId userId
     * @return AjaxResult
     */
    @PostMapping("/uploadAvatar")
    public AjaxResult uploadAvatar(@RequestParam(value = "file") MultipartFile file, @RequestParam(name = "userId") Long userId) {
        bizUserService.saveAvatar(file, userId);
        return success(Boolean.TRUE);
    }

    /**
     * 下载头像
     *
     * @param userId userId
     * @return ResponseEntity<byte [ ]>
     */
    @Anonymous
    @GetMapping("/showAvatar/{userId}")
    public ResponseEntity<byte[]> showAvatar(@PathVariable("userId") Long userId) {
        BizUser user = bizUserService.getById(userId);
        if (user != null && user.getAvatar() != null) {
            return HttpUtils.getResponseBody(user.getAvatar(), user.getAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

}
