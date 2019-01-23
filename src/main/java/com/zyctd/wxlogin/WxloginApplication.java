package com.zyctd.wxlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication(scanBasePackages="com.zyctd",exclude = DataSourceAutoConfiguration.class)
public class WxloginApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxloginApplication.class, args);
    }
}
