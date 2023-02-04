package com.summer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.core.entity.Order;
import com.summer.example.service.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Zero.
 * @date 2023/2/4 6:59 PM
 */
@SpringBootTest
public class ExampleApplicationTest {

    @Autowired
    private IOrderService orderService;
    @Test
    void test(){
        Page<Order> list = orderService.listPage(1, 3);

        list.getRecords().forEach(System.out::println);
    }
}
