package top.gaogle.gateway.filter;


import com.auth0.jwt.interfaces.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.gaogle.framework.commons.common.CacheConstants;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.enums.HttpStatusEnum;
import top.gaogle.framework.commons.util.ServletUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.redis.service.StringRedisService;
import top.gaogle.gateway.config.IgnoreWhiteProperties;
import top.gaogle.gateway.util.Auth0TokenUtil;

import java.util.Map;

/**
 * 网关鉴权
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);


    private final IgnoreWhiteProperties ignoreWhite;


    private final StringRedisService stringRedisService;

    @Autowired
    public AuthFilter(IgnoreWhiteProperties ignoreWhite, StringRedisService stringRedisService) {
        this.ignoreWhite = ignoreWhite;
        this.stringRedisService = stringRedisService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String url = request.getURI().getPath();
        // 🔹 打印原始请求头（用户传入的）
        log.info("=== 原始请求头（处理前） ===");
        request.getHeaders().forEach((key, values) -> {
            log.info("Header: {} = {}", key, values);
        });
        // 跳过不需要验证的路径
        if (StringUtil.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtil.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Map<String, Claim> claims = Auth0TokenUtil.getClaims(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userKey = Auth0TokenUtil.getUserKey(claims);
        boolean isLogin = stringRedisService.hasKey(getTokenKey(userKey));
        if (!isLogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userId = Auth0TokenUtil.getUserId(claims);
        String username = Auth0TokenUtil.getUserName(claims);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }
        // 内部请求参数清除
        mutate.headers(httpHeaders -> {
            httpHeaders.remove(SecurityConstants.FROM_SOURCE);
            httpHeaders.remove(SecurityConstants.USER_KEY);
            httpHeaders.remove(SecurityConstants.DETAILS_USER_ID);
            httpHeaders.remove(SecurityConstants.DETAILS_USERNAME);
        });

        ServerHttpRequest newRequest = mutate.build();
        log.info("=== 修改后的请求头（处理中） ===");
        newRequest.getHeaders().forEach((key, values) -> {
            log.info("Header: {} = {}", key, values);
        });

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userKey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userId);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);

        log.info("=== 修改后的请求头（处理后） ===");
        newRequest.getHeaders().forEach((key, values) -> {
            log.info("Header: {} = {}", key, values);
        });

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = StringUtil.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{},错误信息:{}", exchange.getRequest().getPath(), msg);
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), HttpStatusEnum.UNAUTHORIZED, msg);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        return request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HEADER);
    }

    @Override
    public int getOrder() {
        return -200;
    }
}