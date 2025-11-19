package top.gaogle.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.gaogle.framework.feign.EnableCustomFeignClients;
import top.gaogle.framework.mybatis.EnableCustomMybatisConfig;

@EnableCustomFeignClients
@EnableCustomMybatisConfig
@SpringBootApplication
public class RegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class, args);
    }
}
