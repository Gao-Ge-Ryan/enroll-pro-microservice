package top.gaogle.auth.dao.master;

import org.springframework.stereotype.Repository;
import top.gaogle.pojo.param.auth.TestEditParam;

@Repository
public interface TestMapper {

    int insert(TestEditParam editParam);

    int deleteById(String id);

}
