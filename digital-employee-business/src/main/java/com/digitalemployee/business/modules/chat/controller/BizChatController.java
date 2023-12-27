package com.digitalemployee.business.modules.chat.controller;


import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/de/bizChat")
@RequiredArgsConstructor
public class BizChatController {

    private final BizChatService bizChatService;


    @Anonymous
    @PostMapping("/anonymousChat")
    public AjaxResult anonymousChat(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        return AjaxResult.success(bizChatService.xxx(param, request, response));
    }

    @PostMapping("/chat")
    public AjaxResult chat() {
        //

        return AjaxResult.success();
    }


}
