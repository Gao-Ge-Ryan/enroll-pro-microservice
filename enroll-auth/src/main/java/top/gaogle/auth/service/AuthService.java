package top.gaogle.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.gaogle.auth.dao.master.UserMapper;
import top.gaogle.auth.util.Auth0TokenUtil;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.CaptchaGeneratorUtil;
import top.gaogle.framework.commons.util.DateUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.commons.util.UniqueUtil;
import top.gaogle.framework.security.pojo.LoginUser;
import top.gaogle.framework.security.service.TokenService;
import top.gaogle.framework.security.util.SecurityUtil;
import top.gaogle.pojo.dto.auth.RegistryDTO;
import top.gaogle.pojo.entity.auth.AuthenticationPacket;
import top.gaogle.pojo.entity.auth.User;
import top.gaogle.pojo.enums.security.AuthorityEnumConst;
import top.gaogle.pojo.model.auth.UserModel;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService extends SuperService {

    private final TokenService tokenService;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(TokenService tokenService, UserMapper userMapper) {
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }


    public I18nResult<Map<String, Object>> login(AuthenticationPacket authenticationPacket) {
        I18nResult<Map<String, Object>> result = I18nResult.newInstance();
        try {
            String username = authenticationPacket.getUsername();
            String password = authenticationPacket.getPassword();
            if (StringUtil.isAnyBlank(username, password)) {
                return result.failedBadRequest().setMessage("缺少必要参数");
            }
            UserModel userModel = userMapper.selectByUsername(username);
            if (userModel == null) {
                return result.failedBadRequest().setMessage("用户不存在");
            }
            if (!SecurityUtil.matchesPassword(password, userModel.getPassword())) {
                return result.failedBadRequest().setMessage("密码错误");
            }
            LoginUser loginUser = new LoginUser();
            loginUser.setUserid(userModel.getId());
            loginUser.setUsername(userModel.getUsername());
            String token = String.join("_", loginUser.getUsername(), UniqueUtil.getUniqueId(), CaptchaGeneratorUtil.generateCaptcha(6), StringUtil.UUID());
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

    public I18nResult<Boolean> register(RegistryDTO registryDTO) {
        I18nResult<Boolean> result = I18nResult.newInstance();
        try {
            String username = registryDTO.getUsername();
            String password = registryDTO.getPassword();
            String nickname = registryDTO.getNickname();
            if (StringUtil.isAnyBlank(username, password)) {
                return result.failedBadRequest().setMessage("缺少必要参数");
            }
            String encryptPassword = SecurityUtil.encryptPassword(password);
            User user = new User();
            user.setId(UniqueUtil.getUniqueId());
            user.setUsername(username);
            user.setPassword(encryptPassword);
            user.setNickname(nickname);
            user.setCreateBy(username);
            user.setUpdateBy(username);
            Long timeMillis = DateUtil.currentTimeMillis();
            user.setCreateAt(timeMillis);
            user.setUpdateAt(timeMillis);
            user.setDelFlag(false);
            user.setDisabled(false);
            userMapper.insertOne(user);
            result.succeed().setData(true);
        } catch (DuplicateKeyException duplicateKeyException) {
            log.info("用户名已存在:{}", duplicateKeyException.getMessage());
            result.failedBadRequest().setMessage("用户名已存在");
        } catch (Exception e) {
            log.error("注册失败：", e);
            result.failed().setMessage("注册失败，请联系管理员！");
        }
        return result;

    }
}
