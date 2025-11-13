package top.gaogle.service;

import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.dao.master.TestMapper;
import top.gaogle.i18n.I18nResult;
import top.gaogle.param.auth.TestEditParam;
import top.gaogle.service.tcc.TestTCC;
import top.gaogle.util.UniqueUtil;

@Service
public class TestService extends SuperService {

    private final TestMapper testMapper;
    private final TestTCC testTCC;

    @Autowired
    public TestService(TestMapper testMapper, TestTCC testTCC) {
        this.testMapper = testMapper;
        this.testTCC = testTCC;
    }

    public I18nResult<String> test() {
        I18nResult<String> result = I18nResult.newInstance();
        try {
            System.out.println("================================="+ RootContext.getXID());
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("test");
            testMapper.insert(editParam);
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);
            result.failed().setMessage("test_message","test发生异常：");
        }
        return result;
    }

    public I18nResult<String> tcc() {
        I18nResult<String> result = I18nResult.newInstance();
        try {
            System.out.println("================================="+ RootContext.getXID());
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("testtcc");
            testMapper.insert(editParam);
            TestEditParam testEditParam = new TestEditParam();
            testEditParam.setId(UniqueUtil.getUniqueId());
            testEditParam.setName("testtcctcc");
            testTCC.test(testEditParam);
            TestEditParam testEditParam1 = new TestEditParam();
            testEditParam1.setId(UniqueUtil.getUniqueId());
            testEditParam1.setName("testtcctccend");
            testMapper.insert(testEditParam1);
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);
            result.failed().setMessage("test_message","test发生异常：");
        }
        return result;

    }
}
