package top.gaogle.dao.slave;

import org.springframework.stereotype.Repository;
import top.gaogle.param.auth.TestEditParam;

@Repository
public interface TestSlaveMapper {
    int insert(TestEditParam editParam);
}
