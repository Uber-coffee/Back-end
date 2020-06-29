package auth.service.auth;

import auth.payload.WebLoginRequest;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class WebAuthService {

    private final AuthenticationManager authenticationManager;

    private final AccessTokenProvider accessTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    public WebAuthService(@Qualifier("webAuthenticationManagerBean") AuthenticationManager authenticationManager,
                          AccessTokenProvider accessTokenProvider,
                          RefreshTokenProvider refreshTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    public void login(WebLoginRequest webLoginRequest, HttpServletResponse httpServletResponse) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                webLoginRequest.getEmail(),
                webLoginRequest.getPassword()
        ));
        final String accessToken = accessTokenProvider.createToken(auth);
        final String refreshToken = refreshTokenProvider.createToken(auth);
        accessTokenProvider.writeTokenToResponse(accessToken, httpServletResponse);
        refreshTokenProvider.writeTokenToResponse(refreshToken, httpServletResponse);

    }
}
