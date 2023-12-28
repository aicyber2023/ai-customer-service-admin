package com.digitalemployee.business.api.domain;

import com.digitalemployee.business.modules.chatsession.domain.BizSessionRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatParam extends BaseModelParam{

    private String user_input;
    private List<ChatHistory> history;

    public ChatParam() {
    }

    public ChatParam(String user_input, BigDecimal temperature, BigDecimal presence_penalty, BigDecimal frequency_penalty, List<BizSessionRecord> sessionRecords) {
        super(temperature, presence_penalty, frequency_penalty);
        this.user_input = user_input;
        this.history = new ArrayList<>();
        appendHistoryList(sessionRecords);
    }

    public void appendHistory(BizSessionRecord record) {
        history.add(new ChatHistory(record.getInputText(), record.getOutputText()));
    }

    public void appendHistoryList(List<BizSessionRecord> sessionRecords) {
        sessionRecords.forEach(this::appendHistory);
    }

}
