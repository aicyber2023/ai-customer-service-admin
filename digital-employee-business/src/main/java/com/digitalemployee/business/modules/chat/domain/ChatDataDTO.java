package com.digitalemployee.business.modules.chat.domain;

import com.digitalemployee.business.api.domain.ChatParam;
import com.digitalemployee.business.api.domain.QAParam;
import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.domain.BizKnowledgeBase;
import com.digitalemployee.business.modules.chatsession.domain.BizSession;
import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import lombok.Data;

import java.util.List;

@Data
public class ChatDataDTO {

    public static final String DEFAULT_PROCEDURE = "非常抱歉，我还没学到相关知识。";
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
    private String clientIp;

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
     * 登录人用户ID
     */
    private Long loginUserId;

    /**
     * 会话
     */
    private BizSession session;
    private BizSessionRecord newRecord;

    /**
     * 对话历史
     */
    private List<BizSessionRecord> recordList;

    /**
     * qa请求参数
     */
    public QAParam initQaParam() {
        return new QAParam(
                knowledgeBase.getCollectionNameQa(),
                userInput,
                digitalEmployee.getQaRadius(),
                digitalEmployee.getTemperature(),
                digitalEmployee.getPresencePenalty(),
                digitalEmployee.getFrequencyPenalty()
        );
    }

    /**
     * 文档请求参数
     */
    public QAParam initSearchTextParam() {
        return new QAParam(
                knowledgeBase.getCollectionName(),
                userInput,
                digitalEmployee.getKbRadius(),
                digitalEmployee.getTemperature(),
                digitalEmployee.getPresencePenalty(),
                digitalEmployee.getFrequencyPenalty()
        );
    }

    public ChatParam initChatParam() {
        return new ChatParam(
                userInput,
                digitalEmployee.getTemperature(),
                digitalEmployee.getPresencePenalty(),
                digitalEmployee.getFrequencyPenalty(),
                recordList
        );
    }

    public void initSession() {
        int sessionType = getSessionType();
        session = new BizSession();
        session.setDigitalEmployeeId(digitalEmployee.getId());
        session.setSessionType(sessionType);
        session.setTestUserId(loginUserId);
        session.setIp(clientIp);
        session.setCookie(clientToken);
        session.setRecordAmount(0);
        session.setUserId(digitalEmployee.getUserId());
        session.setDeptId(digitalEmployee.getDeptId());
    }

    /**
     * 使用兜底话术 或者 使用模型
     *
     * @return true 使用兜底话术 false 使用模型
     */
    public boolean useProcedure() {
        return digitalEmployee.getModelSwitch() == null || digitalEmployee.getModelSwitch() == 0;
    }

    /**
     * 返回携带历史消息数
     *
     * @return 历史消息数
     */
    public int getHistoryAmount() {
        Long count = digitalEmployee.getHistoryMessageCount();
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    /**
     * 返回对话类型
     *
     * @return 0-仅问答 1-仅知识库 2-问答+知识库
     */
    public int getChatType() {
        Integer chatType = digitalEmployee.getChatType();
        if (chatType == null) {
            return 0;
        }
        return chatType;
    }


    /**
     * 是否是测试
     *
     * @return true-是测试 false-非测试
     */
    public boolean isTest() {
        return loginUserId != null;
    }

    public int getSessionType() {
        if (session != null) {
            return session.getSessionType();
        }
        if (isTest()) {
            return 0;
        }
        return 1;
    }

    public String getProcedure() {
        return procedure == null ? DEFAULT_PROCEDURE : procedure;
    }


}
