package com.digitalemployee.business.modules.chat.service;

import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;
import com.digitalemployee.business.modules.chatsession.domain.BizSession;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BizChatService {

    BizSessionRecord chat(ChatDataDTO chatData);

    ChatDataDTO initChatData(BizChatRequest chatRequest, Long loginUserId, HttpServletRequest request, HttpServletResponse response);

    List<BizSessionRecord> anonymousHistory(BizChatRequest param, Long loginUserId, HttpServletRequest request, HttpServletResponse response);
}
