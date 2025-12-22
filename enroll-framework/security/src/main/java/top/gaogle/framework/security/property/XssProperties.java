package top.gaogle.framework.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "xss")
public class XssProperties {
    /**
     * 是否启用 XSS 过滤
     */
    private boolean enabled = true;

    /**
     * 需要排除 XSS 过滤的 URL 路径列表（支持 Ant 风格路径匹配，如 /api/**, /static/* 等）
     */
    private List<String> excludes;

    /**
     * 需要应用 XSS 过滤的 URL 模式（可选，若未指定则默认为 /*）
     */
    private List<String> urlPatterns;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

}
