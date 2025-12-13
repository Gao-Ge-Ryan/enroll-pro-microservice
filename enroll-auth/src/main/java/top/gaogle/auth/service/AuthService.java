package top.gaogle.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.security.pojo.LoginUser;
import top.gaogle.framework.security.service.TokenService;
import top.gaogle.pojo.entity.auth.AuthenticationPacket;

import java.util.Map;

@Service
public class AuthService extends SuperService {

    private final TokenService tokenService;

    @Autowired
    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    public I18nResult<Map<String, Object>> login(AuthenticationPacket authenticationPacket) {
        I18nResult<Map<String, Object>> result = I18nResult.newInstance();
        try {
            LoginUser loginUser = new LoginUser();
            loginUser.setUserid("userid");
            loginUser.setUsername("username");
            Map<String, Object> tokenMap = tokenService.createToken(loginUser);
            result.succeed().setData(tokenMap);
        } catch (Exception e) {
            log.error("登录失败：", e);
            result.failed().setMessage("登录失败，请联系管理员！");
        }
        return result;
    }
}
