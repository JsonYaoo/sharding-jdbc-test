package com.jsonyao.shardingjdbc.test;

import com.jsonyao.shardingjdbc.ShardingjdbcApplication;
import com.jsonyao.shardingjdbc.dao.OrderMapper;
import com.jsonyao.shardingjdbc.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * ShardingJDBC测试应用测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShardingjdbcApplication.class})
public class ShardingjdbcApplicationTests {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void testOrder(){
        Order order = new Order();
        order.setId(1);
        order.setUserId(19);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insert(order);
    }
}
