package com.bjyb.settlement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bjyb.settlement", "com.bjyb.common"})
@MapperScan(basePackages = {"com.bjyb.common.mapper", "com.bjyb.settlement.mapper"})
public class SettlementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettlementApplication.class, args);
        System.out.println("=== 北京医保异地结算服务启动成功 ===");
    }
}
