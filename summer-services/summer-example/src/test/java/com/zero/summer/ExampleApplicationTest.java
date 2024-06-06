package com.zero.summer;

import com.zero.summer.cache.template.CacheTemplate;
import com.zero.summer.example.service.IOrderService;
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
    @Autowired
    private CacheTemplate cacheTemplate;
    @Test
    void test(){
        Object val = cacheTemplate.get("ttt1");
        System.out.println(val);
    }
}
