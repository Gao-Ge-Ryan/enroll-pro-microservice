package top.gaogle.auth.controller;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.gaogle.auth.service.AuthService;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.log.annotation.Log;
import top.gaogle.pojo.entity.auth.AuthenticationPacket;

import java.util.Map;

/**
 * 认证
 *
 * @author gaogle
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录接口
     */
    @SentinelResource(entryType = EntryType.IN)
    @Log("登录接口")
    @PostMapping("/login")
    public I18nResult<Map<String, Object>> login(@RequestBody @Validated AuthenticationPacket authenticationPacket,
                                                 @RequestParam("ge") String ge) {
        return authService.login(authenticationPacket);
    }
}
