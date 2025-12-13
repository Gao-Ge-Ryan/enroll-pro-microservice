package top.gaogle.framework.commons.util;


import top.gaogle.framework.commons.common.CommonsConst;
import top.gaogle.framework.commons.function.SnowflakeIdGenerator;

public class UniqueUtil {

    private UniqueUtil() {
        throw new IllegalStateException(CommonsConst.PROHIBIT_INSTANTIATION);
    }

    public static final long WORKER_ID = 1;

    private static final long DATACENTER_ID = 1;

    private static final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(WORKER_ID, DATACENTER_ID);

    public static String getUniqueId() {
        return String.valueOf(snowflakeIdGenerator.nextId());
    }
}
