package auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security")
@Getter
public class AppProperties {

    private final AccessToken accessToken = new AccessToken();

    private final RefreshToken refreshToken = new RefreshToken();

    public interface Token {

        String getSecret();

        Long getExpire();
    }

    @Getter
    @Setter
    public static class AccessToken implements Token {

        private String secret;

        private Long expire;
    }

    @Getter
    @Setter
    public static class RefreshToken implements Token {

        private String secret;

        private Long expire;
    }
}
