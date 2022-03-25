package com.wjc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {"com.wjc.mapper"})
@SpringBootApplication
public class EpidemicPreventionManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpidemicPreventionManageApplication.class, args);
    }

}
