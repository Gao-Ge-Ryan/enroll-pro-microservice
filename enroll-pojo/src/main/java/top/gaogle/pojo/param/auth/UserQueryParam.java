package top.gaogle.pojo.param.auth;


import top.gaogle.framework.commons.pojo.SuperQuerying;
import top.gaogle.pojo.entity.auth.User;

import java.util.List;

public class UserQueryParam extends User implements SuperQuerying {

    private String roleId;
    private List<String> usernames;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
