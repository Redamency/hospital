package com.shu.hospital.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: Young
 * @date: 2023/2/22 21:43
 * @email: 1683209437@qq.com
 */

/**
 * 逻辑过期 解决缓存击穿
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}