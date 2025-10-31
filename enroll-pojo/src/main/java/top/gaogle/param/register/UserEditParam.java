package top.gaogle.param.register;


import top.gaogle.domain.register.User;

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
