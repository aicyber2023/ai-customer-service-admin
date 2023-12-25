package com.digitalemployee.business.modules.chat.controller;

import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.modules.chat.domain.ChatLoginBody;
import com.digitalemployee.business.modules.chat.service.ChatLoginService;
import com.digitalemployee.business.modules.chat.service.ChatService;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.business.service.IBizUserService;
import com.digitalemployee.business.utils.HttpUtils;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.annotation.ChatAuth;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/de/chat")
@RequiredArgsConstructor
@Anonymous
public class ChatController {

    private final ChatLoginService chatLoginService;

    private final ChatService chatService;

    private final IBizUserService bizUserService;

    private final IBizDigitalEmployeeService digitalEmployeeService;

    /**
     * 聊天用户登录
     *
     * @param chatLoginBody ChatLoginBody
     * @return AjaxResult
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody ChatLoginBody chatLoginBody) {
        return AjaxResult.success(chatLoginService.login(chatLoginBody));
    }

    /**
     * 获取用户信息
     *
     * @param userId userId
     * @return AjaxResult
     */
    @ChatAuth
    @PostMapping("/getUserInfo")
    public AjaxResult getUserInfo(Long userId) {
        return AjaxResult.success(chatService.getUserInfo(userId));
    }

    /**
     * 修改用户信息
     *
     * @param userId userId
     * @return AjaxResult
     */
    @ChatAuth
    @PostMapping("/modifyUser")
    public AjaxResult modifyUser(Long userId, @RequestBody BizUser user) {
        user.setId(userId);
        String password = user.getPassword();
        if (password != null && !password.isEmpty()) {
            user.setPassword(SecurityUtils.encryptPassword(password));
        }
        return AjaxResult.success(chatService.modifyUser(user));
    }

    /**
     * 重置密码
     *
     * @param userId      主键
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return AjaxResult
     */
    @ChatAuth
    @GetMapping("/updateUserPwd")
    public AjaxResult updatePwd(Long userId, String oldPassword, String newPassword) {
        BizUser user = bizUserService.selectBizUserById(userId);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }

        user.setPassword(SecurityUtils.encryptPassword(newPassword));
        if (bizUserService.updateById(user)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 上传头像
     *
     * @param file   头像文件
     * @param userId userId
     * @return AjaxResult
     */
    @ChatAuth
    @PostMapping("/uploadUserAvatar")
    public AjaxResult uploadUserAvatar(@RequestParam(name = "userId", required = false) Long userId,
                                       @RequestParam(value = "file") MultipartFile file) {
        bizUserService.saveAvatar(file, userId);
        return AjaxResult.success(Boolean.TRUE);
    }

    /**
     * 下载头像
     *
     * @param userId userId
     * @return AjaxResult
     */
    @ChatAuth
    @PostMapping("/showUserAvatar")
    public ResponseEntity<byte[]> showAvatar(Long userId) {
        BizUser user = chatService.selectUserById(userId);
        if (user != null && user.getAvatar() != null) {
            return HttpUtils.getResponseBody(user.getAvatar(), user.getAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 获取用户数字员工
     *
     * @param userId userId
     * @return AjaxResult
     */
    @ChatAuth
    @PostMapping("/selectUserDe")
    public AjaxResult selectUserDe(Long userId) {
        return AjaxResult.success(chatService.selectUserDe(userId));
    }

    /**
     * 下载数字员工头像
     *
     * @param digitalEmployeeId digitalEmployeeId
     * @return AjaxResult
     */
    // @ChatAuth
    @PostMapping("/showDeAvatar")
    public ResponseEntity<byte[]> showDeAvatar(Long digitalEmployeeId) {
        BizDigitalEmployee employee = digitalEmployeeService.getById(digitalEmployeeId);
        if (employee != null && employee.getAvatar() != null) {
            return HttpUtils.getResponseBody(employee.getAvatar(), employee.getAvatarContentType());
        }
        return ResponseEntity.notFound().build();
    }

}
