package top.gaogle.pojo.param.register;


import top.gaogle.pojo.entity.register.User;

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
