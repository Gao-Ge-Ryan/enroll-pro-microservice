package top.gaogle.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gaogle.framework.commons.common.SecurityConstants;

import java.util.Map;

public class JWTUtil {
    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);
    public static String generateToken(Map<String, Object> claims) {
        return JWT.create().withIssuer(SecurityConstants.AUTHENTICATION_ISSUER).withPayload(claims).sign(Algorithm.HMAC512(SecurityConstants.AUTHENTICATION_SECRET));
    }
}
