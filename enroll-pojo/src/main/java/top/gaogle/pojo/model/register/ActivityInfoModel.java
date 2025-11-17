package top.gaogle.pojo.model.register;

import top.gaogle.pojo.entity.register.ActivityInfo;

import java.math.BigDecimal;

public class ActivityInfoModel extends ActivityInfo {

    private BigDecimal activityScore;

    public BigDecimal getActivityScore() {
        return activityScore;
    }

    public void setActivityScore(BigDecimal activityScore) {
        this.activityScore = activityScore;
    }
}
