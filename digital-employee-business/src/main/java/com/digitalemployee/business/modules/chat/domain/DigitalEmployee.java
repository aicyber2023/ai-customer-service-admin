package com.digitalemployee.business.modules.chat.domain;

import com.digitalemployee.business.domain.BizDigitalEmployee;
import com.digitalemployee.business.modules.chat.service.impl.ModelConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DigitalEmployee extends BizDigitalEmployee {

    private ModelConfig modelConfig;

    public void initModelConfig() {
        modelConfig = new ModelConfig();
        this.modelConfig.setModel(this.getModel());
        this.modelConfig.setTemperature(this.getTemperature());
        this.modelConfig.setMaxTokens(this.getMaxTokens());
        this.modelConfig.setPresencePenalty(this.getPresencePenalty());
        this.modelConfig.setFrequencyPenalty(this.getFrequencyPenalty());
        this.modelConfig.setSendMemory(this.getSendMemory());
        this.modelConfig.setCompressMessageLengthThreshold(this.getCompressMessageLengthThreshold());
        this.modelConfig.setHistoryMessageCount(this.getHistoryMessageCount());
    }

}
