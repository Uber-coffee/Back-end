package auth.service.auth;

import auth.entity.InvalidToken;
import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.repository.InvalidTokenRepository;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CommonAuthService {

    private static final Logger log = LoggerFactory.getLogger(CommonAuthService.class);

    private final AccessTokenProvider accessTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final InvalidTokenRepository invalidTokenRepository;

    public CommonAuthService(AccessTokenProvider accessTokenProvider,
                             RefreshTokenProvider refreshTokenProvider,
                             InvalidTokenRepository invalidTokenRepository) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.invalidTokenRepository = invalidTokenRepository;
    }

    public void logout(HttpServletRequest httpServletRequest) throws TokenException {
        final String accessToken = accessTokenProvider.resolveToken(httpServletRequest);
        final String refreshToken = refreshTokenProvider.resolveToken(httpServletRequest);

        InvalidToken token = new InvalidToken(accessToken, accessTokenProvider.getExpireTime(accessToken));
        invalidTokenRepository.save(token);

        token = new InvalidToken(refreshToken, refreshTokenProvider.getExpireTime(refreshToken));
        invalidTokenRepository.save(token);
    }

    public void refresh(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws TokenException, UserNotFoundException {
        refreshTokenProvider.validateToken(httpServletRequest);

        final Authentication auth = refreshTokenProvider.getAuthentication(httpServletRequest);
        accessTokenProvider.writeTokenToResponse(accessTokenProvider.createToken(auth), httpServletResponse);
        refreshTokenProvider.writeTokenToResponse(refreshTokenProvider.createToken(auth), httpServletResponse);

        final String refreshToken = refreshTokenProvider.resolveToken(httpServletRequest);
        InvalidToken token = new InvalidToken(refreshToken, refreshTokenProvider.getExpireTime(refreshToken));
        invalidTokenRepository.save(token);

        try {
            final String accessToken = accessTokenProvider.resolveToken(httpServletRequest);
            token = new InvalidToken(accessToken, accessTokenProvider.getExpireTime(accessToken));
            invalidTokenRepository.save(token);
        } catch (TokenException e) {
            log.warn("Invalid access token.");
        }
    }
}
