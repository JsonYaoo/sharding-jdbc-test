package com.jsonyao.shardingjdbc.test;

import com.jsonyao.shardingjdbc.ShardingjdbcApplication;
import com.jsonyao.shardingjdbc.dao.AreaMapper;
import com.jsonyao.shardingjdbc.dao.OrderMapper;
import com.jsonyao.shardingjdbc.model.Area;
import com.jsonyao.shardingjdbc.model.AreaExample;
import com.jsonyao.shardingjdbc.model.Order;
import com.jsonyao.shardingjdbc.model.OrderExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * ShardingJDBC测试应用测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShardingjdbcApplication.class})
public class ShardingjdbcApplicationTests {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private AreaMapper areaMapper;

    /**
     * 测试Spring XML配置Sharding JDBC
     */
    @Test
    public void testOrder(){
        Order order = new Order();
        order.setId(1);
        order.setUserId(19);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insert(order);
    }

    /**
     * 测试SpringBoot配置Sharding JDBC
     */
    @Test
    public void testOrder2(){
        Order order = new Order();
        order.setId(4);
        order.setUserId(20);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insert(order);
    }

    /**
     * 测试Sharding JDBC查询
     */
    @Test
    public void testSelectOrder(){
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andIdEqualTo(4)
                .andUserIdEqualTo(20);
        List<Order> orders = orderMapper.selectByExample(orderExample);
        orders.forEach(o -> System.out.println(o.getId() + "-----" + o.getUserId()));
    }

    /**
     * 测试Sharding JDBC全局表插入
     */
    @Test
    public void testGlobalInsert(){
        Area area = new Area();
        area.setId(2);
        area.setName("上海");
        areaMapper.insert(area);
    }

    /**
     * 测试Sharding JDBC全局表查询
     */
    @Test
    public void testGlobalSelect(){
        AreaExample areaExample = new AreaExample();
        areaExample.createCriteria().andIdEqualTo(2);
        List<Area> areas = areaMapper.selectByExample(areaExample);
        areas.forEach(a -> System.out.println(a.getId() + "---" + a.getName()));
    }
}
