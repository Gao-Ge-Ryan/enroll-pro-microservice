package top.gaogle.base.dao.master;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gaogle.framework.log.pojo.OperateLog;
import top.gaogle.framework.log.pojo.OperateLogQueryParam;

import java.util.List;

@Repository
public interface OperateLogMapper {

    int insert(OperateLog operateLog);

    List<OperateLog> queryByPageAndCondition(OperateLogQueryParam queryParam);

    int deleteOneYearBeforeByCreateAt(@Param("cutoffTime") Long cutoffTime);

}
