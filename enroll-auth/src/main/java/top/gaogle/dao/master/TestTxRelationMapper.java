package top.gaogle.dao.master;

import org.springframework.stereotype.Repository;
import top.gaogle.model.auth.TestTxRelationModel;
import top.gaogle.param.auth.TestTxRelationEditParam;

@Repository
public interface TestTxRelationMapper {

    int insert(TestTxRelationEditParam editParam);

    int deleteByTxId(String xid);

    TestTxRelationModel selectByTxId(String xid);

}
