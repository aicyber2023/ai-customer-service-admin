package com.digitalemployee.business.modules.chat.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.AiChatResponse;
import com.digitalemployee.business.api.domain.ChatParam;
import com.digitalemployee.business.api.domain.QAParam;
import com.digitalemployee.business.api.domain.QAResponse;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chat.service.BizChatService;
import com.digitalemployee.business.modules.chatsession.domain.BizSession;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import com.digitalemployee.business.modules.chatsession.service.IBizSessionRecordService;
import com.digitalemployee.business.modules.chatsession.service.IBizSessionService;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import com.digitalemployee.business.modules.de.domain.BizDigitalEmployeeProcedure;
import com.digitalemployee.business.modules.de.service.IBizDigitalEmployeeProcedureService;
import com.digitalemployee.business.service.IBizDigitalEmployeeService;
import com.digitalemployee.business.service.IBizKnowledgeBaseService;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BizChatServiceImpl implements BizChatService {

    private final IBizDigitalEmployeeService digitalEmployeeService;
    private final IBizKnowledgeBaseService knowledgeBaseService;
    private final IBizDigitalEmployeeProcedureService procedureService;

    // session module
    private final IBizSessionService sessionService;
    private final IBizSessionRecordService sessionRecordService;

    private final RemoteModelService remoteModelService;

    private final ChatResourcesConfig chatResourcesConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizSessionRecord chat(ChatDataDTO chatData) {
        this.chatAndSaveRecord(chatData);
        return chatData.getNewRecord();
    }

    @Override
    public ChatDataDTO initChatData(BizChatRequest chatRequest, Long loginUserId, HttpServletRequest request, HttpServletResponse response) {
        Long digitalEmployeeId = chatRequest.getDigitalEmployeeId();
        if (digitalEmployeeId == null || chatRequest.getUserInput() == null) {
            throw new RuntimeException("对话请求参数缺失");
        }
        // 初始化 ChatDataDTO
        ChatDataDTO chatData = new ChatDataDTO();
        chatData.setUserInput(chatRequest.getUserInput());
        chatData.setLoginUserId(loginUserId);
        chatData.initCookieToken();
        // 数字员工
        BizDigitalEmployee de = this.getDigitalEmployee(digitalEmployeeId);
        chatData.setDigitalEmployee(de);
        // 知识库
        BizKnowledgeBase knowledgeBase = this.getKnowledgeBase(de.getKnowledgeBaseId());
        chatData.setKnowledgeBase(knowledgeBase);
        // ip
        chatData.setClientIp(ServletUtil.getClientIP(request));
        // cookie token
        chatData.setClientToken(this.getTokenHeader(chatData.getCookieToken(), chatData.getTokenMapKey(), request, response));
        // procedure
        if (chatData.useProcedure()) {
            chatData.setProcedure(this.getProcedure(digitalEmployeeId));
        }
        // session and recordList
        this.getOrInitSession(chatData);
        // history
        chatData.setRecordList(this.getHistoryList(chatData));
        // init new record
        chatData.setNewRecord(this.initRecord(chatData));
        return chatData;
    }

    /**
     * 数字员工
     */
    private BizDigitalEmployee getDigitalEmployee(Long digitalEmployeeId) {
        BizDigitalEmployee de = digitalEmployeeService.getById(digitalEmployeeId);
        if (de == null) {
            throw new BaseException("未查询到可用的数字客服");
        }
        Long knowledgeBaseId = de.getKnowledgeBaseId();
        if (knowledgeBaseId == null) {
            throw new BaseException("未查询到可用的知识库");
        }
        return de;
    }

    /**
     * 知识库
     */
    private BizKnowledgeBase getKnowledgeBase(Long knowledgeBaseId) {
        BizKnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null || knowledgeBase.getCollectionName() == null || knowledgeBase.getCollectionNameQa() == null) {
            throw new BaseException("未查询到可用的知识库");
        }
        return knowledgeBase;
    }

    private String getTokenHeader(String headerName, String mapKey, HttpServletRequest request, HttpServletResponse response) {
        String headerValue = ServletUtil.getHeader(request, headerName, StandardCharsets.UTF_8);
        Map<String, String> headerMap = ServletUtil.getHeaderMap(request);
        log.info("Header_Map: {}", headerMap);
        log.info("Header: {}, Map_Key: {}, Header_Value: {}", headerName, mapKey, headerValue);
        if (headerValue == null) {
            Map<String, String> tokenMap = new HashMap<>();
            return this.initTokenHeader(tokenMap, mapKey, headerName, response);
        }

        String jsonString = new String(Base64.decode(headerValue.getBytes(StandardCharsets.UTF_8)));
        Map<String, String> bean = JSONUtil.toBean(jsonString, new TypeReference<HashMap<String, String>>() {}, true);
        log.info("Existed_Token_Map: {}", bean);
        if (!bean.containsKey(mapKey)) {
            return initTokenHeader(bean, mapKey, headerName, response);
        }

        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, headerName);
        response.setHeader(headerName, headerValue);
        return bean.get(mapKey);
    }

    private String initTokenHeader(Map<String, String> tokenMap, String mapKey, String headerName, HttpServletResponse response) {
        String value = IdUtil.fastSimpleUUID();
        tokenMap.put(mapKey, value);
        log.info("NEW_Token_Map: {}", tokenMap);
        String encodedToken = Base64.encode(JSONUtil.toJsonStr(tokenMap).getBytes(StandardCharsets.UTF_8));
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, headerName);
        response.setHeader(headerName, encodedToken);
        return value;
    }

    private String getProcedure(Long digitalEmployeeId) {
        try {
            BizDigitalEmployeeProcedure procedure = procedureService.selectProcedureByDeId(digitalEmployeeId);
            return procedure == null ? chatResourcesConfig.getDefaultProcedure() : procedure.getContent();
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    private void getOrInitSession(ChatDataDTO chatData) {
        LambdaQueryWrapper<BizSession> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizSession::getDigitalEmployeeId, chatData.getDigitalEmployee().getId())
                .eq(BizSession::getIp, chatData.getClientIp())
                .eq(BizSession::getCookie, chatData.getClientToken())
                .eq(chatData.isTest(), BizSession::getTestUserId, chatData.getLoginUserId());
        List<BizSession> sessionList = sessionService.list(wrapper);
        if (sessionList.size() > 1) {
            throw new BaseException("会话数据异常，请联系管理员");
        }
        if (sessionList.isEmpty()) {
            chatData.initSession();
            sessionService.insertBizSession(chatData.getSession());
        } else {
            chatData.setSession(sessionList.get(0));
        }
    }

    private List<BizSessionRecord> getHistoryList(ChatDataDTO chatData) {
        int historyAmount = chatData.getHistoryAmount();
        if (historyAmount > 0) {
            LambdaQueryWrapper<BizSessionRecord> sessionRecordWrapper = Wrappers.lambdaQuery();
            sessionRecordWrapper
                    .eq(BizSessionRecord::getSessionId, chatData.getSession().getId())
                    .orderByDesc(BizSessionRecord::getSendTime)
                    .last("LIMIT 0, " + historyAmount);
            List<BizSessionRecord> recordList = sessionRecordService.list(sessionRecordWrapper);
            return CollUtil.reverse(recordList);
        }
        return new ArrayList<>();
    }

    private void chatAndSaveRecord(ChatDataDTO chatData) {
        long start = System.currentTimeMillis();
        switch (chatData.getChatType()) {
            case 1:
                // 1 - 文档
                this.doc(chatData);
                break;
            case 2:
                // 2- 问答 + 文档
                this.qaAndDoc(chatData);
                break;
            default:
                // 0 或者 其它值 - 问答
                this.qa(chatData);
                break;
        }

        BizSessionRecord record = chatData.getNewRecord();

        record.setReturnTime(new Date());
        long responseInterval = System.currentTimeMillis() - start;
        record.setResponseInterval(responseInterval);

        record.setTokens(record.getOutputText().length());

        record.setAiResponse(JSONUtil.toJsonStr(chatData.getAiReq()));

        sessionRecordService.save(record);
        BizSession session = chatData.getSession();
        session.setRecordAmount(session.getRecordAmount() + 1);
        session.setUpdateTime(new Date());
        sessionService.updateById(session);
    }

    private BizSessionRecord initRecord(ChatDataDTO chatData) {
        BizSessionRecord record = new BizSessionRecord();
        record.setSessionId(chatData.getSession().getId());
        record.setIp(chatData.getClientIp());
        record.setDigitalEmployeeId(chatData.getDigitalEmployee().getId());
        record.setSendTime(new Date());
        record.setInputText(chatData.getUserInput());
        record.setStatus(1);
        record.setChatType(chatData.getChatType());
        record.setErrorMessage("");
        return record;
    }

    private void qa(ChatDataDTO chatData) {
        BizSessionRecord record = chatData.getNewRecord();
        QAParam qaParam = chatData.initQaParam();
        QAResponse qaResponse = remoteModelService.qa(qaParam);
        chatData.putAiReq("qaParam", qaParam);
        chatData.putAiReq("qaResponse", qaResponse);
        if (qaResponse.getSuccessful() && this.isHighQuantityOfInformation(qaResponse, chatData.getDigitalEmployee())) {
            record.setHitStatus(0);
            record.setOutputText(qaResponse.getAnswer());
        } else {
            String msg = record.getErrorMessage() + "问答库错误信息: " + qaResponse.getMessage() + "...";
            record.setErrorMessage(msg);
            this.setProcedure(chatData);
        }
    }

    private void doc(ChatDataDTO chatData) {
        BizSessionRecord record = chatData.getNewRecord();
        QAParam searchTextParam = chatData.initSearchTextParam();
        QAResponse docResponse = remoteModelService.searchText(searchTextParam);
        chatData.putAiReq("docParam", searchTextParam);
        chatData.putAiReq("docResponse", docResponse);
        if (docResponse.getSuccessful() && this.isHighQuantityOfInformation(docResponse, chatData.getDigitalEmployee())) {
            record.setHitStatus(1);
            record.setOutputText(docResponse.getAnswer());
        } else {
            String msg = record.getErrorMessage() + "文档库错误信息: " + docResponse.getMessage() + "...";
            record.setErrorMessage(msg);
            this.setProcedure(chatData);
        }
    }

    private void qaAndDoc(ChatDataDTO chatData) {
        BizSessionRecord record = chatData.getNewRecord();
        QAParam qaParam = chatData.initQaParam();
        QAResponse qaResponse = remoteModelService.qa(qaParam);
        chatData.putAiReq("qaParam", qaParam);
        chatData.putAiReq("qaResponse", qaResponse);
        if (qaResponse.getSuccessful() && this.isHighQuantityOfInformation(qaResponse, chatData.getDigitalEmployee())) {
            record.setHitStatus(0);
            record.setOutputText(qaResponse.getAnswer());
        } else {
            String msg = record.getErrorMessage() + "问答库错误信息: " + qaResponse.getMessage() + "...";
            record.setErrorMessage(msg);
            this.doc(chatData);
        }
    }

    private boolean isHighQuantityOfInformation(QAResponse response, BizDigitalEmployee digitalEmployee) {
        Double quantityOfInformation = response.getQuantity_of_information();
        BigDecimal userConfigQuantity = digitalEmployee.getQuantityOfInformation();
        if (quantityOfInformation == null || userConfigQuantity == null) {
            // 二者之一为空时，返回true
            return true;
        }
        return quantityOfInformation >= userConfigQuantity.doubleValue();
    }

    private void setProcedure(ChatDataDTO chatData) {
        BizSessionRecord record = chatData.getNewRecord();
        record.setHitStatus(2);
        if (chatData.useProcedure()) {
            record.setOutputText(chatData.getProcedure());
        } else {
            ChatParam chatParam = chatData.initChatParam();
            AiChatResponse aiChatResponse = remoteModelService.chat(chatParam);
            chatData.putAiReq("chatParam", chatParam);
            chatData.putAiReq("chatResponse", aiChatResponse);
            if (!aiChatResponse.getSuccessful()) {
                String msg = record.getErrorMessage() + "大模型错误信息: " + aiChatResponse.getMessage() + "...";
                record.setErrorMessage(msg);
                record.setOutputText(chatData.getProcedure());
            }
            record.setOutputText(aiChatResponse.getOutput());
        }

    }

    @Override
    public List<BizSessionRecord> anonymousHistory(BizChatRequest param, Long loginUserId, HttpServletRequest request, HttpServletResponse response) {
        Long digitalEmployeeId = param.getDigitalEmployeeId();
        if (digitalEmployeeId == null) {
            return new ArrayList<>();
        }

        String clientIP = ServletUtil.getClientIP(request);
        Cookie cookie = ServletUtil.getCookie(request, ChatDataDTO.CHAT_USER_TOKEN);
        if (cookie == null) {
            return new ArrayList<>();
        }
        String clientToken = cookie.getValue();

        LambdaQueryWrapper<BizSession> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BizSession::getDigitalEmployeeId, digitalEmployeeId)
                .eq(BizSession::getIp, clientIP)
                .eq(BizSession::getCookie, clientToken)
                .eq(loginUserId != null, BizSession::getTestUserId, loginUserId);
        List<BizSession> sessionList = sessionService.list(wrapper);
        if (sessionList.size() > 1) {
            throw new BaseException("会话数据异常，请联系管理员");
        }
        if (sessionList.isEmpty()) {
            return new ArrayList<>();
        }

        BizSession session = sessionList.get(0);
        LambdaQueryWrapper<BizSessionRecord> sessionRecordWrapper = Wrappers.lambdaQuery();
        sessionRecordWrapper
                .eq(BizSessionRecord::getSessionId, session.getId())
                .orderByDesc(BizSessionRecord::getSendTime);
        return CollUtil.reverse(sessionRecordService.list(sessionRecordWrapper));
    }

}
