package top.gaogle.framework.security.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.gaogle.framework.security.interceptor.HeaderInterceptor;
import top.gaogle.framework.security.interceptor.SameUrlDataInterceptor;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * 拦截器配置
 *
 * @author gaogle
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 不需要拦截地址
     */
    public static final String[] excludeUrls = {"/auth/login"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrls)
                .order(-10);

        registry.addInterceptor(getSameUrlDataInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns()
                .order(LOWEST_PRECEDENCE - 1);
    }

    /**
     * 自定义请求头拦截器
     */
    public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }

    /**
     * 防止重复提交拦截器
     */
    public SameUrlDataInterceptor getSameUrlDataInterceptor() {
        return new SameUrlDataInterceptor();
    }


}
