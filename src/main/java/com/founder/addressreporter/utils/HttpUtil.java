package com.founder.addressreporter.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtil {

  private static CloseableHttpClient httpClient = HttpClients.createDefault();


  /*get/{token}方法*/
  public static Map<String,String> doGet(String url,String param){
    String uri = url + param;
    HttpGet httpGet = new HttpGet(uri);
    CloseableHttpResponse httpResponse = null;
    try {
      httpResponse = httpClient.execute(httpGet);
      String resultStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
      Map<String,String> resultMap  = (Map<String,String>) JSON.parseObject(resultStr, Map.class);
      return resultMap;
    }catch (Exception e){
      e.printStackTrace();
      log.error("出现异常;{}",e.getMessage());
    }
    return new HashMap<String,String>();
  }

  /*post/json对象方法方法*/
  public static Map<String,String> doPost(String url,String JsonParam){
    HttpPost httpPost = new HttpPost(url);
    httpPost.addHeader("Content-type","application/json; charset=utf-8");
    httpPost.setEntity(new StringEntity(JsonParam, Charset.forName("UTF-8")));
    CloseableHttpResponse httpResponse = null;
    try {
      httpResponse = httpClient.execute(httpPost);
      String resultStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
      Map<String,String> resultMap = (Map<String,String>)JSON.parseObject(resultStr, Map.class);
      return resultMap;
    }catch (Exception e){
      e.printStackTrace();
      log.error("出现异常;{}",e.getMessage());
    }
    return new HashMap<String,String>();

  }

}