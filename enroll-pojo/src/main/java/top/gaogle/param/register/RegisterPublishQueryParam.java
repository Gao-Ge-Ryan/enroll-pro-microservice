package top.gaogle.param.register;


import top.gaogle.domain.register.RegisterPublish;
import top.gaogle.pojo.SuperQuerying;

public class RegisterPublishQueryParam extends RegisterPublish implements SuperQuerying {

    private String accountBy;

    public String getAccountBy() {
        return accountBy;
    }

    public void setAccountBy(String accountBy) {
        this.accountBy = accountBy;
    }
}
