package top.gaogle.framework.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@MapperScan(value = "top.gaogle.**.dao.master", sqlSessionTemplateRef = "sqlSessionTemplate", sqlSessionFactoryRef = "sqlSessionFactory")
@MapperScan(value = "top.gaogle.**.dao.slave", sqlSessionTemplateRef = "slaveSqlSessionTemplate", sqlSessionFactoryRef = "slaveSqlSessionFactory")
@EnableTransactionManagement
public @interface EnableCustomMybatisConfig {
}
