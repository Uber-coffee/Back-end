package auth.security.token;

import auth.config.AppProperties;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.CustomerDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenProvider extends TokenProvider {

    public static final String HEADER = "Authorization";

    private static final String TOKEN_TYPE = "Bearer";

    public AccessTokenProvider(AppProperties appProperties,
                               InvalidTokenRepository invalidTokenRepository,
                               CustomerDetailsService customerDetailsService) {
        super(HEADER,
                TOKEN_TYPE,
                invalidTokenRepository,
                appProperties.getAccessToken(),
                customerDetailsService);
    }
}
