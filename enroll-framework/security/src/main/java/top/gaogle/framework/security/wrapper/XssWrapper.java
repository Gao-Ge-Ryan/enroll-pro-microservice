package top.gaogle.framework.security.wrapper;


import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.commons.util.XssUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS请求包装类
 *
 * @author gaogle
 * @since 1.0.0
 */
public class XssWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 对数组参数进行特殊字符过滤
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    /**
     * 对参数中特殊字符进行过滤
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtil.isBlank(value)) {
            return value;
        }
        return cleanXSS(value);
    }

    /**
     * 获取attribute,特殊字符过滤
     */
    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (value instanceof String && StringUtil.isNotBlank((String) value)) {
            return cleanXSS((String) value);
        }
        return value;
    }

    /**
     * 对请求头部进行特殊字符过滤
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StringUtil.isBlank(value)) {
            return value;
        }
        return cleanXSS(value);
    }

    /**
     * 清理输入的字符串以防止XSS攻击
     *
     * @param value 待清理的字符串，通常为用户输入或来自不可信源的数据。
     * @return 清理后的字符串，移除了可能的XSS攻击代码。
     */
    private String cleanXSS(String value) {
        return XssUtil.clean(value);
    }
}

