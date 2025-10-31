package top.gaogle.model.register;


import top.gaogle.domain.register.User;

import java.util.List;

public class UserModel extends User {

    List<RoleModel> roleModels;

    public List<RoleModel> getRoleModels() {
        return roleModels;
    }

    public void setRoleModels(List<RoleModel> roleModels) {
        this.roleModels = roleModels;
    }
}
