package com.digitalemployee.common.config;

import feign.Logger;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder feignDecoder() {
        MyMappingJackson2HttpMessageConverter converter = new MyMappingJackson2HttpMessageConverter();
        return new SpringDecoder(() -> new HttpMessageConverters(converter));
    }

}
