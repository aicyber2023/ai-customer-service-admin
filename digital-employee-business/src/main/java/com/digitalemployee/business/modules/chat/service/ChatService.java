package com.digitalemployee.business.modules.chat.service;

import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.modules.chat.domain.ChatLoginBody;
import com.digitalemployee.business.modules.chat.domain.DigitalEmployee;

import java.util.List;

public interface ChatService {

    BizUser getUserInfo(Long userId);

    int modifyUser(BizUser user);

    BizUser selectUserById(Long userId);

    List<DigitalEmployee> selectUserDe(Long userId);
}
