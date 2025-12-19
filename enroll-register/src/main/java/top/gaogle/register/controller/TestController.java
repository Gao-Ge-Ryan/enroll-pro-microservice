package top.gaogle.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.web.bind.annotation.*;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.log.annotation.Log;
import top.gaogle.framework.redis.pojo.ProducerParam;

import top.gaogle.framework.redis.service.StringRedisService;
import top.gaogle.framework.security.annotation.RequiresPermissions;
import top.gaogle.framework.security.enums.LogicalEnum;
import top.gaogle.pojo.enums.security.AuthorityEnumConst;
import top.gaogle.register.service.TestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaogle
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;
    private final StringRedisService stringRedisService;

    @Autowired
    public TestController(TestService testService, StringRedisService stringRedisService) {
        this.testService = testService;
        this.stringRedisService = stringRedisService;
    }

    @RequiresPermissions(value = {AuthorityEnumConst.USER_VIEW_ADMIN, AuthorityEnumConst.USER_PUT_ADMIN}, logical = LogicalEnum.OR)
    @GetMapping
    public I18nResult<String> insert() {
        return testService.test();
    }

    @GetMapping("/tcc")
    public I18nResult<String> tcc() {
        return testService.tcc();
    }

    @GetMapping("/testNacos")
    public I18nResult<String> testNacos() {
        return testService.testNacos();
    }

    @PostMapping("/addMsg")
//    @ApiOperation(value = "添加消息")
    public I18nResult<String> addMsg(@RequestBody ProducerParam param) {
        I18nResult<String> result = I18nResult.newInstance();
        Map<String, String> map = new HashMap<>();
        String myStream = stringRedisService.addStreamMsg(param.getStreamName(), param.getParam());
        return result.succeed().setData(myStream);

    }
    @Log("获取pending list集合(消费出现异常 没有ack时)")
    @GetMapping(value = "/getPendingList")
//    @ApiOperation(value = "获取pending list集合(消费出现异常 没有ack时)")
    public I18nResult<Map<String, Object>> getPendingList(@RequestParam(value = "streamName") String streamName,
                                                          @RequestParam(value = "groupName") String groupName) {
        I18nResult<Map<String, Object>> result = I18nResult.newInstance();
        List<MapRecord<String, String, String>> pendingList = stringRedisService.getPendingList(streamName, groupName);
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("pendingList", pendingList);
        stringObjectHashMap.put("size", pendingList.size());
        return result.succeed().setData(stringObjectHashMap);
    }

    @GetMapping(value = "/getPendingByMsgId")
//    @ApiOperation(value = "获取pending list中某个元素(消费出现异常 没有ack时)")
    public I18nResult<List<MapRecord<String, String, String>>> getPendingByMsgId(@RequestParam(value = "streamName") String streamName,
                                                                                 @RequestParam(value = "msgId", required = false) String msgId) {
        I18nResult<List<MapRecord<String, String, String>>> result = I18nResult.newInstance();
        List<MapRecord<String, String, String>> pendingList = stringRedisService.getPendingByMsgId(streamName, msgId);
        return result.succeed().setData(pendingList);
    }

    @DeleteMapping(value = "/deletePendingListByMsgId")
//    @ApiOperation(value = "获取pending list集合(消费出现异常 没有ack时)")
    public I18nResult<Boolean> deletePendingListByMsgId(@RequestParam(value = "streamName") String streamName,
                                                        @RequestParam(value = "msgId", required = false) String msgId) {
        I18nResult<Boolean> result = I18nResult.newInstance();

        stringRedisService.deleteStreamMsg(streamName, msgId);

        return result.succeed().setData(true);
    }

}
