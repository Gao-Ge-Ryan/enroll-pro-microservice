package top.gaogle.framework.security.filter;

import org.apache.commons.lang3.StringUtils;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.security.wrapper.XssWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XSS过滤器
 *
 * @author gaogle
 * @since 1.0.0
 */
public class XssFilter implements Filter {
    /**
     * 存储需要排除XSS过滤的URL模式列表。
     */
    private final List<String> excludes = new ArrayList<>();

    /**
     * 是否启用XSS过滤的标志。
     */
    private boolean enabled = false;

    /**
     * 初始化过滤器，从过滤器配置中读取排除列表和启用状态。
     *
     * @param filterConfig 过滤器配置对象。
     * @throws ServletException 如果初始化过程中出现错误。
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String strExcludes = filterConfig.getInitParameter("excludes");
        String strEnabled = filterConfig.getInitParameter("enabled");
        //将不需要xss过滤的接口添加到列表中
        if (StringUtils.isNotEmpty(strExcludes)) {
            String[] urls = strExcludes.split(",");
            excludes.addAll(Arrays.asList(urls));
        }
        if (StringUtils.isNotEmpty(strEnabled)) {
            enabled = Boolean.parseBoolean(strEnabled);
        }
    }

    /**
     * 执行过滤逻辑，如果当前请求不在排除列表中，则通过XSS过滤器包装请求。
     *
     * @param request  HTTP请求对象。
     * @param response HTTP响应对象。
     * @param chain    过滤器链对象，用于继续或中断请求处理。
     * @throws IOException      如果处理过程中出现I/O错误。
     * @throws ServletException 如果处理过程中出现Servlet相关错误。
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //如果该访问接口在排除列表里面则不拦截
        if (isExcludeUrl(req.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }
        // xss 过滤
        chain.doFilter(new XssWrapper(req), resp);
    }

    /**
     * 销毁过滤器，释放资源。
     */
    @Override
    public void destroy() {
        // 无需额外的销毁逻辑
    }

    /**
     * 判断当前请求的URL是否应该被排除在XSS过滤之外。
     *
     * @param urlPath 请求的 URL 路径。
     * @return 如果请求应该被排除，则返回 true；否则返回 false。
     */
    private boolean isExcludeUrl(String urlPath) {
        if (!enabled) {
            return true; // XSS 关闭，全部放行
        }
        if (excludes.isEmpty()) {
            return false; // 无排除项，不放行
        }

        // 使用 AntPathMatcher 判断 urlPath 是否匹配任意一个 exclude 模式
        return excludes.stream()
                .anyMatch(pattern -> StringUtil.isMatch(pattern, urlPath));
    }
}

