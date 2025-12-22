package top.gaogle.framework.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SecurityHeadersFilter
 *
 * @author gaogle
 * @since 1.0.0
 */
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 防止点击劫持
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // 防止MIME类型嗅探
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // 防XSS（旧浏览器）
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // 防止Referrer泄露
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // 限制第三方资源使用
        httpResponse.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");

        chain.doFilter(request, response);
    }

}