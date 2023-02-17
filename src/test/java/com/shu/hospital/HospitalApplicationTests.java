package com.shu.hospital;

import com.shu.hospital.domain.Department;
import com.shu.hospital.mapper.DepartmentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HospitalApplicationTests {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Test
    void contextLoads() {

    }

    @Test
    public void test01() {
        List<Department> list = departmentMapper.selectList(null);
        list.forEach(System.out::println);
    }

}
