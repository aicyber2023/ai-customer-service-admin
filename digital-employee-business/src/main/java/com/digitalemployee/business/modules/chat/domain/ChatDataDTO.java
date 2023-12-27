package com.digitalemployee.business.modules.chat.domain;

import com.digitalemployee.business.api.domain.QAParam;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import lombok.Data;

import java.util.List;

@Data
public class ChatDataDTO {

    /**
     * 数字员工
     */
    private BizDigitalEmployee digitalEmployee;

    /**
     * 知识库
     */
    private BizKnowledgeBase knowledgeBase;

    /**
     * 客户端ip
     */
    private String clientAddr;

    /**
     * 客户端cookie_token
     */
    private String clientToken;

    /**
     * 客户端用户输入
     */
    private String userInput;

    /**
     * 保底话术
     */
    private String procedure;

    /**
     * 对话历史
     */
    private List<BizSessionRecord> recordList;

    public QAParam initQaParam() {
        return new QAParam(knowledgeBase.getCollectionNameQa(),
                userInput,
                digitalEmployee.getQaRadius(),
                digitalEmployee.getTemperature(),
                digitalEmployee.getPresencePenalty(),
                digitalEmployee.getFrequencyPenalty()
        );
    }

    public boolean useProcedure() {
        return digitalEmployee.getModelSwitch() == null || digitalEmployee.getModelSwitch() == 0;
    }

    public int getHistoryAmount() {
        Long count = digitalEmployee.getHistoryMessageCount();
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    public int getChatType() {
        Integer chatType = digitalEmployee.getChatType();
        if (chatType == null) {
            return 0;
        }
        return chatType;
    }

}
