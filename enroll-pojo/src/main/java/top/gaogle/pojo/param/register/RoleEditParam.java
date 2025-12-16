package top.gaogle.pojo.param.register;


import top.gaogle.pojo.entity.register.Role;
import top.gaogle.pojo.enums.security.AuthorityEnum;

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
