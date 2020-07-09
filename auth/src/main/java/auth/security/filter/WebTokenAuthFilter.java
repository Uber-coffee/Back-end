package auth.security.filter;

import auth.entity.Role;
import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.security.token.AccessTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public class WebTokenAuthFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;

    public WebTokenAuthFilter(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            accessTokenProvider.validateToken(httpServletRequest);
            final List<String> roles = accessTokenProvider.getRoles(httpServletRequest);
            if (roles.contains(Role.ROLE_CUSTOMER.name())) throw new AccessDeniedException("");
            SecurityContextHolder.getContext().setAuthentication(accessTokenProvider.getAuthentication(httpServletRequest));
        } catch (TokenException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT.");
            return;
        } catch (AccessDeniedException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "You don't have permission.");
            return;
        } catch (UserNotFoundException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.I_AM_A_TEAPOT.value(), "Everything is ok, but user doesn't exists.");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
