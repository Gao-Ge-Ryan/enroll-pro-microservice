package top.gaogle.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.gaogle.framework.mybatis.EnableCustomMybatisConfig;

@EnableFeignClients(basePackages = "top.gaogle.spi")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableCustomMybatisConfig
public class RegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class, args);
    }
}
