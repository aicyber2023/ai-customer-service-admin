package com.digitalemployee.business.modules.chat.service;

import com.digitalemployee.business.modules.chat.domain.BizChatRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BizChatService {

    Object xxx(BizChatRequest chatRequest, HttpServletRequest request, HttpServletResponse response);
}
