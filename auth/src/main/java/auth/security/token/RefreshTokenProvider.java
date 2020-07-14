package auth.security.token;

import auth.config.AppProperties;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.CustomerDetailsService;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenProvider extends TokenProvider {

    public static final String HEADER = "Refresh-token";

    private static final String TOKEN_TYPE = "Bearer";

    public RefreshTokenProvider(AppProperties appProperties,
                                InvalidTokenRepository invalidTokenRepository,
                                CustomerDetailsService customerDetailsService) {
        super(HEADER,
                TOKEN_TYPE,
                invalidTokenRepository,
                appProperties.getRefreshToken(),
                customerDetailsService);
    }
}
