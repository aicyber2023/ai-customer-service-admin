package com.digitalemployee.train.api;

import com.digitalemployee.train.api.domain.ChatRequest;
import com.digitalemployee.train.api.domain.ChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "remoteGptService", url = "https://api.wonderfulctu.com")
public interface RemoteLLMService {

    @PostMapping(value = "/v1/chat/completions", headers = {"Authorization=Bearer sk-VyYe4uTKc8v2uRXR9b136c9c3aBd4e27B572C0025a689447"})
    ChatResponse chat(ChatRequest chatRequest);

}
