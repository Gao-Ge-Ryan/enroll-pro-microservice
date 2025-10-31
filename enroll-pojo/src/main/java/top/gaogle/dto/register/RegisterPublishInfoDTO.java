package top.gaogle.dto.register;


import top.gaogle.enums.register.FormTemplateFlagEnum;
import top.gaogle.model.register.RegisterPublishModel;
import top.gaogle.pojo.PageModel;

import java.util.Map;

public class RegisterPublishInfoDTO {

    private Map<String, Object> infoModel;
    private RegisterPublishModel registerPublishModel;
    private PageModel<Map<String, Object>> pageInfoModel;

    private FormTemplateFlagEnum templateFlag;

    public Map<String, Object> getInfoModel() {
        return infoModel;
    }

    public void setInfoModel(Map<String, Object> infoModel) {
        this.infoModel = infoModel;
    }

    public RegisterPublishModel getRegisterPublishModel() {
        return registerPublishModel;
    }

    public void setRegisterPublishModel(RegisterPublishModel registerPublishModel) {
        this.registerPublishModel = registerPublishModel;
    }

    public PageModel<Map<String, Object>> getPageInfoModel() {
        return pageInfoModel;
    }

    public void setPageInfoModel(PageModel<Map<String, Object>> pageInfoModel) {
        this.pageInfoModel = pageInfoModel;
    }

    public FormTemplateFlagEnum getTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(FormTemplateFlagEnum templateFlag) {
        this.templateFlag = templateFlag;
    }
}
