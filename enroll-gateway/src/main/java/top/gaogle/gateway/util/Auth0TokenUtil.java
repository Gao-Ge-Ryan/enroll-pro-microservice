package top.gaogle.gateway.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gaogle.framework.commons.common.CommonsConst;
import top.gaogle.framework.commons.common.SecurityConstants;

import java.util.Map;

/**
 * 生成令牌，验证等一些操作
 *
 * @author goge
 * @since 1.0.0
 */
public class Auth0TokenUtil {
    private static final Logger log = LoggerFactory.getLogger(Auth0TokenUtil.class);

    private Auth0TokenUtil() {
        throw new IllegalStateException(CommonsConst.PROHIBIT_INSTANTIATION);
    }
    private static final String AUTHENTICATION_SECRET = SecurityConstants.AUTHENTICATION_SECRET + System.getenv(SecurityConstants.AUTHENTICATION_SECRET);

    static {
        String envValue = System.getenv(SecurityConstants.AUTHENTICATION_SECRET);
        if (StringUtils.isEmpty(envValue)) {
            log.error("Environment variable {} not found or empty! AUTHENTICATION_SECRET =={}", SecurityConstants.AUTHENTICATION_SECRET, AUTHENTICATION_SECRET);
            throw new IllegalStateException("Startup aborted: Missing environment variable " + SecurityConstants.AUTHENTICATION_SECRET);
        }

    }


    public static String generateToken(Map<String, Object> claims) {
        return JWT.create().withIssuer(SecurityConstants.AUTHENTICATION_ISSUER).withPayload(claims).sign(Algorithm.HMAC512(AUTHENTICATION_SECRET));
    }

    public static Map<String, Claim> getClaims(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getClaims();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token) {
        Map<String, Claim> claims = getClaims(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Map<String, Claim> claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Map<String, Claim> claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserName(String token) {
        Map<String, Claim> claims = getClaims(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Map<String, Claim> claims) {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }


    public static DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC512(AUTHENTICATION_SECRET)).withIssuer(SecurityConstants.AUTHENTICATION_ISSUER).build().verify(token);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Map<String, Claim> claims, String key) {
        return claims.get(key).asString();
    }

}
