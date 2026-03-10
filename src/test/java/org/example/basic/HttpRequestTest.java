package org.example.basic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    private RestTestClient restTestClient;

    @BeforeEach
    void setup(WebApplicationContext context) {
        restTestClient = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void greetingShouldReturnDefaultMessage() {
        restTestClient.get()
                .uri("http://localhost:8080:%d".formatted(port))
                .exchange()
                .expectBody(String.class)
                .isEqualTo("Hello, World!");
    }
}
