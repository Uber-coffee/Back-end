package auth.security.token;

import auth.config.AppProperties;
import auth.entity.Role;
import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.exception.WrongAuthServiceException;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.CustomerDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefreshTokenProviderTest {

    private RefreshTokenProvider tokenProvider;

    private AppProperties appProperties;


    private CustomerDetailsService customerDetailsService;

    private InvalidTokenRepository invalidTokenRepository;

    @BeforeEach
    public void init() {
        initAppProperties();
        initCustomerDetailsService();
        invalidTokenRepository = mock(InvalidTokenRepository.class);
        when(invalidTokenRepository.existsByToken(anyString())).thenReturn(false);
        tokenProvider = new RefreshTokenProvider(
                appProperties,
                invalidTokenRepository,
                customerDetailsService
        );
    }

    public void initAppProperties() {
        appProperties = mock(AppProperties.class);
        when(appProperties.getRefreshToken()).then(i -> {
            AppProperties.RefreshToken token = new AppProperties.RefreshToken();
            token.setSecret("secret");
            token.setExpire(3600L);
            return token;
        });
    }


    public void initCustomerDetailsService() {
        customerDetailsService = mock(CustomerDetailsService.class);
        when(customerDetailsService.loadUserByUsername(anyString())).then(
                i -> org.springframework.security.core.userdetails.User.builder()
                        .username(i.getArgument(0))
                        .password("password")
                        .authorities(List.of(Role.ROLE_CUSTOMER))
                        .disabled(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .accountExpired(false)
                        .build());
    }

    @Test
    void resolveToken() throws TokenException {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer token");
        final String token = tokenProvider.resolveToken(httpServletRequest);
        assertNotNull(token);
    }

    @Test
    void resolveTokenException() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("token");
        assertThrows(TokenException.class, () -> tokenProvider.resolveToken(httpServletRequest));
    }

    @Test
    void validateToken() {
        final String token = Jwts.builder()
                .setSubject("test@test.ru")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + appProperties.getRefreshToken().getExpire()))
                .claim("roles", List.of(Role.ROLE_MANAGER))
                .signWith(SignatureAlgorithm.HS512, appProperties.getRefreshToken().getSecret())
                .compact();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer " + token);
        assertDoesNotThrow(() -> tokenProvider.validateToken(httpServletRequest));
    }

    @Test
    void validateTokenException() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer token");
        assertThrows(TokenException.class, () -> tokenProvider.validateToken(httpServletRequest));
    }

    @Test
    void getRoles() throws TokenException {
        final String token = Jwts.builder()
                .setSubject("test@test.ru")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + appProperties.getRefreshToken().getExpire()))
                .claim("roles", List.of(Role.ROLE_MANAGER))
                .signWith(SignatureAlgorithm.HS512, appProperties.getRefreshToken().getSecret())
                .compact();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer " + token);
        assertArrayEquals(List.of(Role.ROLE_MANAGER.name()).toArray(), tokenProvider.getRoles(httpServletRequest).toArray());
    }

    @Test
    void getRolesException() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer token");
        assertThrows(TokenException.class, () -> tokenProvider.getRoles(httpServletRequest));
    }

    @Test
    void getSubject() throws TokenException {
        final String token = Jwts.builder()
                .setSubject("89500190736")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + appProperties.getRefreshToken().getExpire()))
                .claim("roles", List.of(Role.ROLE_CUSTOMER))
                .signWith(SignatureAlgorithm.HS512, appProperties.getRefreshToken().getSecret())
                .compact();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer " + token);
        assertEquals("89500190736", tokenProvider.getSubject(httpServletRequest));
    }

    @Test
    void getSubjectException() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer token");
        assertThrows(TokenException.class, () -> tokenProvider.getSubject(httpServletRequest));
    }

    @Test
    void createToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "89500190736",
                "",
                List.of(Role.ROLE_CUSTOMER)
        );
        String token = tokenProvider.createToken(auth);
        assertDoesNotThrow(() ->
                Jwts.parser()
                        .setSigningKey(appProperties.getRefreshToken().getSecret())
                        .parseClaimsJws(token));
    }

    @Test
    void writeTokenToResponse() {
        HttpServletResponse response = new MockHttpServletResponse();
        tokenProvider.writeTokenToResponse("token", response);
        assertNotNull(response.getHeader("Refresh-token"));
    }

    @Test
    void getExpireTime() throws TokenException {
        DateTime expected = DateTime.now().plus(appProperties.getRefreshToken().getExpire());
        final String token = Jwts.builder()
                .setSubject("89500190736")
                .setIssuedAt(new Date())
                .setExpiration(new Date(expected.getMillis()))
                .claim("roles", List.of(Role.ROLE_CUSTOMER))
                .signWith(SignatureAlgorithm.HS512, appProperties.getRefreshToken().getSecret())
                .compact();
        final DateTime actual = tokenProvider.getExpireTime(token);
        assertThat(expected.getMillis() - actual.getMillis() == 0);
    }

    @Test
    void getExpireTimeException() {
        assertThrows(TokenException.class, () -> tokenProvider.getExpireTime("token"));
    }

    @Test
    void getAuthentication() throws TokenException, UserNotFoundException, WrongAuthServiceException {
        final String token = Jwts.builder()
                .setSubject("89500190736")
                .setIssuedAt(new Date())
                .setExpiration(new Date(DateTime.now().plus(appProperties.getRefreshToken().getExpire()).getMillis()))
                .claim("roles", List.of(Role.ROLE_CUSTOMER))
                .signWith(SignatureAlgorithm.HS512, appProperties.getRefreshToken().getSecret())
                .compact();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(anyString())).thenReturn("Bearer " + token);
        final Authentication auth = tokenProvider.getAuthentication(request);
        assertEquals("89500190736", auth.getPrincipal());
        assertArrayEquals(List.of(Role.ROLE_CUSTOMER).toArray(), auth.getAuthorities().toArray());
    }
}