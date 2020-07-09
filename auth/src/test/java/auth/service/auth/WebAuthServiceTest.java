package auth.service.auth;

import auth.entity.Role;
import auth.payload.WebLoginRequest;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebAuthServiceTest {

    private WebAuthService webAuthService;

    private AuthenticationManager authenticationManager;

    private AccessTokenProvider accessTokenProvider;

    private RefreshTokenProvider refreshTokenProvider;

    @BeforeEach
    public void init(){
        initAuthenticationManager();
        initAccessTokenProvider();
        initRefreshToken();
        webAuthService = new WebAuthService(
                authenticationManager,
                accessTokenProvider,
                refreshTokenProvider
        );
    }

    public void initAuthenticationManager() {
        authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any(Authentication.class))).then(i -> {
            Authentication a = i.getArgument(0);
            return new UsernamePasswordAuthenticationToken(
                    a.getPrincipal(),
                    a.getCredentials(),
                    List.of(Role.ROLE_MANAGER)
            );
        });
    }

    public void initAccessTokenProvider() {
        accessTokenProvider = mock(AccessTokenProvider.class);
        when(accessTokenProvider.createToken(any(Authentication.class))).then(i -> {
            Authentication auth = i.getArgument(0);
            return auth.getPrincipal().toString() +
                    "/" +
                    auth.getCredentials() +
                    "/" +
                    auth.getAuthorities().toArray()[0].toString();
        });
        doAnswer(i -> {
            String token = i.getArgument(0);
            HttpServletResponse httpServletResponse = i.getArgument(1);
            httpServletResponse.addHeader("auth", token);
            return null;
        }).when(accessTokenProvider).writeTokenToResponse(anyString(), any(HttpServletResponse.class));
    }

    public void initRefreshToken() {
        refreshTokenProvider = mock(RefreshTokenProvider.class);
        when(refreshTokenProvider.createToken(any(Authentication.class))).then(i -> {
            Authentication auth = i.getArgument(0);
            return auth.getPrincipal().toString() +
                    "/" +
                    auth.getCredentials() +
                    "/" +
                    auth.getAuthorities().toArray()[0].toString();
        });
        doAnswer(i -> {
            String token = i.getArgument(0);
            HttpServletResponse httpServletResponse = i.getArgument(1);
            httpServletResponse.addHeader("ref", token);
            return null;
        }).when(refreshTokenProvider).writeTokenToResponse(anyString(), any(HttpServletResponse.class));
    }

    @Test
    void login() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        webAuthService.login(new WebLoginRequest("test@test.ru", "Administrator42"), httpServletResponse);
        assertNotNull(httpServletResponse.getHeader("auth"));
        assertNotNull(httpServletResponse.getHeader("ref"));
    }
}