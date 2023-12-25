package com.digitalemployee.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtils.class);

    private static final Map<Long, Session> jobs = new ConcurrentHashMap<>();

    public static void add(Long jobId, Session session) {
        jobs.put(jobId, session);
        logger.info("当前连接数 = " + jobs.size());
    }

    public static void remove(Long jobId) {
        if (jobs.containsKey(jobId)) {
            try(Session session = jobs.remove(jobId)) {
                logger.info("Close WebSocket Session: " + session.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("当前连接数 = " + jobs.size());
    }

    public static void sendText(Long jobId, String message) {
        if (jobs.containsKey(jobId)) {
            try {
                jobs.get(jobId).getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
