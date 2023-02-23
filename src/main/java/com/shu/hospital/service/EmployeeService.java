package com.shu.hospital.service;

import com.shu.hospital.constant.R;
import com.shu.hospital.domain.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shu.hospital.dto.LoginFormDto;

/**
* @author 小杨的华为
* @description 针对表【employee】的数据库操作Service
* @createDate 2023-02-17 14:43:28
*/
public interface EmployeeService extends IService<Employee> {
    R login(Employee employee);

    R login(LoginFormDto loginFormDto);

    R getEmployeeInfo(String token);

    R logout(String token);

    R sendCode(String phone);

    R queryById(Integer id);

    R update(Employee employee);
}
