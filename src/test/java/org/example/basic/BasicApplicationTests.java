package org.example.basic;

import org.example.basic.rest.HelloWorldController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BasicApplicationTests {

    @Autowired
    private HelloWorldController helloWorldController;

    @Test
    void contextLoads() {
        assertThat(helloWorldController).isNotNull();
    }

    @Test
    void testApi(){
        HelloWorldController helloWorldController = new HelloWorldController();
        assertEquals("Hello World!",helloWorldController.helloMethod(),"Should return Hello World!");
    }
}
