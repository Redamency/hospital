package com.shu.hospital.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author: Young
 * @date: 2023/2/23 13:38
 * @email: 1683209437@qq.com
 */
@Component
public class RedisIdWorker {
    //当前时间戳
    private static final long BEGIN_TIMESTAMP = 1677159900L;
    //序列号位数
    private static final int COUNT_BITS = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    private StringRedisTemplate stringRedisTemplate;
//
//    private RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }

    public long nextId(String keyPrefix) {
        //生成时间戳
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timeStamp = now - BEGIN_TIMESTAMP;
        //生成序列号
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + data);
        //拼接，位运算
        return timeStamp << COUNT_BITS | count;
    }
}