package com.example.producttrading;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.producttrading.mapper")
public class ProductTradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductTradingApplication.class, args);
    }

}
