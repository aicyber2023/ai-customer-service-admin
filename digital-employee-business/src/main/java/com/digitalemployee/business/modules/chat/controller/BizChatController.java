package com.digitalemployee.business.modules.chat.controller;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.controller.BaseController;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
        return success(bizChatService.xxx(param, null, request, response));
    }

    @PostMapping("/chatHistory")
    public void chatHistory(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {

    }

    @PostMapping("/test")
    @Anonymous
    public AjaxResult test(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) {
        String test = "test";
        String id = param.get("id");
        Cookie cookie = ServletUtil.getCookie(request, test);
        if (cookie == null) {
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put(id, IdUtil.fastSimpleUUID());
            String encodedToken = Base64.encode(JSONUtil.toJsonStr(tokenMap).getBytes(StandardCharsets.UTF_8));
            cookie = new Cookie(test, encodedToken);
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            return success(tokenMap);
        }

        String value = cookie.getValue();
        Map bean = JSONUtil.toBean(new String(Base64.decode(value.getBytes(StandardCharsets.UTF_8))), Map.class);

        if (!bean.containsKey(id)) {
            bean.put(id, IdUtil.fastSimpleUUID());
            String encodedToken = Base64.encode(JSONUtil.toJsonStr(bean).getBytes(StandardCharsets.UTF_8));
            cookie = new Cookie(test, encodedToken);
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }

        return success(bean);
    }

}
