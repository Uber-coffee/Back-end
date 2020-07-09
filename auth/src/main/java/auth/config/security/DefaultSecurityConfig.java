package auth.config.security;

import auth.security.filter.DefaultTokenAuthFilter;
import auth.security.token.AccessTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccessTokenProvider accessTokenProvider;

    public DefaultSecurityConfig(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();

        http.cors();

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