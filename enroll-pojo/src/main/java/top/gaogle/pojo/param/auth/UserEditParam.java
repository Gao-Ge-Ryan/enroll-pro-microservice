package top.gaogle.pojo.param.auth;


import top.gaogle.pojo.entity.auth.User;

import java.util.List;

public class UserEditParam extends User {

    List<String> roleIds;

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
