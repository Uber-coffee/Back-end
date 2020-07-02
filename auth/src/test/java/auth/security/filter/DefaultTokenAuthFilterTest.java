package auth.security.filter;

import auth.entity.Role;
import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.security.token.AccessTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultTokenAuthFilterTest {

    private AccessTokenProvider accessTokenProvider;

    private Authentication auth;

    @BeforeEach
    public void init() throws TokenException, UserNotFoundException {
        accessTokenProvider = mock(AccessTokenProvider.class);
        doAnswer(i -> {
            HttpServletRequest httpServletRequest = i.getArgument(0);
            assertEquals("token", httpServletRequest.getHeader("Authorization"));
            auth = new UsernamePasswordAuthenticationToken("user", "password", List.of(Role.ROLE_ADMIN));
            return null;
        }).when(accessTokenProvider).validateToken(any(HttpServletRequest.class));
        when(accessTokenProvider.getAuthentication(any(HttpServletRequest.class))).thenReturn(auth);
    }

    @Test
    void doFilterInternal() {
    }
}