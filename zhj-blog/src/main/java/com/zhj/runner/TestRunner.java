package com.zhj.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/11 23:24
 */
@Component
public class TestRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("程序初始化。。。");
    }
}
