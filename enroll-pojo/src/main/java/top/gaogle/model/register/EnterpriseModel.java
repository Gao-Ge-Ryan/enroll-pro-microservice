package top.gaogle.model.register;

import top.gaogle.domain.register.Enterprise;

public class EnterpriseModel extends Enterprise {

    private Integer onGoingStatusCount;

    public Integer getOnGoingStatusCount() {
        return onGoingStatusCount;
    }

    public void setOnGoingStatusCount(Integer onGoingStatusCount) {
        this.onGoingStatusCount = onGoingStatusCount;
    }
}
