package top.gaogle.pojo.model.register;


import top.gaogle.pojo.entity.register.User;

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
