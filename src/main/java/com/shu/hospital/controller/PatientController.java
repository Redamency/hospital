package com.shu.hospital.controller;

import com.shu.hospital.constant.R;
import com.shu.hospital.domain.Patient;
import com.shu.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: Young
 * @date: 2023/2/17 16:27
 * @email: 1683209437@qq.com
 */

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Resource
    private PatientService patientService;


//    @GetMapping
//    public String get(){
//        return "I am Young !";
//    }

//    @GetMapping
//    public R getP() {
//        return new R(200,"Young",patientService.getById(100001));
//    }



    @PostMapping("{username}/{password}")
    public R register(@PathVariable String username, @PathVariable String password) {
        Patient patient = new Patient();
        patient.setPname(username);
        patient.setPassword(password);
        boolean save = patientService.save(patient);
        if (save)
            return R.success("注册成功!", null);
        return R.fail("注册失败!");
    }

    @PostMapping("/login")
    public R login(@RequestBody Patient patient) {
        return patientService.login(patient);
    }

    @GetMapping("/info")
    public R getPatientInfo(@RequestParam("token") String token) {
        return patientService.getPatientInfo(token);
    }


}