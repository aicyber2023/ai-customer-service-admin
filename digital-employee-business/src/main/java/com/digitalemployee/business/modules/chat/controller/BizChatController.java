package com.digitalemployee.business.modules.chat.controller;


import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/de/bizChat")
@RequiredArgsConstructor
public class BizChatController extends BaseController {

    private final BizChatService bizChatService;

    @Anonymous
    @CrossOrigin
    @PostMapping("/anonymousChat")
    public AjaxResult anonymousChat(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        ChatDataDTO chatData = bizChatService.initChatData(param, null, request, response);
        return AjaxResult.success(bizChatService.chat(chatData));
    }

    @CrossOrigin
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        Long loginUserId = getUserId();
        if (loginUserId == null) {
            throw new BaseException("请登录后再进行测试！");
        }
        ChatDataDTO chatData = bizChatService.initChatData(param, loginUserId, request, response);
        return AjaxResult.success(bizChatService.chat(chatData));
    }

    //history

    @PostMapping("/anonymousHistory")
    public AjaxResult anonymousHistory(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        return success(bizChatService.anonymousHistory(param, null, request, response));
    }

    @PostMapping("/chatHistory")
    public void chatHistory(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {

    }

}
