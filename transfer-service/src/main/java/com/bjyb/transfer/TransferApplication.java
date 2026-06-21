package com.bjyb.transfer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bjyb.transfer", "com.bjyb.common"})
@MapperScan(basePackages = {"com.bjyb.common.mapper", "com.bjyb.transfer.mapper"})
public class TransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferApplication.class, args);
        System.out.println("=== 北京医保个账转移服务启动成功 ===");
    }
}
