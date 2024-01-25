package com.digitalemployee.business.modules.chat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/de/api")
@RequiredArgsConstructor
public class ChatApiController {

    private final IBizDigitalEmployeeService digitalEmployeeService;

    private final BizChatService bizChatService;

    @CrossOrigin
    @PostMapping("/chat")
    @Anonymous
    public AjaxResult chat(@RequestBody BizChatRequest param, HttpServletRequest request, HttpServletResponse response) {
        String apiKey = param.getApiKey();
        String errMsg = "请输入正确的 apiKey";
        if (StrUtil.isEmpty(apiKey)) {
            throw new BaseException(errMsg);
        }

        LambdaQueryWrapper<BizDigitalEmployee> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizDigitalEmployee::getEmployeeKey, apiKey);
        List<BizDigitalEmployee> list = digitalEmployeeService.list(wrapper);
        if (CollUtil.isEmpty(list) || list.size() > 1) {
            throw new BaseException(errMsg);
        }
        BizDigitalEmployee digitalEmployee = list.get(0);
        param.setDigitalEmployeeId(digitalEmployee.getId());

        ChatDataDTO chatData = bizChatService.initChatData(param, null, request, response);
        return AjaxResult.success(bizChatService.chat(chatData));
    }

}
