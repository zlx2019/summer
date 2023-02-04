package com.zero.summer.example.service.impl;

import com.zero.summer.core.entity.Order;
import com.zero.summer.db.service.impl.SuperServiceImpl;
import com.zero.summer.example.mapper.OrderMapper;
import com.zero.summer.example.service.IOrderService;
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
