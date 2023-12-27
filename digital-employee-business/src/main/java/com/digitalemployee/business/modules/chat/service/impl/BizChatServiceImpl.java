package com.digitalemployee.business.modules.chat.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.QAParam;
import com.digitalemployee.business.api.domain.QAResponse;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;
import com.digitalemployee.business.modules.de.service.IBizDigitalEmployeeProcedureService;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;

import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class BizChatServiceImpl implements BizChatService {

    private final IBizDigitalEmployeeService digitalEmployeeService;
    private final IBizKnowledgeBaseService knowledgeBaseService;
    private final IBizDigitalEmployeeProcedureService procedureService;

    // TODO: 2023/12/26 session module
    // private final IBizSessionRecordService sessionRecordService;

    private final RemoteModelService remoteModelService;

    @Override
    public Object xxx(BizChatRequest chatRequest, HttpServletRequest request, HttpServletResponse response) {

        ChatDataDTO chatData = this.initChatData(chatRequest, request, response);

        QAResponse qaResponse = this.chat(chatData);

        // TODO: 2023/12/26 更新聊天记录

        return qaResponse.getMessage();
    }

    private ChatDataDTO initChatData(BizChatRequest chatRequest, HttpServletRequest request, HttpServletResponse response) {
        if (chatRequest.getDigitalEmployeeId() == null || chatRequest.getUserInput() == null) {
            throw new RuntimeException("对话请求参数缺失");
        }

        // 初始化 ChatDataDTO
        ChatDataDTO chatData = new ChatDataDTO();
        chatData.setUserInput(chatRequest.getUserInput());

        // 数字员工
        BizDigitalEmployee de = digitalEmployeeService.getById(chatRequest.getDigitalEmployeeId());
        if (de == null) {
            throw new BaseException("未查询到可用的数字客服");
        }
        Long knowledgeBaseId = de.getKnowledgeBaseId();
        if (knowledgeBaseId == null) {
            throw new BaseException("未查询到可用的知识库");
        }
        chatData.setDigitalEmployee(de);

        // 知识库
        BizKnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null || knowledgeBase.getCollectionName() == null || knowledgeBase.getCollectionNameQa() == null) {
            throw new BaseException("未查询到可用的知识库");
        }
        chatData.setKnowledgeBase(knowledgeBase);

        // ip
        chatData.setClientAddr(ServletUtil.getClientIP(request));
        // cookie token
        String cookieName = "chat_user_token";
        Cookie cookie = ServletUtil.getCookie(request, cookieName);
        if (cookie == null) {
            cookie = new Cookie(cookieName, IdUtil.fastSimpleUUID());
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
        chatData.setClientToken(cookie.getValue());

        // procedure
        if (chatData.useProcedure()) {
            try {
                BizDigitalEmployeeProcedure procedure = procedureService.selectProcedureByDeId(chatRequest.getDigitalEmployeeId());
                chatData.setProcedure(procedure.getContent());
            } catch (Exception e) {
                throw new BaseException(e.getMessage());
            }
        }

        // recordList
        if (chatData.getHistoryAmount() > 0) {
            // TODO: 2023/12/26 session 字段调整
        }

        return chatData;
    }

    private QAResponse chat(ChatDataDTO chatData) {
        QAParam qaParam = chatData.initQaParam();
        return remoteModelService.qa(qaParam);

        // TODO: 2023/12/26 根据 chatType 处理调用逻辑

        // TODO: 2023/12/26 等待代伟的新模型接口

    }



}
