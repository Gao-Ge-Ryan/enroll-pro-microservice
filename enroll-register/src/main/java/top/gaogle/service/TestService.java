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
            System.out.println("================================="+RootContext.getXID());
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("test");
            I18nResult<String> result1 = testFeign.insert();

            if (result1.isSuccessful()){
                System.out.println("sssssssss");
            }
            testMapper.insert(editParam);
            int i = 1/0;
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);

            result.failed().setMessage("register_message","test发生异常：");
            throw e;
        }
        return result;
    }

}
