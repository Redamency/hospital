package com.shu.hospital.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.hospital.config.RedissonConfig;
import com.shu.hospital.domain.Patient;
import com.shu.hospital.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.executor.RedissonClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: Young
 * @date: 2023/2/20 15:15
 * @email: 1683209437@qq.com
 */

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private RedissonClient redissonClient;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test01() {
        redisTemplate.opsForValue().set("name","杨哥");
        String name = (String) redisTemplate.opsForValue().get("name");
        System.out.println(name);

    }
    @Test
    public void test02() {
        redisTemplate.opsForValue().set("user",new Patient(1,"Young","123456",new Date(),1,"18878999987","Shanghai"));
        Object user = redisTemplate.opsForValue().get("user");
        System.out.println(user);
    }

    @Test
    public void test03() throws JsonProcessingException {
        Patient patient = new Patient(1, "Young", "123456", new Date(), 1, "18878999987", "Shanghai");
        //手动序列化
        String json = mapper.writeValueAsString(patient);
        stringRedisTemplate.opsForValue().set("user1",json);
        String jsonUser = stringRedisTemplate.opsForValue().get("user1");
        //手动反序列化
        Patient patient1 = mapper.readValue(jsonUser, Patient.class);
        System.out.println(patient1);
    }

    /**
     * hash 测试
     */
    @Test
    public void test04() {
        stringRedisTemplate.opsForHash().put("TT","name","Young");
        stringRedisTemplate.opsForHash().put("TT","age","20");
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("TT");
        System.out.println(map);
    }

    private ExecutorService es = Executors.newFixedThreadPool(500);
    @Test
    public void test05() {
        Runnable task = () -> {
          for (int i = 0; i < 10; ++i) {
              long id = redisIdWorker.nextId("order");
              System.out.println("id:" + id);
          }
        };
        for (int i = 0; i < 30; ++i) {
            es.submit(task);
        }
//        System.out.println(1);
    }

    @Test
    public void test06() throws InterruptedException {
        //获取锁
        RLock lock = redissonClient.getLock("lock");
        //尝试获取锁
        boolean isLock = lock.tryLock(1, TimeUnit.SECONDS);
        if (isLock) {
            try {
                System.out.println("执行业务");
            } finally {
                //释放锁
                lock.unlock();
            }
        }
    }


}