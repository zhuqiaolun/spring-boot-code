package com.demon.springbootcode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class SpringBootCodeApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Date());
    }

}
