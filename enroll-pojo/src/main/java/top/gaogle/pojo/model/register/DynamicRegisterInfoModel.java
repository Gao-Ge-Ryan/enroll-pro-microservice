package top.gaogle.pojo.model.register;

import top.gaogle.pojo.entity.register.DynamicRegisterInfo;

import java.util.List;

public class DynamicRegisterInfoModel extends DynamicRegisterInfo {

    private List<ActivityInfoModel> activityInfoModels;

    public List<ActivityInfoModel> getActivityInfoModels() {
        return activityInfoModels;
    }

    public void setActivityInfoModels(List<ActivityInfoModel> activityInfoModels) {
        this.activityInfoModels = activityInfoModels;
    }
}
