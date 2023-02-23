package com.shu.hospital.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author: Young
 * @date: 2023/2/18 20:55
 * @email: 1683209437@qq.com
 */
@Configuration
public class MyRedisConfig {
    @Resource
    private RedisConnectionFactory factory;

    @Bean
    public RedisTemplate redisTemplate() {      //默认是用JDK序列化
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer.string();
        redisTemplate.setKeySerializer(new StringRedisSerializer());    //key-序列化       用String序列化
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(serializer);   //v-序列化         用JSON序列化
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(serializer);

//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        om.setTimeZone(TimeZone.getDefault());
//        om.configure(MapperFeature.USE_ANNOTATIONS, false);
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        serializer.setObjectMapper(om);

        return redisTemplate;
    }
}