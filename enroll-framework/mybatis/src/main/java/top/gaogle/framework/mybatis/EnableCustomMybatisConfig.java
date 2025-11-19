package top.gaogle.framework.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * 启用自定义 MyBatis 多数据源配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableTransactionManagement
@MapperScans({
        @MapperScan(value = "top.gaogle.**.dao.master",
                sqlSessionFactoryRef = "sqlSessionFactory",
                sqlSessionTemplateRef = "sqlSessionTemplate"),
        @MapperScan(value = "top.gaogle.**.dao.slave",
                sqlSessionFactoryRef = "slaveSqlSessionFactory",
                sqlSessionTemplateRef = "slaveSqlSessionTemplate")
})
@Import(MybatisDataSourceConfig.class)
public @interface EnableCustomMybatisConfig {
}
