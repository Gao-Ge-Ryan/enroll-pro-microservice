package top.gaogle.framework.security.service;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.common.CacheConstants;
import top.gaogle.framework.commons.util.DateUtil;
import top.gaogle.framework.commons.util.JsonUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.redis.service.RedisStringService;
import top.gaogle.framework.security.pojo.LoginUser;

import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);


    private final RedisStringService redisStringService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long TOKEN_EXPIRE_TIME = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long TOKEN_REFRESH_THRESHOLD_MINUTES = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    @Autowired
    public TokenService(RedisStringService redisStringService) {
        this.redisStringService = redisStringService;
    }


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String userKey) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(userKey)) {
                String userJson = redisStringService.getCacheObject(getTokenKey(userKey));
                return JsonUtil.json2Object(userJson, LoginUser.class);
            }
        } catch (Exception e) {
            log.error("获取用户信息异常'{}'", e.getMessage());
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtil.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String userKey) {
        if (StringUtils.isNotEmpty(userKey)) {
            redisStringService.deleteObject(getTokenKey(userKey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= TOKEN_REFRESH_THRESHOLD_MINUTES) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(DateUtil.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + TOKEN_EXPIRE_TIME * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisStringService.setCacheObject(userKey, JsonUtil.object2Json(loginUser), TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}