package com.founder.addressreporter.controller;
import com.alibaba.fastjson.JSON;
import com.founder.addressreporter.bean.*;
import com.founder.addressreporter.runnable.*;
import com.founder.addressreporter.schedule.Rec_BZDZTask;
import com.founder.addressreporter.schedule.Rec_FBZDZTask;
import com.founder.addressreporter.service.REC_BZDZService;
import com.founder.addressreporter.service.REC_FBZDZService;
import com.founder.addressreporter.service.RedisService;
import com.founder.addressreporter.utils.AddressUploadValue;
import com.founder.addressreporter.utils.DateUtil;
import com.founder.addressreporter.utils.HttpUtil;
import com.founder.addressreporter.utils.ProcessValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 姜涛
 * @create 2021-11-03 9:51
 */
@Slf4j
@RestController
@RequestMapping("/AddressReporter")
@Api(tags = "数据统计和数据订阅")
public class AddressReporterController {
    @Autowired
    private REC_BZDZService bzdzService;
    @Autowired
    private REC_FBZDZService fbzdzService;
    @Autowired
    private RedisService redisService;
    @Value("${user.usernamepy}")
    private String username;
    @Value(value = "${user.username}")
    private String username1;
    @Value(value = "${user.password}")
    private String password;
    @Value(value = "${user.addrserverurl}")
    private String addrserverurl;
    @Value(value = "${user.remark}")
    private String remark;
    @Value(value = "${user.httpurl}")
    private String httpUrl;
    @Value(value = "${user.rec_type_bzdz}")
    private String bzdzType;
    @Value(value = "${user.rec_type_fbzdz}")
    private String fbzdzType;
    @Value(value = "${user.uploadperbatch}")
    private  Integer uploadperbatch;
    @Resource(name = "StringObject")
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "注册信息")
    @GetMapping("/register")
    public Map<String,String> register(){
        Map<String,String> resultMap = new HashMap<>();
        try {
            /*信息注册*/
            RegisterInfo register = new RegisterInfo(username1, password, addrserverurl, remark);
            String registerJsonStr = JSON.toJSONString(register);
            Map<String, String> map = HttpUtil.doPost(httpUrl + "Register", registerJsonStr);
            String token = map.get("token");
            System.out.println(token);
            /*使用redis存储token*/
            redisTemplate.opsForHash().put(username, "token",token);
            System.out.println("注册信息....");
            resultMap.put("result","0");
            resultMap.put("time",DateUtil.dateToStr(new Date()));
            resultMap.put("message","0");
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("result","0");
        resultMap.put("time",DateUtil.dateToStr(new Date()));
        resultMap.put("message","0");
        return resultMap;
    }

    @ApiOperation(value = "保活")
    @GetMapping("/keepLive")
    public Map<String,String> keepLive(){
        Map<String,String> resultMap = new HashMap<>();
        try {
            ExecutorService service = Executors.newFixedThreadPool(5);
            /*启用保活线程*/
            KeepLiveRunnableTask runnableTask = new KeepLiveRunnableTask(httpUrl,username);
            service.execute(runnableTask);
            System.out.println("保活线程启动....");
            resultMap.put("result","0");
            resultMap.put("time",DateUtil.dateToStr(new Date()));
            resultMap.put("message","0");
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("result","1");
        resultMap.put("time",DateUtil.dateToStr(new Date()));
        resultMap.put("message","1");
        return resultMap;
    }
    @ApiOperation(value = "标准地址数据更新通知")
    @GetMapping("/notifyByBzdz")
    public Map<String,String> notifyByBzdz(){
        Map<String,String> resultMap = new HashMap<>();
        try {
            /*得到token*/
            String token = (String) redisTemplate.opsForHash().get(username,"token");
            /*得到lasttime*/
            String lasstime = (String)redisTemplate.opsForHash().get(username,"lasttimeByBzdz") ;
            NotifyInfo info = new NotifyInfo(token, DateUtil.dateToStr(new Date()), bzdzType, "数据的最后更新时间",lasstime);
            String infoJsonStr = JSON.toJSONString(info);
            Map<String, String> map1 = HttpUtil.doPost(httpUrl + "Notify", infoJsonStr);
            System.out.println("标准地址数据更新通知....");
            resultMap.put("result","0");
            resultMap.put("time",DateUtil.dateToStr(new Date()));
            resultMap.put("message","0");
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("result","1");
        resultMap.put("time",DateUtil.dateToStr(new Date()));
        resultMap.put("message","1");
        return resultMap;
    }

    @ApiOperation(value = "非标准地址数据更新通知")
    @GetMapping("/notifyByFBzdz")
    public Map<String,String> notifyByFBzdz(){
        Map<String,String> resultMap = new HashMap<>();
        try {
            /*得到token*/
            String token = (String) redisTemplate.opsForHash().get(username,"token");
            /*得到lasttime*/
            String lasstime = (String)redisTemplate.opsForHash().get(username,"lasttimeByFBzdz") ;
            NotifyInfo info = new NotifyInfo(token, DateUtil.dateToStr(new Date()), fbzdzType, "数据的最后更新时间",lasstime);
            String infoJsonStr = JSON.toJSONString(info);
            Map<String, String> map1 = HttpUtil.doPost(httpUrl + "Notify", infoJsonStr);
            System.out.println("非标准地址数据更新通知....");
            resultMap.put("result","0");
            resultMap.put("time",DateUtil.dateToStr(new Date()));
            resultMap.put("message","0");
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("result","1");
        resultMap.put("time",DateUtil.dateToStr(new Date()));
        resultMap.put("message","1");
        return resultMap;
    }


    @ApiOperation(value = "接收标准地址和非标准数据")
    @PostMapping("/SendData")
    public Map<String, Object> sendData(SendData data) throws ParseException {
        Map<String, Object> resultMap3 = new HashMap<>();
        if ("rec_bzdz".equals(data.getRec_type())) {
            Map<String, Object> resultMap1 = bzdzService.sendData(data);
            return resultMap1;
        }
        /*非标准地址数据*/
        if ("rec_fbzdz".equals(data.getRec_type())) {
            Map<String, Object> resultMap2 = fbzdzService.sendData(data);
            return resultMap2;
        }
        resultMap3.put("failnum", 0);
        resultMap3.put("successnum", 0);
        resultMap3.put("message", "请您输入正确的数据传输类型");
        resultMap3.put("result", "1");
        return resultMap3;
    }


    @ApiOperation(value = "数据统计接口")
    @PostMapping("/GetTotal")
    public Map<String, String> getTotal(@RequestBody GetTotalInfo getTotalInfo) {
        /*返回数据格式map*/
        Map<String, String> resultMap = new HashMap<>();
        /*查询数据的数量值*/
        int total = 0;
        try {
            /*标准地址数据数量*/
            if ("rec_bzdz".equals(getTotalInfo.getRec_type())) {
               String beginTime =  getTotalInfo.getData_begintime();
               String endTime = getTotalInfo.getData_endtime();
                total = bzdzService.getTotal(beginTime, endTime);
                resultMap.put("result", "0");
                resultMap.put("total", total + "");
                resultMap.put("message", "ok");
                resultMap.put("time", DateUtil.dateToStr(new Date()));
                return resultMap;
            }

            /*非标准地址数据数量*/
            if ("rec_fbzdz".equals(getTotalInfo.getRec_type())) {
                String beginTime =  getTotalInfo.getData_begintime();
                String endTime = getTotalInfo.getData_endtime();
                total = fbzdzService.getTotal(beginTime, endTime);
                resultMap.put("result", "0");
                resultMap.put("total", total + "");
                resultMap.put("message", "ok");
                resultMap.put("time", DateUtil.dateToStr(new Date()));
                return resultMap;
            }
            resultMap.put("result", "1");
            resultMap.put("total", total + "");
            resultMap.put("message", "没有匹配的地址数据类型");
            resultMap.put("time", DateUtil.dateToStr(new Date()));
            return resultMap;
        } catch (Exception e) {
            log.error("出现错误:{}" + e.getMessage());
        }
        resultMap.put("result", "1");
        resultMap.put("total", total + "");
        resultMap.put("message", "出现异常错误");
        resultMap.put("time", DateUtil.dateToStr(new Date()));
        return resultMap;
    }


    @ApiOperation(value = "数据订阅接口")
    @PostMapping("/Subscribe")
    public Map<String, String> subscribe(@RequestBody SubscribeInfo subscribeInfo) {
        /*返回数据格式map*/
        Map<String, String> resultMap = new HashMap<>();
        /*定义线程的并发数量*/
        ExecutorService threadPool = Executors.newFixedThreadPool(50);
        /**/
        try {
            /*标准地址数据订阅推送*/
            if ("rec_bzdz".equals(subscribeInfo.getRec_type())) {
                /*在执行标准地址数据推送之前进行标准地址数据的处理*/
                REC_BZDZRunnableTask task = new REC_BZDZRunnableTask(subscribeInfo,username,uploadperbatch,httpUrl);
                threadPool.execute(task);
                System.out.println("开始推送标准地址数据.....");
                resultMap.put("result", "0");
                resultMap.put("message", "ok");
                resultMap.put("time", DateUtil.dateToStr(new Date()));
                return resultMap;
            }
            /*非标准地址数据订阅推送*/
            if ("rec_fbzdz".equals(subscribeInfo.getRec_type())) {
                /*在执行非标准地址数据推送之前进行非标准地址数据的处理*/
                REC_FBZDZRunnableTask task = new REC_FBZDZRunnableTask(subscribeInfo,username,uploadperbatch,httpUrl);
                threadPool.execute(task);
                System.out.println("开始推送非标准地址数据.....");
                resultMap.put("result", "0");
                resultMap.put("message", "ok");
                resultMap.put("time", DateUtil.dateToStr(new Date()));
                return resultMap;
            }
            resultMap.put("result", "1");
            resultMap.put("message", "没有匹配的地址数据类型");
            resultMap.put("time", DateUtil.dateToStr(new Date()));
            return resultMap;
        } catch (Exception e) {
            log.error("出现错误:{}" + e.getMessage());
        }
        resultMap.put("result", "1");
        resultMap.put("message", "出现异常错误");
        resultMap.put("time", DateUtil.dateToStr(new Date()));
        return resultMap;
    }

    @ApiOperation(value = "查询redis缓存数据")
    @GetMapping("/getRedisInfo")
    public Map<String, Object> getRedisInfo() {
        Map<String, Object> resultMap = redisService.getRedisInfo();
        return resultMap;
    }


    @ApiOperation(value = "删除redis缓存数据")
    @GetMapping("/deleteRedis")
    public Map<String,String> deleteRedis(){
        Map<String,String> resultMap = redisService.deleteRedis();
        return resultMap;
    }

    @ApiOperation(value = "注销用户信息")
    @GetMapping("/CancelUserInfo")
    public Map<String,Object> cancelUserInfo(){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            String token = (String) redisTemplate.opsForHash().get(username, "token");
            CancelInfo info = new CancelInfo(token, "标准地址数据推送完成,请求注销....");
            String infoJson = JSON.toJSONString(info);
            HttpUtil.doPost(httpUrl, infoJson);
            System.out.println("注销信息....");

            /*消除保活线程*/
            redisTemplate.opsForHash().delete(username, "token");

            resultMap.put("result","0");
            resultMap.put("time",DateUtil.dateToStr(new Date()));
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("result","1");
        resultMap.put("time",DateUtil.dateToStr(new Date()));
        resultMap.put("message", "出现异常错误");
        return resultMap;
    }

  @ApiOperation(value = "标准地址数据校验接口")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "beginTime",value = "开始时间",required = true,dataTypeClass = String.class),
          @ApiImplicitParam(name = "endTime",value = "结束时间",required = true,dataTypeClass = String.class)
  })
    @GetMapping("/bzdzAddressClean")
    public Map<String,Object> bzdzAddressClean(String beginTime,String endTime){
        Map<String, Object> resultMap = new HashMap<>();
        ExecutorService service = Executors.newFixedThreadPool(10);
        Rec_BZDZTask bzdzTask = new Rec_BZDZTask(username, beginTime, endTime);
        service.execute(bzdzTask);
        resultMap.put("result","SUCCESS");
        resultMap.put("msg","标准地址数据质检开始......");
        return resultMap;
    }

    @ApiOperation(value = "标准地址数据质检进度查询")
    @GetMapping("/checkbzdzAddressCleanResult")
    public Map<String,Object> checkbzdzAddressCleanResult(){
        Map<String,Object> resultMap = new HashMap<>();
        if (ProcessValue.bzdzProcessValue == 1){
            ProcessValue.bzdzProcessValue = 0;
            return ProcessValue.bzdzProcessResult;
        }else if (ProcessValue.bzdzProcessValue == 2){
            resultMap.put("result", "SUCCESS");
            resultMap.put("msg", "标准地址数据处理中，请稍等.....");
            return resultMap;

        }else {
            resultMap.put("result", "FAILURE");
            resultMap.put("msg", "标准地址数据处理流程未开启.....");
            return resultMap;
        }
    }



    @ApiOperation(value = "非标准地址数据校验接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime",value = "开始时间",required = true,dataTypeClass = String.class),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = true,dataTypeClass = String.class)
    })
    @GetMapping("/fbzdzAddressClean")
    public Map<String,Object> fbzdzAddressClean(String beginTime,String endTime){
        Map<String, Object> resultMap = new HashMap<>();
        ExecutorService service = Executors.newFixedThreadPool(10);
        Rec_FBZDZTask fbzdzTask = new Rec_FBZDZTask(username, beginTime, endTime);
        service.execute(fbzdzTask);
        resultMap.put("result","SUCCESS");
        resultMap.put("msg","非标准地址数据质检开始......");
        return resultMap;

    }

    @ApiOperation(value = "非标准地址数据质检进度查询")
    @GetMapping("/checkfbzdzAddressCleanResult")
    public Map<String,Object> checkfbzdzAddressCleanResult(){
        Map<String,Object> resultMap = new HashMap<>();
        if (ProcessValue.fbzdzProcessValue == 1){
            ProcessValue.fbzdzProcessValue = 0;
            return ProcessValue.fbzdzProcessResult;
        }else if (ProcessValue.fbzdzProcessValue == 2){
            resultMap.put("result", "SUCCESS");
            resultMap.put("msg", "非标准地址数据处理中，请稍等.....");
            return resultMap;
        } else {
            resultMap.put("result", "FAILURE");
            resultMap.put("msg", "非标准地址数据处理流程未开启.....");
            return resultMap;
        }
    }

    @ApiOperation(value = "标准地址数据上传进度查询")
    @GetMapping("/checkbzdzAddressUploadResult")
    public Map<String,Object> checkbzdzAddressUploadResult(){
        Map<String,Object> resultMap = new HashMap<>();
        if (AddressUploadValue.bzdzProcessValue == 1){
            AddressUploadValue.bzdzProcessValue = 0;
            return AddressUploadValue.bzdzProcessResult;
        }else if (AddressUploadValue.bzdzProcessValue == 2){
            resultMap.put("result", "SUCCESS");
            resultMap.put("msg", "标准地址数据上传中，请稍等.....");
            return resultMap;

        }else {
            resultMap.put("result", "FAILURE");
            resultMap.put("msg", "标准地址数据上传流程未开启.....");
            return resultMap;
        }
    }


    @ApiOperation(value = "非标准地址数据上传进度查询")
    @GetMapping("/checkfbzdzAddressUploadResult")
    public Map<String,Object> checkfbzdzAddressUploadResult(){
        Map<String,Object> resultMap = new HashMap<>();
        if (AddressUploadValue.fbzdzProcessValue == 1){
            AddressUploadValue.fbzdzProcessValue = 0;
            return AddressUploadValue.fbzdzProcessResult;
        }else if (AddressUploadValue.fbzdzProcessValue == 2){
            resultMap.put("result", "SUCCESS");
            resultMap.put("msg", "标准地址数据上传中，请稍等.....");
            return resultMap;
        }else {
            resultMap.put("result", "FAILURE");
            resultMap.put("msg", "标准地址数据上传流程未开启.....");
            return resultMap;
        }
    }



}

