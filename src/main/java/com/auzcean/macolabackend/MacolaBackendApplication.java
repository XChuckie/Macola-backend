package com.auzcean.macolabackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan("com.auzcean.macolabackend.mapper")  // 将mapper文件的数据访问层操作注入到框架
public class MacolaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MacolaBackendApplication.class, args);
    }

}
