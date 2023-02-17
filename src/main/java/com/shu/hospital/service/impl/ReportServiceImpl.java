package com.shu.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.hospital.domain.Report;
import com.shu.hospital.service.ReportService;
import com.shu.hospital.mapper.ReportMapper;
import org.springframework.stereotype.Service;

/**
* @author 小杨的华为
* @description 针对表【report(体检报告表)】的数据库操作Service实现
* @createDate 2023-02-17 14:43:42
*/
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report>
    implements ReportService{

}




