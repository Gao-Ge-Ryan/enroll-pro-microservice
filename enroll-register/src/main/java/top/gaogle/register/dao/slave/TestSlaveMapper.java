package top.gaogle.register.dao.slave;

import org.springframework.stereotype.Repository;
import top.gaogle.pojo.param.auth.TestEditParam;

@Repository
public interface TestSlaveMapper {
    int insert(TestEditParam editParam);
}
