package com.founder.addressreporter;

import com.founder.addressreporter.runnable.RedisRunnableTask;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.founder.addressreporter.mapper")
public class AddressReporterApplication {

    private static String username;
    @Value("${user.usernamepy}")
    public void setUsername(String value){
        username = value;
    }

    public static void main(String[] args) {
        SpringApplication.run(AddressReporterApplication.class, args);
        new Thread(new RedisRunnableTask(username)).start();
        System.out.println("redis初始化....");
    }

}
