package top.gaogle.pojo.model.register;

import top.gaogle.pojo.entity.register.Enterprise;

public class EnterpriseModel extends Enterprise {

    private Integer onGoingStatusCount;

    public Integer getOnGoingStatusCount() {
        return onGoingStatusCount;
    }

    public void setOnGoingStatusCount(Integer onGoingStatusCount) {
        this.onGoingStatusCount = onGoingStatusCount;
    }
}
