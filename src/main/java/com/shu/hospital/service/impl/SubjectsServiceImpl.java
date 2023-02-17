package com.shu.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.hospital.domain.Subjects;
import com.shu.hospital.service.SubjectsService;
import com.shu.hospital.mapper.SubjectsMapper;
import org.springframework.stereotype.Service;

/**
* @author 小杨的华为
* @description 针对表【subjects(体检项目表)】的数据库操作Service实现
* @createDate 2023-02-17 14:43:46
*/
@Service
public class SubjectsServiceImpl extends ServiceImpl<SubjectsMapper, Subjects>
    implements SubjectsService{

}




