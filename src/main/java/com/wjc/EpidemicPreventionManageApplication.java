package com.wjc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@MapperScan(value = {"com.wjc.mapper"})
@SpringBootApplication
public class EpidemicPreventionManageApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(EpidemicPreventionManageApplication.class, args);
    }

}
