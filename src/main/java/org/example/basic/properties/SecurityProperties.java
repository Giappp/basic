package org.example.basic.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.security")
@Configuration
@Getter
public class SecurityProperties {
    private final Jwt jwt = new Jwt();
    private final Cookie cookie = new Cookie();

    @Getter
    public static class Jwt {
        private String secret;
        private final TokenExpiration user = new TokenExpiration();
        private final TokenExpiration admin = new TokenExpiration();

        @Getter
        public static class TokenExpiration {
            private long accessTokenExpiration;
            private long refreshTokenExpiration;
        }
    }

    @Getter
    public static class Cookie {
        private boolean secure;
        private final RoleCookie user = new RoleCookie();
        private final RoleCookie admin = new RoleCookie();

        @Getter
        public static class RoleCookie {
            private final CookieMeta accessToken = new CookieMeta();
            private final CookieMeta refreshToken = new CookieMeta();

            @Getter
            public static class CookieMeta {
                private String name;
                private String path;
            }
        }
    }

    public String getSecretKey() {
        return jwt.getSecret();
    }

    public SecurityProperties.Cookie.RoleCookie userCookieProps() {
        return this.getCookie().getUser();
    }

    public long userAccessMaxAge() {
        return this.getJwt().getUser().getAccessTokenExpiration();
    }

    public long userRefreshMaxAge() {
        return this.getJwt().getUser().getRefreshTokenExpiration();
    }

    public SecurityProperties.Cookie.RoleCookie adminCookieProps() {
        return this.getCookie().getAdmin();
    }

    public long adminAccessMaxAge() {
        return this.getJwt().getAdmin().getAccessTokenExpiration();
    }

    public long adminRefreshMaxAge() {
        return this.getJwt().getAdmin().getRefreshTokenExpiration();
    }
}
