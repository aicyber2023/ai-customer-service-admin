package com.digitalemployee.business.modules.chat.domain;

import com.digitalemployee.common.core.domain.model.LoginBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChatLoginBody extends LoginBody {

    private String chatUserName;

}
