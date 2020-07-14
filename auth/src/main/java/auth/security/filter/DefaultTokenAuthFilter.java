package auth.security.filter;

import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.exception.WrongAuthServiceException;
import auth.security.token.AccessTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultTokenAuthFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;

    public DefaultTokenAuthFilter(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            accessTokenProvider.validateToken(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(accessTokenProvider.getAuthentication(httpServletRequest));
        } catch (TokenException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT.");
            return;
        } catch (UserNotFoundException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.I_AM_A_TEAPOT.value(), "Everything is ok, but user doesn't exists.");
            return;
        } catch (WrongAuthServiceException e){
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.CONFLICT.value(), "Why are you still here??");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
