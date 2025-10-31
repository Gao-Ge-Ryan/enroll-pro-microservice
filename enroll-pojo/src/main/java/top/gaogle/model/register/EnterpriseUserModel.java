package top.gaogle.model.register;

import top.gaogle.domain.register.EnterpriseUser;

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
