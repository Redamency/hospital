package com.shu.hospital.controller;

import com.shu.hospital.service.PatientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Young
 * @date: 2023/2/17 16:27
 * @email: 1683209437@qq.com
 */

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Resource
    private PatientService patientService;


}