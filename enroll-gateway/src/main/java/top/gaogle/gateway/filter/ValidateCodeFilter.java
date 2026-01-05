package top.gaogle.gateway.filter;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.enums.HttpStatusEnum;
import top.gaogle.framework.commons.util.ServletUtil;
import top.gaogle.gateway.property.CaptchaProperties;
import top.gaogle.gateway.service.ValidateCodeService;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 验证码过滤器
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
    private final static String[] VALIDATE_URL = new String[]{"/auth/login"};


    private final ValidateCodeService validateCodeService;

    private final CaptchaProperties captchaProperties;

    private static final String CODE = "code";

    private static final String UUID = "uuid";

    @Autowired
    public ValidateCodeFilter(ValidateCodeService validateCodeService, CaptchaProperties captchaProperties) {
        this.validateCodeService = validateCodeService;
        this.captchaProperties = captchaProperties;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 非登录/注册请求或验证码关闭，不处理
            if (!StringUtils.equalsAnyIgnoreCase(path, VALIDATE_URL) || !captchaProperties.getEnabled()) {
                return chain.filter(exchange);
            }

            try {
                AtomicReference<String> code = new AtomicReference<>();
                AtomicReference<String> uuid = new AtomicReference<>();
                request.getQueryParams()
                        .forEach((k, v) -> {
                            if (CODE.equals(k)) {
                                code.set(v.get(0));
                            }
                            if (UUID.equals(k)) {
                                uuid.set(v.get(0));
                            }
                        });
                validateCodeService.checkCaptcha(code.get(), uuid.get());
            } catch (Exception e) {
                return ServletUtil.webFluxResponseWriter(exchange.getResponse(), HttpStatusEnum.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            return chain.filter(exchange);
        };
    }
}
