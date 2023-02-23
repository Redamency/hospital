package com.shu.hospital.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: Young
 * @date: 2023/2/23 16:40
 * @email: 1683209437@qq.com
 */

public class SimpleRedisLock implements ILock{

    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";   //hutool工具类中的UUID可以去除UUID中自带的'-'
    private String name;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name) {
        this.name = name;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        String threadId =  ID_PREFIX + Thread.currentThread().getId();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    @Override
    public void unlock() {
        String threadId =  ID_PREFIX + Thread.currentThread().getId();
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        if (threadId.equals(id)) {          //防止分布式/集群模式下出现误删的情况
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }
}