package top.gaogle.framework.commons.util;

import java.util.Base64;

/**
 * 密钥工具类
 *
 * @author gaogle
 * @since 1.0.0
 */
public class SecretUtil {

    public static String getEncoderByBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String getEncoderByBase64(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    public static String getDecoderByBase64(String str) {
        byte[] decodedBytes = Base64.getDecoder().decode(str);
        return new String(decodedBytes);
    }

}
