package com.shu.hospital.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.hospital.constant.R;
import com.shu.hospital.constant.RedisConstants;
import com.shu.hospital.domain.Employee;
import com.shu.hospital.dto.LoginFormDto;
import com.shu.hospital.dto.UserDto;
import com.shu.hospital.service.EmployeeService;
import com.shu.hospital.mapper.EmployeeMapper;
import com.shu.hospital.utils.CacheClient;
import com.shu.hospital.utils.RedisData;
import com.shu.hospital.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
* @author Young
* @description 针对表【employee】的数据库操作Service实现
* @createDate 2023-02-17 14:43:28
*/
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private CacheClient cacheClient;

//    @Override
//    public R login(String username, String password) {
//        Employee employee = query().eq(username != null, username, username).one();
//        return new R(employee == null ? 401 : 200 , "Young", employee);
//    }

    @Override
    public R login(Employee employee) {
        //根据用户名和密码查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getPname,employee.getPname());
        wrapper.eq(Employee::getPassword,employee.getPassword());
        Employee loginEmployee = this.baseMapper.selectOne(wrapper);
        //结果不为空，则生成token，存入redis
        if (loginEmployee != null) {
            //暂时用UUID，终极方案是jwt
            String key = UUID.randomUUID() + "";
            //存入redis
            loginEmployee.setPassword(null);
            redisTemplate.opsForValue().set(key, loginEmployee, 30, TimeUnit.MINUTES);
            //返回数据  用HashMap存
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",key);
            return R.success("登录成功!",data);
        }
        return R.fail("用户名或密码错误!");
    }

    @Override
    public R login(LoginFormDto loginFormDto) {
        // 1.校验手机号和验证码
        String phone = loginFormDto.getPhone();
        String code = loginFormDto.getCode();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return R.fail("手机号格式错误！");
        }
        if (RegexUtils.isCodeInvalid(code)) {
            // 2.如果不符合，返回错误信息
            return R.fail("验证码格式错误！");
        }
        // 3.从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return R.fail("验证码错误！");
        }
        // 4.一致，根据手机号查询用户 select * from employee where phone = ?
        Employee employee = query().eq("telephone", phone).one();
        // 5.判断用户是否存在
        if (employee == null) {
            // 6.不存在，创建新用户并保存
            employee = createEmployeeWithPhone(phone);
        }
        // 7.保存用户信息到 redis中
        // 7.1.随机生成token，作为登录令牌
        String token = cn.hutool.core.lang.UUID.randomUUID().toString(true);
        // 7.2.将User对象转为HashMap存储
        UserDto userDto = BeanUtil.copyProperties(employee, UserDto.class);
        // 7.3.存储：key为token，value为UserDto对象
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForValue().set(tokenKey, JSONUtil.toJsonStr(userDto), RedisConstants.LOGIN_USER_TTL, TimeUnit.DAYS);
        // 8.返回token
        return R.success("登录成功!", token);
    }

    private Employee createEmployeeWithPhone(String phone) {
        Employee employee = new Employee();
        employee.setTelephone(phone);
        employee.setPname(RandomUtil.randomNumbers(8));
        save(employee);     //写入数据库
        return employee;
    }

    @Override
    public R getEmployeeInfo(String token) {
        //根据token获取用户信息，redis
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            Employee loginEmployee = JSON.parseObject(JSON.toJSONString(obj), Employee.class);
            HashMap<String, Object> data = new HashMap<>();
            data.put("name",loginEmployee.getPname());
            data.put("department", loginEmployee.getDepartment());
            return R.success("获取用户信息成功!",data);
        }
        return R.fail("用户未登录/登录已过期");
    }

    @Override
    public R logout(String token) {
        Boolean delete = redisTemplate.delete(token);
        if (delete) {
            return R.success("退出成功!",null);
        }
        return R.fail("退出失败!");
    }

    @Override
    public R sendCode(String phone) {
        // 校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            //手机号不符合要求，返回错误信息
            return R.fail("手机号格式错误！");
        }
        // 手机号符合要求,生成验证码                                            -->是否能验证手机号是否有用
        String code = RandomUtil.randomNumbers(6);

        //保存验证码到redis
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);

        // 发送验证码   模拟   实际需要调用阿里云/其它公司api
        log.debug("发送短信验证码成功，验证码：{}", code);

        return R.success("发送成功！", null);
    }

    @Override
    public R queryById(Integer id) {
        //缓存穿透
//        Employee employee = queryWithPassThrough(id);
//        Employee employee = cacheClient.queryWithPassThrough(RedisConstants.CACHE_EMPLOYEE_KEY, id, Employee.class, this::getById, RedisConstants.CACHE_EMPLOYEE_TTL, TimeUnit.MINUTES);
        //基于互斥锁的缓存击穿
        Employee employee = queryWithMutex(id);
        //基于逻辑过期的缓存击穿
//        Employee employee = cacheClient.queryWithLogicalExpire(RedisConstants.CACHE_EMPLOYEE_KEY, id, Employee.class, this::getById, RedisConstants.CACHE_EMPLOYEE_TTL, TimeUnit.MINUTES);
        if (employee == null)
            return R.fail("Employee信息不存在");
        return R.success("查询成功！", employee);
    }

    //缓存击穿
    public Employee queryWithLogicalExpire(Integer id) {
        String key = RedisConstants.CACHE_EMPLOYEE_KEY + id;
        //从redis种查询缓存
        String employeeJson = stringRedisTemplate.opsForValue().get(key);
        //未命中
        if (StrUtil.isBlank(employeeJson)) {
            return null;
        }
        //命中，json反序列化为对象
        RedisData redisData = JSONUtil.toBean(employeeJson, RedisData.class);
        Employee employee = JSONUtil.toBean((JSONObject) redisData.getData(), Employee.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            //未过期，直接返回店铺信息
            return employee;
        }
        //已过期，需要缓存重建
        //缓存重建
        //获取互斥锁
        String lockKey = RedisConstants.LOCK_EMPLOYEE_KEY + id;
        boolean isLcok = tryLock(lockKey);
        //判断是否获取锁
        if (isLcok) {
            //获取，开启独立线程，实现缓存重建      (线程池)
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                //重建
                try {
                    rebuildRedis(id, 20L);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    unLock(lockKey);
                }
            });
        }

        //未获取，返回过期的信息
        return employee;
    }

    // 缓存重建
    public void rebuildRedis(Integer id, Long expireSeconds) throws InterruptedException {
        Thread.sleep(200);
        Employee employee = getById(id);
        RedisData redisData = new RedisData();
        redisData.setData(employee);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        //写入redis
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_EMPLOYEE_KEY + id, JSONUtil.toJsonStr(redisData));
    }
    //缓存穿透
    public Employee queryWithPassThrough(Integer id) {
        String key = RedisConstants.CACHE_EMPLOYEE_KEY + id;
        //从redis种查询缓存
        String employeeJson = stringRedisTemplate.opsForValue().get(key);
        //命中
        if (StrUtil.isNotBlank(employeeJson)) {
            Employee employee = JSONUtil.toBean(employeeJson, Employee.class);
            return employee;
        }
        //由于加入了null，需要判断是否命中null        isNotBlank只能判断有内容的 null 和 "" 无法判断
        if (employeeJson != null)
            return null;    //命中""，直接返回，防止缓存穿透
        //如果是null则未命中  根据id查询数据库
        Employee e = getById(id);
        // 不存在（即数据库中没有），返回错误
        if (e == null) {
            //存入null, 防止缓存穿透
            stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 存在， 写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(e), RedisConstants.CACHE_EMPLOYEE_TTL, TimeUnit.MINUTES);
        //返回employee信息
        return e;
    }

    //基于互斥锁实现缓存击穿
    public Employee queryWithMutex(Integer id) {
        String key = RedisConstants.CACHE_EMPLOYEE_KEY + id;
        //从redis种查询缓存
        String employeeJson = stringRedisTemplate.opsForValue().get(key);
        //命中
        if (StrUtil.isNotBlank(employeeJson)) {
            return JSONUtil.toBean(employeeJson, Employee.class);
        }
        //由于加入了null，需要判断是否命中null        isNotBlank只能判断有内容的 null 和 "" 无法判断
        if (employeeJson != null)
            return null;    //命中""，直接返回，防止缓存穿透
        //实现缓存重建
        //获取互斥锁
        String lockKey = null;
        Employee e = null;
        try {
            lockKey = RedisConstants.LOCK_EMPLOYEE_KEY + id;
            boolean lock = tryLock(lockKey);
            //判断是否获取成功
            if (!lock) {
                Thread.sleep(50);
                return queryWithMutex(id);
            }
            // 成功，查询数据库
            e = getById(id);
            if (e == null) {
                //不存在,将null放入redis
                stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //存在，写入redis
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(e), RedisConstants.CACHE_EMPLOYEE_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } finally {
            //释放互斥锁
            unLock(lockKey);
        }
        //返回
        return e;
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

    @Override
    @Transactional
    public R update(Employee employee) {
        Integer id = employee.getId();
        if (id == null) {
            return R.fail("ID不能为空！");
        }
        // 更新数据库
        updateById(employee);
        //删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_EMPLOYEE_KEY + id);
        return R.success("更新成功！", null);
    }
}




