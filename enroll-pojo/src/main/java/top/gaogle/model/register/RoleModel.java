package top.gaogle.model.register;


import top.gaogle.domain.register.Role;

public class RoleModel extends Role {
    private Boolean authorized = false;
    private String accountBy;

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getAccountBy() {
        return accountBy;
    }

    public void setAccountBy(String accountBy) {
        this.accountBy = accountBy;
    }
}
