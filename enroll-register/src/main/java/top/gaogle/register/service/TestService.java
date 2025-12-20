package top.gaogle.register.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.UniqueUtil;
import top.gaogle.pojo.param.auth.TestEditParam;
import top.gaogle.register.dao.master.TestMapper;
import top.gaogle.register.dao.slave.TestSlaveMapper;
import top.gaogle.spi.auth.TestFeign;

import java.lang.reflect.UndeclaredThrowableException;

@Service
public class TestService extends SuperService {

//    @Value("${e}")
//    public String test;

//    @Value("${spring.profiles.active}")
//    public String profile;

//    @Value("${vb}")
//    public String vb;
//
//    @Value("${we}")
//    public String we;
//
//    @Value("${enroll-register}")
//    public String ENROLL_REGISTER;

//    @Value("${enroll-register-dev}")
//    public String ENROLL_REGISTER_DEV;

//    @Value("${enroll-register-dev-yaml}")
//    public String ENROLL_REGISTER_DEV_YAML;

    private final TestMapper testMapper;
    private final TestFeign testFeign;
    private final TestSlaveMapper testSlaveMapper;

    @Autowired
    public TestService(TestMapper testMapper, TestFeign testFeign, TestSlaveMapper testSlaveMapper) {
        this.testMapper = testMapper;
        this.testFeign = testFeign;
        this.testSlaveMapper = testSlaveMapper;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    public I18nResult<String> test() throws BlockException {
        I18nResult<String> result = I18nResult.newInstance();
        try {
            System.out.println("=================================" + RootContext.getXID());
            TestEditParam editParam = new TestEditParam();
            editParam.setId(UniqueUtil.getUniqueId());
            editParam.setName("test");
            I18nResult<String> result1 = testFeign.insert();
            if (result1.isFailed()){
                return result1;
            }

            if (result1.isSuccessful()) {
                System.out.println("sssssssss");
            }
            testMapper.insert(editParam);
            int i = 1 / 0;
            result.succeed().setData("test");
        } catch (Exception e) {
            Throwable cause = e;
            if (cause instanceof UndeclaredThrowableException) {
                cause = ((UndeclaredThrowableException) cause).getUndeclaredThrowable();
            }

            if (cause instanceof BlockException) {
                log.warn("Sentinel blocked request: {}", cause.getMessage());
                // BlockException 是 RuntimeException，Seata 会回滚
                throw (BlockException) cause;
            }

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
            editParam.setName("ssssssssssssssssssssssss");
            testSlaveMapper.insert(editParam);

            int i = 1 / 0;
            result.succeed().setData("test");
        } catch (Exception e) {
            log.error("test发生异常：", e);

            result.failed().setMessage("register_message", "test发生异常：");
            throw e;
        }
        return result;
    }

    public I18nResult<String> testNacos() {
        I18nResult<String> result = I18nResult.newInstance();
//        System.out.println(profile);
//        System.out.println(vb);
//        System.out.println(test);
//        System.out.println(we);
//        System.out.println(ENROLL_REGISTER);
//        System.out.println(ENROLL_REGISTER_DEV);
//        System.out.println(ENROLL_REGISTER_DEV_YAML);
//        result.succeed().setData(test);
        return result;
    }
}
