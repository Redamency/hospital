package com.shu.hospital.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Young
 * @date: 2023/2/17 22:21
 * @email: 1683209437@qq.com
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {

    private Integer code;
    private String message;
    private Object data;

    public static R success(String message, Object data) {
        return new R(200, message, data);
    }

    public static R success(Integer code, String message, Object data) {
        return new R(code, message, data);
    }

    public static R fail(String message) {
        return new R(401, message, null);
    }

}