package auth.service.auth;

import auth.entity.InvalidToken;
import auth.entity.Role;
import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.exception.WrongAuthServiceException;
import auth.repository.InvalidTokenRepository;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({LoggerFactory.class})
class CommonAuthServiceTest {

    private CommonAuthService commonAuthService;

    private AccessTokenProvider accessTokenProvider;

    private RefreshTokenProvider refreshTokenProvider;

    private InvalidTokenRepository invalidTokenRepository;

    private HttpServletRequest httpServletRequest;

    private Map<DateTime, String> tokens;

    @BeforeEach
    public void init() throws TokenException, UserNotFoundException, WrongAuthServiceException {
        initAccessTokenProvider();
        initRefreshTokenProvider();
        initInvalidTokenRepository();
        initHttpServletRequest();
        tokens = new HashMap<>();
        commonAuthService = new CommonAuthService(accessTokenProvider, refreshTokenProvider, invalidTokenRepository);
    }

    public void initAccessTokenProvider() throws TokenException, UserNotFoundException,  WrongAuthServiceException {
        accessTokenProvider = mock(AccessTokenProvider.class);
        when(accessTokenProvider.createToken(any(Authentication.class))).then(i -> {
            Authentication auth = i.getArgument(0);
            return auth.getPrincipal().toString() +
                    "/" +
                    auth.getCredentials() +
                    "/" +
                    auth.getAuthorities().toArray()[0].toString();
        });
        when(accessTokenProvider.getAuthentication(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            String token = httpServletRequest.getHeader("auth");
            String[] chain = token.split("/");
            return new UsernamePasswordAuthenticationToken(chain[0], chain[1], List.of(Role.getRole(chain[2])));
        });
        when(accessTokenProvider.getExpireTime(anyString())).thenReturn(DateTime.now().plusDays(10));
        when(accessTokenProvider.getRoles(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            String token = httpServletRequest.getHeader("auth");
            String[] chain = token.split("/");
            return List.of(Role.getRole(chain[2]));
        });
        when(accessTokenProvider.getSubject(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            String token = httpServletRequest.getHeader("auth");
            String[] chain = token.split("/");
            return chain[0];
        });
        when(accessTokenProvider.resolveToken(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            return httpServletRequest.getHeader("auth");
        });
        doAnswer(i -> {
            String token = i.getArgument(0);
            HttpServletResponse httpServletResponse = i.getArgument(1);
            httpServletResponse.addHeader("auth", token);
            return null;
        }).when(accessTokenProvider).writeTokenToResponse(anyString(), any(HttpServletResponse.class));
    }

    public void initRefreshTokenProvider() throws TokenException, UserNotFoundException,  WrongAuthServiceException  {
        refreshTokenProvider = mock(RefreshTokenProvider.class);
        when(refreshTokenProvider.createToken(any(Authentication.class))).then(i -> {
            Authentication auth = i.getArgument(0);
            return auth.getPrincipal().toString() +
                    "/" +
                    auth.getCredentials() +
                    "/" +
                    auth.getAuthorities().toArray()[0].toString();
        });
        when(refreshTokenProvider.getAuthentication(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            String token = httpServletRequest.getHeader("ref");
            String[] chain = token.split("/");
            return new UsernamePasswordAuthenticationToken(chain[0], chain[1], List.of(Role.getRole(chain[2])));
        });
        when(refreshTokenProvider.getExpireTime(anyString())).thenReturn(DateTime.now().plusDays(10));
        when(refreshTokenProvider.getRoles(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            String token = httpServletRequest.getHeader("ref");
            String[] chain = token.split("/");
            return List.of(Role.getRole(chain[2]));
        });
        when(refreshTokenProvider.getSubject(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            String token = httpServletRequest.getHeader("ref");
            String[] chain = token.split("/");
            return chain[0];
        });
        when(refreshTokenProvider.resolveToken(any(HttpServletRequest.class))).then(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            return httpServletRequest.getHeader("ref");
        });
        doAnswer(i -> {
            String token = i.getArgument(0);
            HttpServletResponse httpServletResponse = i.getArgument(1);
            httpServletResponse.addHeader("ref", token);
            return null;
        }).when(refreshTokenProvider).writeTokenToResponse(anyString(), any(HttpServletResponse.class));
    }

    public void initInvalidTokenRepository() {
        invalidTokenRepository = mock(InvalidTokenRepository.class);
        when(invalidTokenRepository.save(any(InvalidToken.class))).then(i -> {
            InvalidToken invalidToken = i.getArgument(0);
            tokens.put(invalidToken.getExpireDate(), invalidToken.getToken());
            return null;
        });
    }

    public void initHttpServletRequest() {
        httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString()))
                .thenReturn("test@test.ru" +
                        "/" +
                        "password" +
                        "/" +
                        "ROLE_ADMIN");
    }

    @Test
    void logout() throws TokenException {
        commonAuthService.logout(httpServletRequest);
        assertEquals(2, tokens.entrySet().size());
    }

    @Test
    void refresh() throws TokenException, UserNotFoundException, WrongAuthServiceException {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        commonAuthService.refresh(httpServletRequest, httpServletResponse);
        assertEquals(2, tokens.entrySet().size());
        assertNotNull(httpServletResponse.getHeader("auth"));
        assertNotNull(httpServletResponse.getHeader("ref"));
    }
}