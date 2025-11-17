package top.gaogle.auth.dao.master;

import org.springframework.stereotype.Repository;
import top.gaogle.pojo.model.auth.TestTxRelationModel;
import top.gaogle.pojo.param.auth.TestTxRelationEditParam;

@Repository
public interface TestTxRelationMapper {

    int insert(TestTxRelationEditParam editParam);

    int deleteByTxId(String xid);

    TestTxRelationModel selectByTxId(String xid);

}
