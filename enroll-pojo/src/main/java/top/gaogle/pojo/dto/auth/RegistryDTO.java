package top.gaogle.pojo.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RegistryDTO {
    /**
     * 用户名（邮箱格式）
     */
    @Email(message = "用户名必须是有效的邮箱地址")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码校验规则：
     * 密码必须包含至少一个字母和一个数字,长度必须在8到20个字符之间,可以包含以下特殊字符（但不是必须的）：@$!%*#?&"
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "密码必须包含至少一个字母和一个数字,长度必须在8到20个字符之间,可以包含以下特殊字符（但不是必须的）：@$!%*#?&"
    )
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 验证码
     */
    private String verificationCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
