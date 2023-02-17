package com.shu.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.shu.hospital.domain.Order;
import com.shu.hospital.service.OrderService;
import com.shu.hospital.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author 小杨的华为
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-02-17 14:43:35
*/
@Service
public class OrderServiceImpl extends MppServiceImpl<OrderMapper, Order>
    implements OrderService{

}




