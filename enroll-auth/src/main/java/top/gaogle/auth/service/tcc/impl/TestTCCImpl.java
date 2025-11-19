package top.gaogle.auth.service.tcc.impl;

import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gaogle.auth.dao.master.TestMapper;
import top.gaogle.auth.dao.master.TestTxRelationMapper;
import top.gaogle.auth.service.tcc.TestTCC;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.pojo.model.auth.TestTxRelationModel;
import top.gaogle.pojo.param.auth.TestEditParam;
import top.gaogle.pojo.param.auth.TestTxRelationEditParam;

@Service
public class TestTCCImpl extends SuperService implements TestTCC {

    private final TestMapper testMapper;
    private final TestTxRelationMapper testTxRelationMapper;

    @Autowired
    public TestTCCImpl(TestMapper testMapper, TestTxRelationMapper testTxRelationMapper) {
        this.testMapper = testMapper;
        this.testTxRelationMapper = testTxRelationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public I18nResult<String> test(TestEditParam editParam) {
        String xid = RootContext.getXID();
        log.info("开始TCC分支事务，XID={}, editParam={}", RootContext.getXID(), editParam);
        I18nResult<String> result = I18nResult.newInstance();
        testMapper.insert(editParam);
        TestTxRelationEditParam txRelationEditParam = new TestTxRelationEditParam();
        txRelationEditParam.setTestId(editParam.getId());
        txRelationEditParam.setTxId(xid);
        testTxRelationMapper.insert(txRelationEditParam);

        result.succeed().setData("test");
        return result;
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Boolean commitTest(BusinessActionContext context) {
        log.info("TCC提交成功, XID={}, editParam={}", context.getXid(), context.getActionContext("editParam"));
        testTxRelationMapper.deleteByTxId(context.getXid());
//        int i = 1/0;
        return true;
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelTest(BusinessActionContext context) {
        log.warn("TCC回滚业务, XID={}, editParam={}", context.getXid(), context.getActionContext("editParam"));
        TestTxRelationModel model = testTxRelationMapper.selectByTxId(context.getXid());
        if (model != null) {
            testMapper.deleteById(model.getTestId());
//            int i = 1/0;
            testTxRelationMapper.deleteByTxId(context.getXid());
        }
        return true;
    }

}
