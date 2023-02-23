package com.shu.hospital.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shu.hospital.constant.RedisConstants;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author: Young
 * @date: 2023/2/22 22:12
 * @email: 1683209437@qq.com
 */
@Slf4j
@Component      //由Spring维护
public class CacheClient {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        //设置逻辑过期
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    //缓存穿透
    public <T, ID> T queryWithPassThrough(String keyPrefix, ID id, Class<T> type, Function<ID, T> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(json)) {
            return JSONUtil.toBean(json, type);
        }
        if (json != null) {
            return null;
        }
        T t = dbFallback.apply(id);
        if (t == null) {
            //空值存入redis
            stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            //返回错误信息
            return null;
        }
        // 存在，写入redis
        this.set(key, t, time, unit);
        return t;
    }

    //缓存击穿  基于逻辑过期
    public <T,ID> T queryWithLogicalExpire(String keyPrefix, ID id, Class<T> type, Function<ID, T> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        T t = JSONUtil.toBean((JSONObject)redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();

        if (expireTime.isAfter(LocalDateTime.now())) {
            return t;
        }

        String lockKey = RedisConstants.LOCK_EMPLOYEE_KEY + id;
        boolean isLock = tryLock(lockKey);
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    T t1 = dbFallback.apply(id);
                    this.setWithLogicalExpire(key, id, time, unit);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    unLock(lockKey);
                }
            });
        }
        return t;
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10); //建立一个有10个线程的线程池

    public boolean tryLock(String key) {
        //redis 的setIfAbsent 只有在key不存在的时候才可以去设置value
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "Young", 10, TimeUnit.SECONDS);
        // 直接返回flag 会发生自动拆箱 可能会出现空指针
        return BooleanUtil.isTrue(flag);
    }

    public void unLock(String key) {
        //释放锁
        stringRedisTemplate.delete(key);
    }
}