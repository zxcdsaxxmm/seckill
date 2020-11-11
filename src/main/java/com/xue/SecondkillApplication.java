package com.xue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.xue.mapper")
public class SecondkillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondkillApplication.class, args);
    }

}
