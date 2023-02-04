package com.summer.example.service.impl;

import com.summer.core.entity.Order;
import com.summer.db.service.impl.SuperServiceImpl;
import com.summer.example.mapper.OrderMapper;
import com.summer.example.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Zero.
 * @date 2023/2/4 6:55 PM
 */
@Service
@Slf4j
public class OrderServiceImpl extends SuperServiceImpl<OrderMapper, Order> implements IOrderService {

}
