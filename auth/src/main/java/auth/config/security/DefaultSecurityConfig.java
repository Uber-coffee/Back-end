package auth.config.security;

import auth.security.filter.DefaultTokenAuthFilter;
import auth.security.token.AccessTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
public class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {


    public static final String[] WHITE_LIST = {
            "/w/auth/login",
            "/m/auth/login",
            "/m/auth/signup",
            "/auth/refresh",
            "/configuration/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    private final AccessTokenProvider accessTokenProvider;

    public DefaultSecurityConfig(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(WHITE_LIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .antMatcher("/**")
                .addFilterBefore(new DefaultTokenAuthFilter(accessTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}