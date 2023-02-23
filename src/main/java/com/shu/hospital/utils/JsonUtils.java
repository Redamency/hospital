package com.shu.hospital.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

/**
 * @author: Young
 * @date: 2023/2/20 15:51
 * @email: 1683209437@qq.com
 */

/**
 * JSON序列化器
 */
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //序列化时序列化对象的所有属性
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //反序列化时如果多了其它属性，不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        //序列化时如果是空对象，不抛出异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //取消时间的转化格式，默认是时间戳，同时设置需要表现的时间格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 对象转为Json字符串      序列化
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String serialize(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    /**
     * json字符串转对象       反序列化
     * @param json
     * @param valueType
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T deserialize(String json, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(json, valueType);
    }


}