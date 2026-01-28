package top.gaogle.pojo.dto.auth;

public class UserInfoDTO {

    /**
     * 用户名（邮箱格式）
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    private String userId;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
