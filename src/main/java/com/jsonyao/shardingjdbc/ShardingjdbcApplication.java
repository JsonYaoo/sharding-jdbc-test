package com.jsonyao.shardingjdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * ShardingJDBC测试应用
 */
@SpringBootApplication
@ImportResource("classpath*:sharding-jdbc.xml")
@MapperScan(basePackages = {"com.jsonyao.shardingjdbc.dao"})
public class ShardingjdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingjdbcApplication.class, args);
    }
}
