package top.gaogle.base.config.quartz;


import org.quartz.JobExecutionContext;
import top.gaogle.base.util.JobInvokeUtil;
import top.gaogle.pojo.entity.base.SysJob;


/**
 * 定时任务处理（允许并发执行）
 *
 * @author gaogle
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
