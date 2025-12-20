package top.gaogle.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import top.gaogle.framework.commons.enums.HttpStatusEnum;
import top.gaogle.framework.commons.util.ServletUtil;

/**
 * 自定义限流异常处理
 *
 * @author gaogle
 * @since 1.0.0
 */
public class SentinelFallbackHandler implements WebExceptionHandler {
    private Mono<Void> writeResponse(ServerResponse response, Throwable e, ServerWebExchange exchange) {
        String message = "系统开小差了，请稍后再试";
        if (e instanceof FlowException) {
            message = "当前系统繁忙，请稍后再试";
        } else if (e instanceof DegradeException) {
            message = "服务暂时不可用，请稍后重试哦";
        } else if (e instanceof ParamFlowException) {
            message = "请求过于频繁，请稍等片刻再试";
        } else if (e instanceof SystemBlockException) {
            message = "系统正在保护中，请稍后再试";
        } else if (e instanceof AuthorityException) {
            message = "您暂无权限访问该功能，请联系管理员";
        }
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), HttpStatusEnum.INTERNAL_SERVER_ERROR,
                "Gateway Sentinel: " + message);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(e);
        }
        if (!BlockException.isBlockException(e)) {
            return Mono.error(e);
        }

        return handleBlockedRequest(exchange, e).flatMap(response -> writeResponse(response, e, exchange));
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable throwable) {
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }
}
