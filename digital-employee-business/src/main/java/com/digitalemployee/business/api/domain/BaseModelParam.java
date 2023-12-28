package com.digitalemployee.business.api.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseModelParam {

    public static final double DEFAULT_TEMPERATURE = 2.0;
    public static final double DEFAULT_PRESENCE_PENALTY = 1.5;
    public static final double DEFAULT_FREQUENCY_PENALTY = 0.8;

    protected Double temperature;
    protected Double presence_penalty;
    protected Double frequency_penalty;

    public BaseModelParam() {
    }

    public BaseModelParam(BigDecimal temperature, BigDecimal presence_penalty, BigDecimal frequency_penalty) {
        this.temperature = temperature == null ? DEFAULT_TEMPERATURE : temperature.doubleValue();
        this.presence_penalty = presence_penalty == null ? DEFAULT_PRESENCE_PENALTY : presence_penalty.doubleValue();
        this.frequency_penalty = frequency_penalty == null ? DEFAULT_FREQUENCY_PENALTY : frequency_penalty.doubleValue();
    }

}
