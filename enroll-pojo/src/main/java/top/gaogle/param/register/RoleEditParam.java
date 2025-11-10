package top.gaogle.param.register;


import top.gaogle.entity.register.Role;
import top.gaogle.enums.auth.AuthorityEnum;

import java.util.List;

public class RoleEditParam extends Role {
    private List<AuthorityEnum> authorityEnums;

    public List<AuthorityEnum> getUserAuthorityEnums() {
        return authorityEnums;
    }

    public void setUserAuthorityEnums(List<AuthorityEnum> authorityEnums) {
        this.authorityEnums = authorityEnums;
    }
}
