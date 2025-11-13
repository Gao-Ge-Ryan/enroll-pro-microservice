package top.gaogle.service.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import top.gaogle.i18n.I18nResult;
import top.gaogle.param.auth.TestEditParam;

@LocalTCC
public interface TestTCC {

    /**
     * 测试方法
     *
     * @return 返回结果
     */
    @TwoPhaseBusinessAction(
            //该tcc的bean名称,写方法名便可，全局唯一
            name = "test",
            //二阶段确认方法
            commitMethod = "commitTest",
            //二阶段取消方法
            rollbackMethod = "cancelTest",
            //启用tcc防护（避免幂等、空回滚、悬挂）
            useTCCFence = true)
    I18nResult<String> test(@BusinessActionContextParameter("editParam") TestEditParam editParam);

    /**
     * 确认方法，与@TwoPhaseBusinessAction.commitMethod对应
     * 注：context可以传递try方法的参数
     *
     * @param context 上下文
     * @return 是否成功
     */
    Boolean commitTest(BusinessActionContext context);

    /**
     * 回滚方法，与@TwoPhaseBusinessAction.rollbackMethod对应
     * 注：context可以传递try方法的参数
     *
     * @param context 上下文
     * @return 是否成功
     */
    Boolean cancelTest(BusinessActionContext context);


}
