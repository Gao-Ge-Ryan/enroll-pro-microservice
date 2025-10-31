package top.gaogle.model.register;

import top.gaogle.domain.register.ActivityInfo;

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
