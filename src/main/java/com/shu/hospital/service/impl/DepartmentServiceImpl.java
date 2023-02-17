package com.shu.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.hospital.domain.Department;
import com.shu.hospital.service.DepartmentService;
import com.shu.hospital.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

/**
* @author 小杨的华为
* @description 针对表【department】的数据库操作Service实现
* @createDate 2023-02-17 14:43:15
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService{

}




