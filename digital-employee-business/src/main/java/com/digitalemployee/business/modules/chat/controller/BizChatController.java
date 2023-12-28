package com.digitalemployee.business.modules.chat.controller;


import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.ChatHistory;
import com.digitalemployee.business.api.domain.ChatParam;
import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/de/bizChat")
@RequiredArgsConstructor
public class BizChatController extends BaseController {

    private final BizChatService bizChatService;

    @Anonymous
    @PostMapping("/anonymousChat")
    public AjaxResult anonymousChat(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        ChatDataDTO chatData = bizChatService.initChatData(param, null, request, response);
        return AjaxResult.success(bizChatService.chat(chatData));
    }

    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        Long loginUserId = getUserId();
        if (loginUserId == null) {
            throw new BaseException("请登录后再进行测试！");
        }
        ChatDataDTO chatData = bizChatService.initChatData(param, loginUserId, request, response);
        return AjaxResult.success(bizChatService.chat(chatData));
    }


    private final RemoteModelService remoteModelService;

    @Anonymous
    @PostMapping("/test")
    public AjaxResult test(@RequestBody List<String> param) {
        ChatParam chatParam = new ChatParam();
        chatParam.setUser_input("你刚才说了什么？");
        chatParam.setTemperature(0.5);
        chatParam.setPresence_penalty(0.5);
        chatParam.setFrequency_penalty(0.5);
        List<ChatHistory> history = new ArrayList<>();
        param.forEach(p -> {
            String[] split = p.split(",");
            history.add(new ChatHistory(split[0], split[1]));
        });
        chatParam.setHistory(history);
        return success(remoteModelService.chat(chatParam));
    }

}
