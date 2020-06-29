package auth.security.token;

import auth.config.AppProperties;
import auth.entity.Role;
import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.repository.InvalidTokenRepository;
import auth.service.user_details.UserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.joda.time.DateTime;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public abstract class TokenProvider {

    protected final String HEADER;

    protected final String TOKEN_TYPE;

    private final InvalidTokenRepository invalidTokenRepository;

    private final AppProperties.Token tokenProperties;

    private final UserDetailsService userDetailsService;

    protected TokenProvider(String header,
                            String tokenType,
                            InvalidTokenRepository invalidTokenRepository,
                            AppProperties.Token tokenProperties,
                            UserDetailsService userDetailsService) {
        HEADER = header;
        TOKEN_TYPE = tokenType;
        this.invalidTokenRepository = invalidTokenRepository;
        this.tokenProperties = tokenProperties;
        this.userDetailsService = userDetailsService;
    }

    public String resolveToken(HttpServletRequest httpServletRequest) throws TokenException {
        final String token = httpServletRequest.getHeader(HEADER);
        if (token != null && token.startsWith(TOKEN_TYPE + " ")) {
            return token.substring(TOKEN_TYPE.length() + 1);
        }
        throw new TokenException();
    }

    public void validateToken(HttpServletRequest httpServletRequest) throws TokenException {
        final String token = resolveToken(httpServletRequest);
        if (invalidTokenRepository.existsByToken(token)) throw new TokenException();
        try {
            Jwts.parser()
                    .setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException |
                IllegalArgumentException |
                SignatureException |
                MalformedJwtException |
                UnsupportedJwtException e) {
            throw new TokenException();
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(HttpServletRequest httpServletRequest) throws TokenException {
        final String token = resolveToken(httpServletRequest);
        try {
            return (List<String>) Jwts.parser()
                    .setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles");
        } catch (ExpiredJwtException |
                IllegalArgumentException |
                SignatureException |
                MalformedJwtException |
                UnsupportedJwtException e) {
            throw new TokenException();
        }
    }

    public String getSubject(HttpServletRequest httpServletRequest) throws TokenException {
        final String token = resolveToken(httpServletRequest);
        try {
            return Jwts.parser()
                    .setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException |
                IllegalArgumentException |
                SignatureException |
                MalformedJwtException |
                UnsupportedJwtException e) {
            throw new TokenException();
        }
    }

    public String createToken(Authentication auth) {
        return Jwts.builder()
                .setSubject(auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenProperties.getExpire()))
                .claim("roles", auth.getAuthorities())
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getSecret())
                .compact();
    }

    public void writeTokenToResponse(String token, HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader(HEADER, TOKEN_TYPE + " " + token);
    }

    public DateTime getExpireTime(String accessToken) throws TokenException {
        try {
            Date expire = Jwts.parser()
                    .setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getExpiration();
            return new DateTime(expire);
        } catch (ExpiredJwtException |
                IllegalArgumentException |
                SignatureException |
                MalformedJwtException |
                UnsupportedJwtException e) {
            throw new TokenException();
        }
    }

    public Authentication getAuthentication(HttpServletRequest httpServletRequest)
            throws TokenException, UserNotFoundException {
        final String subject = getSubject(httpServletRequest);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            return new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
