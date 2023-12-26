package com.digitalemployee.business.modules.chat.controller;


import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.QAParam;
import com.digitalemployee.business.api.domain.QAResponse;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.mapper.BizDigitalEmployeeMapper;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/de/bizChat")
@RequiredArgsConstructor
public class BizChatController {

    private final IBizDigitalEmployeeService digitalEmployeeService;
    private final IBizKnowledgeBaseService knowledgeBaseService;

    private final RemoteModelService remoteModelService;

    @Anonymous
    @PostMapping("/anonymousChat")
    public AjaxResult anonymousChat(@RequestBody Map<String, Object> param) {
        // 数字员工ID
        Long digitalEmployeeId = Long.valueOf((Integer) param.get("digitalEmployeeId"));
        // userInput
        String userInput = (String) param.get("userInput");

        BizDigitalEmployee de = digitalEmployeeService.getById(digitalEmployeeId);
        if (de == null) {
            throw new BaseException("未查询到可用的数字客服");
        }
        Long knowledgeBaseId = de.getKnowledgeBaseId();
        if (knowledgeBaseId == null) {
            throw new BaseException("未查询到可用的知识库");
        }
        BizKnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new BaseException("未查询到可用的知识库");
        }

        String kbCollectionName = knowledgeBase.getCollectionName();
        String qaCollectionName = knowledgeBase.getCollectionNameQa();
        if (qaCollectionName == null) {
            throw new BaseException("未查询到可用的问答库");
        }

        QAParam qaParam = new QAParam(qaCollectionName, userInput, new BigDecimal("0.7"), de.getTemperature(), de.getPresencePenalty(), de.getFrequencyPenalty());
        QAResponse qaResponse = remoteModelService.qa(qaParam);

        return AjaxResult.success(qaResponse.getAnswer());
    }

    @PostMapping("/chat")
    public AjaxResult chat() {
        //

        return AjaxResult.success();
    }


}
