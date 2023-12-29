package com.digitalemployee.business.modules.chat.service;

import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BizChatService {

    Object chat(ChatDataDTO chatData);

    ChatDataDTO initChatData(BizChatRequest chatRequest, Long loginUserId, HttpServletRequest request, HttpServletResponse response);

    List<BizSessionRecord> xxx(BizChatRequest param, Long loginUserId, HttpServletRequest request, HttpServletResponse response);
}
