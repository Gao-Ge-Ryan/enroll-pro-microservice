package top.gaogle.framework.security.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import top.gaogle.framework.security.filter.XssFilter;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter配置
 *
 * @author gaogle
 * @since 1.0.0
 */
@EnableConfigurationProperties(XssProperties.class)
@Configuration
public class FilterConfig {

    private final XssProperties xssProperties;

    @Autowired
    public FilterConfig(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }


    /**
     * 配置字符编码过滤器，防止中文乱码和潜在的编码相关漏洞
     */
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> customCharacterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0); // 最高优先级之一
        return registration;
    }

    /**
     * 配置CSP、X-XSS-Protection等安全头
     */
    @Bean
    public FilterRegistrationBean<SecurityHeadersFilter> securityHeadersFilter() {
        FilterRegistrationBean<SecurityHeadersFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecurityHeadersFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    /**
     * 注册XSS过滤器。
     *
     * @return FilterRegistrationBean 用于注册过滤器的bean。
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());

        // 设置 URL patterns：若配置为空，默认为 "/*"
        List<String> urlPatterns = xssProperties.getUrlPatterns();
        if (CollectionUtils.isEmpty(urlPatterns)) {
            registration.addUrlPatterns("/*");
        } else {
            registration.addUrlPatterns(urlPatterns.toArray(new String[0]));
        }

        registration.setName("XssFilter");
        // 使用常量代替魔法数字（可选：定义在类中或配置中）
        registration.setOrder(Integer.MAX_VALUE - 1); // 接近最低优先级，但保留一点空间

        // 初始化参数
        Map<String, String> initParameters = new HashMap<>();
        List<String> excludes = xssProperties.getExcludes();
        initParameters.put("excludes", CollectionUtils.isNotEmpty(excludes) ? String.join(",", excludes) : "");
        initParameters.put("enabled", String.valueOf(xssProperties.isEnabled()));

        registration.setInitParameters(initParameters);
        return registration;
    }


}
