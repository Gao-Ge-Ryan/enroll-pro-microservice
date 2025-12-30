package top.gaogle.gateway.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.gateway.service.ValidateCodeService;

import java.util.Map;

/**
 * 验证码获取
 *
 * @author gaogle
 */
@Component
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {
    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        I18nResult<Map<String, Object>> result ;
        try {
            result = validateCodeService.createCaptcha();
        } catch (Exception e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(result));
    }
}
