package com.digitalemployee.business.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digitalemployee.business.constants.ChatConstants;
import com.digitalemployee.business.domain.BizUser;
import com.digitalemployee.business.mapper.BizUserMapper;
import com.digitalemployee.business.modules.chat.domain.ChatLoginBody;
import com.digitalemployee.business.modules.chat.service.ChatLoginService;
import com.digitalemployee.common.constant.CacheConstants;
import com.digitalemployee.common.constant.UserConstants;
import com.digitalemployee.common.core.domain.entity.SysUser;
import com.digitalemployee.common.core.redis.RedisCache;
import com.digitalemployee.common.exception.ServiceException;
import com.digitalemployee.common.exception.user.*;
import com.digitalemployee.common.utils.SecurityUtils;
import com.digitalemployee.common.utils.StringUtils;
import com.digitalemployee.common.utils.ip.IpUtils;
import com.digitalemployee.common.utils.uuid.IdUtils;
import com.digitalemployee.system.mapper.SysUserMapper;
import com.digitalemployee.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatLoginServiceImpl implements ChatLoginService {

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    private final ISysConfigService configService;

    private final RedisCache redisCache;

    private final BizUserMapper bizUserMapper;

    private final SysUserMapper sysUserMapper;

    /**
     * 聊天用户登录
     *
     * @param chatLoginBody ChatLoginBody
     * @return token
     */
    @Override
    public String login(ChatLoginBody chatLoginBody) {
        // 校验验证码
        validateCaptcha(chatLoginBody);
        // 校验用户
        validateUser(chatLoginBody);

        // 校验系统用户
        String username = chatLoginBody.getUsername();
        SysUser sysUser = sysUserMapper.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        // 校验密码
        BizUser bizUser = this.selectBizUser(sysUser.getUserId(), chatLoginBody.getChatUserName());
        this.validate(bizUser, chatLoginBody.getPassword());

        return this.createToken(bizUser.getId());
    }

    private void validateCaptcha(ChatLoginBody chatLoginBody) {
        String uuid = chatLoginBody.getUuid();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                throw new CaptchaExpireException();
            }
            String code = chatLoginBody.getCode();
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }
    }

    private void validateUser(ChatLoginBody chatLoginBody) {
        String username = chatLoginBody.getUsername();
        String chatUserName = chatLoginBody.getChatUserName();
        String password = chatLoginBody.getPassword();
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(chatUserName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(username)) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {

            throw new RuntimeException("系统用户名长度错误");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {

            throw new RuntimeException("密码长度错误");
        }
        // 用户名不在指定范围内 错误
        if (chatUserName.length() < UserConstants.USERNAME_MIN_LENGTH
                || chatUserName.length() > UserConstants.USERNAME_MAX_LENGTH) {

            throw new RuntimeException("用户名长度错误");
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {

            throw new BlackListException();
        }
    }

    private BizUser selectBizUser(Long sysUserId, String chatUserName) {
        LambdaQueryWrapper<BizUser> bizUserWrapper = Wrappers.lambdaQuery();
        bizUserWrapper.eq(BizUser::getSysUserId, sysUserId).eq(BizUser::getName, chatUserName);
        return bizUserMapper.selectOne(bizUserWrapper);
    }

    private void validate(BizUser bizUser, String rawPassword) {
        if (bizUser == null) {
            throw new RuntimeException("服务对象不存在");
        }

        Long sysUserId = bizUser.getSysUserId();
        String name = bizUser.getName();
        String key = sysUserId + "-" + name;

        Integer retryCount = redisCache.getCacheObject(getRetryCacheKey(key));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!SecurityUtils.matchesPassword(rawPassword, bizUser.getPassword())) {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getRetryCacheKey(key), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        } else {
            if (redisCache.hasKey(getRetryCacheKey(key))) {
                redisCache.deleteObject(getRetryCacheKey(key));
            }
        }
    }

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param key 用户名
     * @return 缓存键key
     */
    private String getRetryCacheKey(String key) {
        return ChatConstants.CHAT_PWD_ERR_CNT_KEY + key;
    }

    private String createToken(Long userId) {
        String token = IdUtils.fastUUID();
        refreshToken(token, userId);
        return token;
    }

    /**
     * 刷新令牌有效期
     *
     * @param token token
     */
    private void refreshToken(String token, Long userId) {
        // 根据uuid将loginUser缓存
        String userKey = ChatConstants.CHAT_LOGIN_TOKEN_KEY + token;
        redisCache.setCacheObject(userKey, userId.intValue(), ChatConstants.TIMEOUT_THIRTY_DAYS_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public String generateAppToken(Long userId) {
        LambdaQueryWrapper<BizUser> bizUserWrapper = Wrappers.lambdaQuery();
        bizUserWrapper.eq(BizUser::getSysUserId, userId).eq(BizUser::getStatus, 1).last("limit 0, 1");
        BizUser bizUser = bizUserMapper.selectOne(bizUserWrapper);
        if (bizUser == null || bizUser.getId() == null) {
            throw new RuntimeException("当前用户没有启用中的服务对象");
        }
        return createToken(bizUser.getId());
    }
}
