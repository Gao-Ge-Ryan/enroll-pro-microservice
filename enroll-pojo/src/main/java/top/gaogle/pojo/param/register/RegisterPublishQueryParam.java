package top.gaogle.pojo.param.register;


import top.gaogle.pojo.entity.register.RegisterPublish;
import top.gaogle.framework.commons.pojo.SuperQuerying;

public class RegisterPublishQueryParam extends RegisterPublish implements SuperQuerying {

    private String accountBy;

    public String getAccountBy() {
        return accountBy;
    }

    public void setAccountBy(String accountBy) {
        this.accountBy = accountBy;
    }
}
