package top.gaogle.gateway.service;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import top.gaogle.framework.commons.common.CacheConstants;
import top.gaogle.framework.commons.common.CommonsConst;
import top.gaogle.framework.commons.exception.CaptchaException;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.util.SecretUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.redis.service.StringRedisService;
import top.gaogle.gateway.property.CaptchaProperties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码实现处理
 *
 * @author gaogle
 */
@Service
public class ValidateCodeService {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final StringRedisService redisService;
    private final CaptchaProperties captchaProperties;

    @Autowired
    public ValidateCodeService(StringRedisService redisService, CaptchaProperties captchaProperties) {
        this.redisService = redisService;
        this.captchaProperties = captchaProperties;
    }

    /**
     * 生成验证码
     */
    public I18nResult<Map<String, Object>> createCaptcha() {
        I18nResult<Map<String, Object>> result = I18nResult.newInstance();
        Map<String, Object> ajax = new HashMap<>();
        boolean captchaEnabled = captchaProperties.getEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return result.succeed();
        }

        // 保存验证码信息
        String uuid = StringUtil.UUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr , code = null;
        BufferedImage image = null;

        String captchaType = captchaProperties.getType();
        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisService.setCacheObject(verifyKey, code, CommonsConst.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return result.failed().setMessage(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", SecretUtil.getEncoderByBase64(os.toByteArray()));
        return result.succeed().setData(ajax);
    }

    /**
     * 校验验证码
     */
    public void checkCaptcha(String code, String uuid) throws CaptchaException {
        if (StringUtil.isAnyBlank(code, uuid)) {
            throw new CaptchaException("验证码不能为空");
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisService.getCacheObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaException("验证码已失效");
        }
        redisService.deleteObject(verifyKey);
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException("验证码错误");
        }
    }
}
