package top.gaogle.framework.redis.pojo;

import java.util.Map;

public class ProducerParam {

    private String streamName;
    private Map<String, Object> param;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
