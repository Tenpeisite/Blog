package com.zhj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/18 9:50
 */
@SpringBootApplication
@MapperScan("com.zhj.mapper")
@EnableScheduling
public class ZhjBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhjBlogApplication.class,args);
    }
}
