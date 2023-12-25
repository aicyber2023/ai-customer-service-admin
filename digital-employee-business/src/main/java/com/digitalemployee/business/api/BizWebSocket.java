package com.digitalemployee.business.api;

import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value = "/bizWebSocket/{bizId}")
@Slf4j
public class BizWebSocket {

    @OnMessage
    public void onMessage(@PathParam("bizId") Long bizId, String message) {
        log.info("onMessage:{}, {}", bizId, message);
        WebSocketUtils.sendText(bizId, message);
    }

    @OnOpen
    public void onOpen(@PathParam("bizId") Long bizId, Session session, EndpointConfig config) {
        log.info("onOpen: {}", bizId);
        WebSocketUtils.add(bizId, session);
        log.info("已连接:" + bizId);
    }

    @OnClose
    public void onClose(@PathParam("bizId") Long bizId, Session session, CloseReason closeReason) {
        log.info("onClose: {}", bizId);
        WebSocketUtils.remove(bizId);
        log.info("已关闭: {}", bizId);
    }

    @OnError
    public void onError(Throwable error) {
        log.info("onError: {} ",error.getMessage());
    }

}
