package top.gaogle.framework.security.util;


import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.context.SecurityContextHolder;
import top.gaogle.framework.commons.util.ServletUtil;
import top.gaogle.framework.security.pojo.LoginUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 权限获取工具类
 *
 * @author gaogle
 */
public class SecurityUtil {
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername() {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(Objects.requireNonNull(ServletUtil.getRequest()));
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        return request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
    }


    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(SecurityConstants.AUTHENTICATION_SECRET);
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(SecurityConstants.AUTHENTICATION_SECRET);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
