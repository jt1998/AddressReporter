package com.founder.addressreporter.service;

import java.util.Map;

/**
 * @author 姜涛
 * @create 2021-11-04 11:27
 */
public interface RedisService {
    Map<String, Object> getRedisInfo();

    Map<String, String> deleteRedis();
}
