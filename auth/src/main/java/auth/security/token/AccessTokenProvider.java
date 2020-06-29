package auth.security.token;

import auth.config.AppProperties;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenProvider extends TokenProvider {

    private static final String HEADER = "Authorization";

    private static final String TOKEN_TYPE = "Bearer";

    private final AppProperties appProperties;

    private final UserDetailsService userDetailsService;

    private final InvalidTokenRepository invalidTokenRepository;

    public AccessTokenProvider(AppProperties appProperties,
                               UserDetailsService userDetailsService,
                               InvalidTokenRepository invalidTokenRepository) {
        super(HEADER,
                TOKEN_TYPE,
                invalidTokenRepository,
                appProperties.getAccessToken(),
                userDetailsService);
        this.appProperties = appProperties;
        this.userDetailsService = userDetailsService;
        this.invalidTokenRepository = invalidTokenRepository;
    }
}
