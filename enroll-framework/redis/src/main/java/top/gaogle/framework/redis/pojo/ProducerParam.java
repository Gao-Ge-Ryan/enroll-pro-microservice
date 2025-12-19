package top.gaogle.framework.redis.pojo;

import java.util.Map;

public class ProducerParam {

    private String streamName;
    private Map<String, String> param;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }
}
