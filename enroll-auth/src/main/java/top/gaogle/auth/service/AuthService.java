package top.gaogle.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.auth.util.Auth0TokenUtil;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.CaptchaGeneratorUtil;
import top.gaogle.framework.commons.util.UniqueUtil;
import top.gaogle.framework.security.pojo.LoginUser;
import top.gaogle.framework.security.service.TokenService;
import top.gaogle.pojo.entity.auth.AuthenticationPacket;
import top.gaogle.pojo.enums.security.AuthorityEnumConst;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
            loginUser.setUserid("use% rid");
            loginUser.setUsername("user% name");
            String token = String.join("_", loginUser.getUsername(), UniqueUtil.getUniqueId(), CaptchaGeneratorUtil.generateCaptcha(8));
            loginUser.setToken(token);
            Set<String> permissions = new LinkedHashSet<>();
            permissions.add(AuthorityEnumConst.USER_PUT_ADMIN);
            loginUser.setPermissions(permissions);
            Map<String, Object> claims = new HashMap<>();
            claims.put(SecurityConstants.USER_KEY, token);
            claims.put(SecurityConstants.DETAILS_USER_ID, loginUser.getUserid());
            claims.put(SecurityConstants.DETAILS_USERNAME, loginUser.getUsername());
            String accessToken = Auth0TokenUtil.generateToken(claims);
            Map<String, Object> rspMap = new HashMap<>();
            rspMap.put("access_token", accessToken);
            tokenService.refreshToken(loginUser);
            result.succeed().setData(rspMap);
        } catch (Exception e) {
            log.error("登录失败：", e);
            result.failed().setMessage("登录失败，请联系管理员！");
        }
        return result;
    }
}
