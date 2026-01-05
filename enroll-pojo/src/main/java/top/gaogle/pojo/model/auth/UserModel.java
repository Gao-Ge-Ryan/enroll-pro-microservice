package top.gaogle.pojo.model.auth;


import top.gaogle.pojo.entity.auth.User;
import top.gaogle.pojo.model.register.RoleModel;

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
