package com.digitalemployee.business.aspectj;

import com.digitalemployee.business.constants.ChatConstants;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.mapper.BizUserMapper;
import com.digitalemployee.common.core.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ChatAuthAspect {

    public static final String CHAT_AUTH_HEADER = "Chat-Auth";

    private final BizUserMapper bizUserMapper;

    private final RedisCache redisCache;

    @Pointcut("@annotation(com.digitalemployee.common.annotation.ChatAuth)")
    public void chatAuthAop() {
    }

    @Around("chatAuthAop()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("请求异常，请联系管理员");
        }

        HttpServletRequest request = attributes.getRequest();
        // 验证请求
        this.validateHeader(request);

        // 从缓存中获取用户ID
        String chatAuth = request.getHeader(CHAT_AUTH_HEADER);
        Integer userId = redisCache.getCacheObject(ChatConstants.CHAT_LOGIN_TOKEN_KEY + chatAuth);

        // 验证用户
        this.validateUser(userId);

        // 刷新token缓存
        this.refreshToken(userId, chatAuth);

        Object[] args = joinPoint.getArgs();
        // 设置userId参数
        this.addUserIdParam(args, (long) userId);
        return joinPoint.proceed(args);
    }

    private void validateHeader(HttpServletRequest request) {
        // 没有 Chat-Auth 请求头时，抛出异常
        if (!hasHeader(request, CHAT_AUTH_HEADER)) {
            log.info("聊天未登录，异常IP: {}:{}", request.getRemoteHost(), request.getRemotePort());
            throw new RuntimeException("您没有权限，请联系管理员");
        }
    }

    private boolean hasHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName) != null;
    }

    private void validateUser(Integer userId) {
        // userId 为空时，提示登录过期
        if (userId == null) {
            throw new RuntimeException("登录已过期，请重新登录用户");
        }
        BizUser bizUser = bizUserMapper.selectById(userId);
        // 无用户
        if (bizUser == null) {
            throw new RuntimeException("没有找到指定用户，请联系管理员");
        }
    }

    private void refreshToken(Integer userId, String token) {
        String userKey = ChatConstants.CHAT_LOGIN_TOKEN_KEY + token;
        redisCache.setCacheObject(
                userKey, // chat_login_tokens:token
                userId,
                ChatConstants.TIMEOUT_THIRTY_DAYS_MINUTES, // 缓存时间：30天(分钟数)
                TimeUnit.MINUTES // 单位：分钟
        );
    }

    private void addUserIdParam(Object[] args, Long userId){
        // 接口的第一个参数设置为 biz_user 的 主键
        args[0] = userId;
    }

}
