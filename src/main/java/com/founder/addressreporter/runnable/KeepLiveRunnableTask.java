package com.founder.addressreporter.runnable;

import com.founder.addressreporter.utils.HttpUtil;
import com.founder.addressreporter.utils.SpringContextUtil;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 姜涛
 * @create 2021-11-15 9:10
 */
public class KeepLiveRunnableTask implements Runnable{
    private String httpUrl;
    private String username;

    public KeepLiveRunnableTask(String httpUrl,String username) {
        this.httpUrl = httpUrl;
        this.username = username;
    }


    @Override
    public void run() {
      try {
          /*注入redisTemplate*/
          RedisTemplate<String,Object> redisTemplate = SpringContextUtil.getBean("StringObject",RedisTemplate.class);
          while(true){
              if (redisTemplate.opsForHash().hasKey(username,"token")){
                  /*得到token*/
                  String token = (String) redisTemplate.opsForHash().get(username,"token");
                  HttpUtil.doGet(httpUrl+"Keeplive/",token);
                  System.out.println("保活.....");
                  /*线程休眠50秒*/
                  Thread.sleep(1000*20);
              }else {
                  break;
              }

          }
          System.out.println("保活结束.......");
      }catch (Exception e){
          e.printStackTrace();
      }
    }
}
