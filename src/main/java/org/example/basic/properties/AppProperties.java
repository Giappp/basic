package org.example.basic.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app")
@Configuration
@Getter
public class AppProperties {

    private final FrontEnd frontEnd = new FrontEnd();

    @Getter
    public static class FrontEnd {
        private String domain;
    }
}