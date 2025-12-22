package top.gaogle.base.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gaogle.base.dao.master.SysJobLogMapper;
import top.gaogle.pojo.entity.base.SysJobLog;


/**
 * 定时任务调度日志信息 服务层
 *
 * @author gaogle
 */
@Service
public class SysJobLogService {

    private final SysJobLogMapper jobLogMapper;

    @Autowired
    public SysJobLogService(SysJobLogMapper jobLogMapper) {
        this.jobLogMapper = jobLogMapper;
    }

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    public void addJobLog(SysJobLog jobLog) {
        jobLogMapper.insertJobLog(jobLog);
    }

}
