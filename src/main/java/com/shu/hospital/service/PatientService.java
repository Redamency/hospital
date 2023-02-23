package com.shu.hospital.service;

import com.shu.hospital.constant.R;
import com.shu.hospital.domain.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 小杨的华为
* @description 针对表【patient】的数据库操作Service
* @createDate 2023-02-17 14:39:48
*/
public interface PatientService extends IService<Patient> {

    R login(Patient patient);

    R getPatientInfo(String token);
}
