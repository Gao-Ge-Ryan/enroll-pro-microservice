package top.gaogle.pojo.param.register;


import top.gaogle.pojo.entity.register.DynamicRegisterInfo;

import java.math.BigDecimal;
import java.util.Map;

public class DynamicRegisterInfoEditParam extends DynamicRegisterInfo {

    private Map<String, BigDecimal> activityCompositeScoreMap;

    public Map<String, BigDecimal> getActivityCompositeScoreMap() {
        return activityCompositeScoreMap;
    }

    public void setActivityCompositeScoreMap(Map<String, BigDecimal> activityCompositeScoreMap) {
        this.activityCompositeScoreMap = activityCompositeScoreMap;
    }
}
