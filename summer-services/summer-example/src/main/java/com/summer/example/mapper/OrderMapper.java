package com.summer.example.mapper;

import com.summer.core.entity.Order;
import com.summer.db.mapper.SupperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单服务 Mapper层
 *
 * @author Zero.
 * @date 2022/6/21 4:16 下午
 */
@Mapper
public interface OrderMapper extends SupperMapper<Order> {

}
