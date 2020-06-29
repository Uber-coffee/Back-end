package auth.security.token;

import auth.config.AppProperties;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.CustomerDetailsService;
import auth.service.user_details.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenProvider extends TokenProvider {

    private static final String HEADER = "Refresh-token";

    private static final String TOKEN_TYPE = "Bearer";

    public RefreshTokenProvider(AppProperties appProperties,
                                UserDetailsService userDetailsService,
                                InvalidTokenRepository invalidTokenRepository,
                                CustomerDetailsService customerDetailsService) {
        super(HEADER,
                TOKEN_TYPE,
                invalidTokenRepository,
                appProperties.getRefreshToken(),
                userDetailsService, customerDetailsService);
    }
}
