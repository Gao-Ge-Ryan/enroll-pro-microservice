package top.gaogle.pojo.model.register;

import top.gaogle.pojo.entity.register.EnterpriseUser;

import java.util.List;

public class EnterpriseUserModel extends EnterpriseUser {

    List<RoleModel> roleModels;

    public List<RoleModel> getRoleModels() {
        return roleModels;
    }

    public void setRoleModels(List<RoleModel> roleModels) {
        this.roleModels = roleModels;
    }
}
