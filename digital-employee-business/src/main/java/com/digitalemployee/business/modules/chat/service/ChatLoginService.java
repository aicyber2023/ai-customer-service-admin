package com.digitalemployee.business.modules.chat.service;

import com.digitalemployee.business.modules.chat.domain.ChatLoginBody;
import com.digitalemployee.common.core.domain.model.LoginUser;

public interface ChatLoginService {

    String login(ChatLoginBody chatLoginBody);

    String generateAppToken(Long userId);

}
