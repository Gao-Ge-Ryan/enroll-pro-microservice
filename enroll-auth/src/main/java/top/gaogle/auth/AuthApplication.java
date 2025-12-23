package top.gaogle.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import top.gaogle.framework.mybatis.EnableCustomMybatisConfig;
import top.gaogle.framework.nacos.util.WatchFunctionHelper;

@EnableCustomMybatisConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        // 为加载检查点设置一个搜索范围: main启动类所在的包
        WatchFunctionHelper.scanCheckPoint(AuthApplication.class.getPackage().getName());
    }
}
