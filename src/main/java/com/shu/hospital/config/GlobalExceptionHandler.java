package com.shu.hospital.config;

import com.shu.hospital.constant.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: Young
 * @date: 2023/2/19 22:19
 * @email: 1683209437@qq.com
 */

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public R handler(RuntimeException e) {
        log.error("运行时异常:-----------{}",e.getMessage());
        return R.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R handler(IllegalArgumentException e) {
        log.error("Assert异常:----------{}",e.getMessage());
        return R.fail(e.getMessage());
    }
}