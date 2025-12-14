package top.gaogle.framework.commons.util;

import java.security.SecureRandom;

public class CaptchaGeneratorUtil {

    private static final String CHARACTERS = "0123456789";
    private static final int CAPTCHA_LENGTH = 6;

    public static String generate6Captcha() {
        StringBuilder captcha = new StringBuilder(CAPTCHA_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            captcha.append(CHARACTERS.charAt(index));
        }
        return captcha.toString();
    }

    public static String generateCaptcha(int length) {
        StringBuilder captcha = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            captcha.append(CHARACTERS.charAt(index));
        }
        return captcha.toString();
    }

}
