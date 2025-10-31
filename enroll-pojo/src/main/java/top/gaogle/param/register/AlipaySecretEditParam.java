package top.gaogle.param.register;


import top.gaogle.domain.register.AlipaySecret;

public class AlipaySecretEditParam extends AlipaySecret {

    private String enterpriseId;

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
