package com.zsc.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void savefile()
    {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/";
        System.out.println(
                path
        );
    }

}
