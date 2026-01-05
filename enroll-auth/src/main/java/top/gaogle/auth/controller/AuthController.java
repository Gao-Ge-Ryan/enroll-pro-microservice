package top.gaogle.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gaogle.auth.service.AuthService;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.pojo.dto.auth.RegistryDTO;
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
    @PostMapping("/login")
    public I18nResult<Map<String, Object>> login(@RequestBody @Validated AuthenticationPacket authenticationPacket) {
        return authService.login(authenticationPacket);
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public I18nResult<Boolean> register(@RequestBody @Validated RegistryDTO registryDTO) {
        return authService.register(registryDTO);
    }
}
