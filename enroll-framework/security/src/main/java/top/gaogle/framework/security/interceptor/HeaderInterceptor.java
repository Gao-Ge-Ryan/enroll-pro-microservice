package top.gaogle.framework.security.interceptor;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.util.ServletUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.security.auth.AuthUtil;
import top.gaogle.framework.security.context.SecurityContextHolder;
import top.gaogle.framework.security.pojo.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author gaogle
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String userKey = ServletUtil.getHeader(request, SecurityConstants.USER_KEY);
        String userId = ServletUtil.getHeader(request, SecurityConstants.DETAILS_USER_ID);
        String username = ServletUtil.getHeader(request, SecurityConstants.DETAILS_USERNAME);
        SecurityContextHolder.setUserKey(userKey);
        SecurityContextHolder.setUserId(userId);
        SecurityContextHolder.setUserName(username);

        if (StringUtils.isNotEmpty(userKey)) {
            LoginUser loginUser = AuthUtil.getLoginUser(userKey);
            if (StringUtil.isNotNull(loginUser)) {
                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
