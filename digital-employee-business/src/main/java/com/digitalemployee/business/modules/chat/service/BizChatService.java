package com.digitalemployee.business.modules.chat.service;

import com.digitalemployee.business.modules.chat.domain.BizChatRequest;
import com.digitalemployee.business.modules.chat.domain.ChatDataDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BizChatService {

    Object chat(ChatDataDTO chatData);

    ChatDataDTO initChatData(BizChatRequest chatRequest, Long loginUserId, HttpServletRequest request, HttpServletResponse response);
}
