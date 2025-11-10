package top.gaogle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.dao.master.TestMapper;
import top.gaogle.i18n.I18nResult;
import top.gaogle.param.auth.TestEditParam;
import top.gaogle.util.UniqueUtil;

@Service
public class TestService extends SuperService {

    private final TestMapper testMapper;

    @Autowired
    public TestService(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    public I18nResult<String> test() {
        I18nResult<String> result = I18nResult.newInstance();
        try {
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("test");
            testMapper.insert(editParam);
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);
            result.failed().setMessage("test发生异常：");
        }
        return result;
    }

}
