package auth.config.security;

import auth.security.filter.WebTokenAuthFilter;
import auth.security.token.AccessTokenProvider;
import auth.service.user_details.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(AccessTokenProvider accessTokenProvider, UserDetailsService userDetailsService) {
        this.accessTokenProvider = accessTokenProvider;
        this.userDetailsService = userDetailsService;
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

        http.cors();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .antMatcher("/w/**")
                .userDetailsService(userDetailsService)
                .addFilterBefore(new WebTokenAuthFilter(accessTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    @Bean
    @Primary
    public AuthenticationManager webAuthenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
