package com.digitalemployee.business.modules.chat.controller;

import com.digitalemployee.business.modules.chat.service.ChatLoginService;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/de/chatToken")
@RestController
@RequiredArgsConstructor
public class ChatTokenController extends BaseController {

    private final ChatLoginService chatLoginService;

    @GetMapping("/generateAppToken")
    public AjaxResult generateAppToken() {
        Long userId = getUserId();
        return AjaxResult.success(chatLoginService.generateAppToken(userId));
    }

}
