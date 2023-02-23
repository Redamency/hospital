package com.shu.hospital.controller;

import com.shu.hospital.constant.R;
import com.shu.hospital.domain.Employee;
import com.shu.hospital.dto.LoginFormDto;
import com.shu.hospital.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: Young
 * @date: 2023/2/18 14:04
 * @email: 1683209437@qq.com
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    @GetMapping("{id}")
    public R queryEmployeeById(@PathVariable("id") Integer id) {
//        System.out.println("111111111");
        return employeeService.queryById(id);
    }

    @PutMapping()
    public R updateEmployee(@RequestBody Employee employee) {
        return employeeService.update(employee);
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public R sendCode(@RequestParam("phone") String phone) {
        return employeeService.sendCode(phone);
    }

    /**
     * 通过用户名和密码注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("{username}/{password}")
    public R register(@PathVariable String username, @PathVariable String password) {
        Employee employee = new Employee();
        employee.setPname(username);
        employee.setPassword(password);
        boolean save = employeeService.save(employee);
        if (save)
            return R.success("注册成功!",null);
        return R.fail("注册失败!");
    }

//    @PostMapping("/login")
//    public R login(@RequestBody Employee employee) {
//        return employeeService.login(employee);
//    }

    /**
     * 通过 手机号 验证码 / 手机号 密码 登录
     * @param loginFormDto
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginFormDto loginFormDto) {
        return employeeService.login(loginFormDto);
    }

    /**
     * 通过token查看用户的信息
     * @param token
     * @return
     */
    @GetMapping("/info")
    public R getEmployeeInfo(@RequestParam("token") String token) {
        R info = employeeService.getEmployeeInfo(token);
//        System.out.println(token);
        if (info.getData() != null)
            return info;
        return R.fail("登录信息无效，请重新登录!");
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public R logout(@RequestHeader("X-Token") String token) {
        //从请求头中获取token，把他从redis中删去
        return employeeService.logout(token);
    }

}