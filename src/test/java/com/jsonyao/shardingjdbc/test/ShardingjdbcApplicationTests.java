package com.jsonyao.shardingjdbc.test;

import com.jsonyao.shardingjdbc.ShardingjdbcApplication;
import com.jsonyao.shardingjdbc.dao.AreaMapper;
import com.jsonyao.shardingjdbc.dao.OrderItemMapper;
import com.jsonyao.shardingjdbc.dao.OrderMapper;
import com.jsonyao.shardingjdbc.model.*;
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

    @Resource
    private OrderItemMapper orderItemMapper;

    /**
     * 测试Spring XML配置Sharding JDBC
     */
    @Test
    public void testOrder(){
        Order order = new Order();
        order.setOrderId(1L);
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
        order.setOrderId(4L);
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
        orderExample.createCriteria().andOrderIdEqualTo(4L)
                .andUserIdEqualTo(20);
        List<Order> orders = orderMapper.selectByExample(orderExample);
        orders.forEach(o -> System.out.println(o.getOrderId() + "-----" + o.getUserId()));
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

    /**
     * 测试Sharding JDBC绑定表插入
     */
    @Test
    public void testOrderItemInsert(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(41);
        orderItem.setOrderId(4L);
        orderItem.setProductName("测试商品");
        orderItem.setNum(1);
        orderItem.setUserId(20);
        orderItemMapper.insert(orderItem);
    }

    /**
     * 测试Sharding JDBC绑定表查询
     */
    @Test
    public void testOrderItemSelect(){
        List<OrderItem> orderItems = orderItemMapper.selectByOrderIdUserId(1);
        System.out.println(orderItems.size());
    }

    /**
     * 测试Sharding JDBC读写分离查询
     * => 可见, Sharding JDBC读写分离只能从读库中查询, 而查询不到写库中的数据, 这点也和MyCat不同
     */
    @Test
    public void testMsOrderSelect(){
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderIdEqualTo(4L)
                .andUserIdEqualTo(20);

        for(int i = 0; i < 10; i++){
            final int times = i;
            List<Order> orders = orderMapper.selectByExample(orderExample);
            orders.forEach(o -> {
                System.out.println("第"+ times +"次查询: orderId: " + o.getOrderId());
                System.out.println("第"+ times +"次查询: userId: " + o.getUserId());
                System.out.println("第"+ times +"次查询: orderAmount: " + o.getOrderAmount());
            });
        }
    }

    /**
     * 分布式ID: 测试Sharding JDBC UUID
     */
    @Test
    public void testOrderUUID(){
        Order order = new Order();
        order.setUserId(15);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insertSelective(order);
    }

    /**
     * 分布式ID: 测试Sharding JDBC UUID与绑定表
     */
    @Test
    public void testOrderItemUUID(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(41);
//        orderItem.setOrderId("92935e77f27e4061ac441f17b94fba02");
        orderItem.setProductName("测试商品");
        orderItem.setNum(1);
        orderItem.setUserId(15);
        orderItemMapper.insert(orderItem);
    }

    /**
     * 分布式ID: 测试Sharding JDBC 雪花算法ID
     */
    @Test
    public void testOrderItemSnowFlake(){
        Order order = new Order();
        order.setUserId(20);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insertSelective(order);
    }

    /**
     * 分布式ID: 测试Sharding JDBC 雪花算法ID与绑定表
     */
    @Test
    public void testOrderSnowFlake(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(41);
        orderItem.setOrderId(567798756077625345L);
        orderItem.setProductName("测试商品");
        orderItem.setNum(1);
        orderItem.setUserId(20);
        orderItemMapper.insert(orderItem);
    }
}
