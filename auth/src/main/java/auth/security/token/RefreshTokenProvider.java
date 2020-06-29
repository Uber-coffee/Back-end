package auth.security.token;

import auth.config.AppProperties;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenProvider extends TokenProvider {

    private static final String HEADER = "Refresh-token";

    private static final String TOKEN_TYPE = "Bearer";

    private final AppProperties appProperties;

    private final UserDetailsService userDetailsService;

    private final InvalidTokenRepository invalidTokenRepository;

    public RefreshTokenProvider(AppProperties appProperties,
                                UserDetailsService userDetailsService,
                                InvalidTokenRepository invalidTokenRepository) {
        super(HEADER,
                TOKEN_TYPE,
                invalidTokenRepository,
                appProperties.getRefreshToken(),
                userDetailsService);
        this.appProperties = appProperties;
        this.userDetailsService = userDetailsService;
        this.invalidTokenRepository = invalidTokenRepository;
    }
}
