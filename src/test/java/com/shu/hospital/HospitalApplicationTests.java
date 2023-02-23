package com.shu.hospital;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Test
    public void test02() {
        IPage page = new Page(0,2);
        departmentMapper.selectPage(page,null);
    }

    @Test
    public void test03() {
        int x = 1;
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(x != 2,Department::getDeptNo,x);
        departmentMapper.selectList(lambdaQueryWrapper);
    }

}
