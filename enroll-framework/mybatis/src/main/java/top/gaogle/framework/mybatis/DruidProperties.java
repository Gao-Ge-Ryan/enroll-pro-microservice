package top.gaogle.framework.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import top.gaogle.framework.enums.IndexedEnum;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * druid 配置属性
 *
 * @author gaogle
 */
@Configuration
public class DruidProperties {
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.connectTimeout}")
    private int connectTimeout;

    @Value("${spring.datasource.druid.socketTimeout}")
    private int socketTimeout;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.connectProperties}")
    private String connectProperties;

    public DruidDataSource dataSource(DruidDataSource datasource) {
        // 配置初始化大小、最小、最大 */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        // 配置获取连接等待超时的时间 */
        datasource.setMaxWait(maxWait);

        // 配置驱动连接超时时间，检测数据库建立连接的超时时间，单位是毫秒 */
        datasource.setConnectTimeout(connectTimeout);

        // 配置网络超时时间，等待数据库操作完成的网络超时时间，单位是毫秒 */
        datasource.setSocketTimeout(socketTimeout);

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        // 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        //用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。

        datasource.setValidationQuery(validationQuery);
        // 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);
        // 为数据库密码提供加密功能
        datasource.setConnectionProperties(connectProperties);
        return datasource;
    }

    @MappedTypes(IndexedEnum.class)
    public static class IndexedEnumTypeHandler<E extends IndexedEnum<Serializable>> extends BaseTypeHandler<E> {
        private final Class<E> type;
        //记录枚举值和枚举的对应关系
        private Map<Integer, E> enumMap;

        public IndexedEnumTypeHandler(Class<E> type) {
            if (type == null) {
                throw new IllegalArgumentException("Type argument cannot be null");
            }
            this.type = type;
            E[] enums = type.getEnumConstants();
            //配置到 <typeHandler> 初始化时，这里的 type 只是一个接口，并不是枚举，所以要特殊判断
            //下面除了第一个 setNonNullParameter 赋值不需要 enumMap，其他 3 个都需要，
            //由于正常情况下实体类不会使用 LabelValue 接口类型，所以这里没有对 null 进行特殊处理
            if (enums != null) {
                //这里将枚举值和枚举类型存入 enumMap，方便后续快速取值
                this.enumMap = new HashMap<>(enums.length);
                for (E anEnum : enums) {
                    this.enumMap.put((Integer) anEnum.value(), anEnum);
                }
            }
        }

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
            ps.setInt(i, (Integer) parameter.value());
        }

        @Override
        public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
            int i = rs.getInt(columnName);
            if (rs.wasNull()) {
                return null;
            } else {
                try {
                    //取出值对应的枚举类
                    return enumMap.get(i);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.", ex);
                }
            }
        }

        @Override
        public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            int i = rs.getInt(columnIndex);
            if (rs.wasNull()) {
                return null;
            } else {
                try {
                    //取出值对应的枚举类
                    return enumMap.get(i);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.", ex);
                }
            }
        }

        @Override
        public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            int i = cs.getInt(columnIndex);
            if (cs.wasNull()) {
                return null;
            } else {
                try {
                    //取出值对应的枚举类
                    return enumMap.get(i);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.", ex);
                }
            }
        }

    }
}
