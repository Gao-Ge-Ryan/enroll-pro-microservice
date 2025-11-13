package top.gaogle.service;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.TestFeign;
import top.gaogle.dao.master.TestMapper;
import top.gaogle.i18n.I18nResult;
import top.gaogle.param.auth.TestEditParam;
import top.gaogle.util.UniqueUtil;

@Service
public class TestService extends SuperService {

    private final TestMapper testMapper;
    private final TestFeign testFeign;

    @Autowired
    public TestService(TestMapper testMapper, TestFeign testFeign) {
        this.testMapper = testMapper;
        this.testFeign = testFeign;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    public I18nResult<String> test() {
        I18nResult<String> result = I18nResult.newInstance();
        try {
            System.out.println("=================================" + RootContext.getXID());
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("test");
            I18nResult<String> result1 = testFeign.insert();

            if (result1.isSuccessful()) {
                System.out.println("sssssssss");
            }
            testMapper.insert(editParam);
            int i = 1 / 0;
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);

            result.failed().setMessage("register_message", "test发生异常：");
            throw e;
        }
        return result;
    }

    /**
     * 注：
     * 若全局事务发起者除了发起服务RPC调用，也需要实现自身对应的分支事务TCC处理逻辑，
     * 则可单独定义并实现TCC接口，然后在@GlobalTransactional方法中调用该TCC接口即可，
     * 且在seata中支持TCC模式和AT模式的混合使用,并且tcc的try commit cancel 数据库操作都带事务了
     */
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 120000)
    public I18nResult<String> tcc() {
        I18nResult<String> result = I18nResult.newInstance();
        try {
            System.out.println("=================================" + RootContext.getXID());
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("test");
            I18nResult<String> result1 = testFeign.insert();

            if (result1.isSuccessful()) {
                System.out.println("sssssssss");
            }
            testMapper.insert(editParam);
            I18nResult<String> tcc = testFeign.tcc();
            int i = 1 / 0;
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);

            result.failed().setMessage("register_message", "test发生异常：");
            throw e;
        }
        return result;
    }
}
