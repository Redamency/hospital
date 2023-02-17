package com.shu.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.hospital.domain.Employee;
import com.shu.hospital.service.EmployeeService;
import com.shu.hospital.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 小杨的华为
* @description 针对表【employee】的数据库操作Service实现
* @createDate 2023-02-17 14:43:28
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




