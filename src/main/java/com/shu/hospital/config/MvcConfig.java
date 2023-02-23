package com.shu.hospital.config;

import com.shu.hospital.interceptor.LoginInterceptor;
import com.shu.hospital.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author: Young
 * @date: 2023/2/20 23:28
 * @email: 1683209437@qq.com
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(   //设置哪些接口不用拦截（如登录页面）
                    "/**"
                ).order(1);     //拦截器的执行次序，越小越先执行
        //拦截器默认拦截全部访问路径
        //刷新token的拦截器先执行
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }
}