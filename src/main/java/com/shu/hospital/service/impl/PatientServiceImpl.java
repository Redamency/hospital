package com.shu.hospital.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shu.hospital.constant.R;
import com.shu.hospital.domain.Employee;
import com.shu.hospital.domain.Patient;
import com.shu.hospital.service.PatientService;
import com.shu.hospital.mapper.PatientMapper;
import com.shu.hospital.utils.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.ObjectName;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author 小杨的华为
* @description 针对表【patient】的数据库操作Service实现
* @createDate 2023-02-17 14:39:48
*/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>
    implements PatientService{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private PatientMapper patientMapper;

    @Override
    public R login(Patient patient) {
        //查询是否有该用户
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getPname,patient.getPname());
        wrapper.eq(Patient::getPassword, patient.getPassword());
        Patient loginPatient = this.baseMapper.selectOne(wrapper);
        if (loginPatient != null) {
            //UUID模拟生成token
            String key = UUID.randomUUID().toString();
            //将用户信息存入redis
            //重要信息建议去除
            loginPatient.setPassword(null);
//            try {
//                String Json = JsonUtils.serialize(patient);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
            redisTemplate.opsForValue().set(key, patient, 30, TimeUnit.MINUTES);
            HashMap<String, Object> data = new HashMap<>(); //返回token
            data.put("token", key);
            return R.success("登录成功!", data);
        }
        return R.fail("用户名或密码错误!");
    }

    @Override
    public R getPatientInfo(String token) {
        //根据token从redis中获取用户信息
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            Patient patient = JSON.parseObject(JSON.toJSONString(obj), Patient.class);
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", patient.getPname());
            data.put("telephone",patient.getTelephone());
            return R.success("获取用户信息成功!", data);
        }
        return R.fail("用户未登录/登录已过期");
    }
}




