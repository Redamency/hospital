package com.shu.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.hospital.domain.Patient;
import com.shu.hospital.service.PatientService;
import com.shu.hospital.mapper.PatientMapper;
import org.springframework.stereotype.Service;

/**
* @author 小杨的华为
* @description 针对表【patient】的数据库操作Service实现
* @createDate 2023-02-17 14:39:48
*/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>
    implements PatientService{

}




