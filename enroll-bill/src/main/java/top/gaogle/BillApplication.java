package top.gaogle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BillApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }
}
