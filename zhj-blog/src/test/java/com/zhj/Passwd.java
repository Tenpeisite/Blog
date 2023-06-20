package com.zhj;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.PackageSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/25 20:42
 */
@SpringBootTest
public class Passwd {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test01(){
        String password = passwordEncoder.encode("2000.10.05");
        System.out.println(password);
    }
}
