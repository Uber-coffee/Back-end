package auth.config.security;

import auth.security.filter.MobileTokenAuthFilter;
import auth.security.token.AccessTokenProvider;
import auth.service.user_details.CustomerDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccessTokenProvider accessTokenProvider;

    private final CustomerDetailsService customerDetailsService;

    public MobileSecurityConfig(AccessTokenProvider accessTokenProvider,
                                CustomerDetailsService customerDetailsService) {
        this.accessTokenProvider = accessTokenProvider;
        this.customerDetailsService = customerDetailsService;
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
                .antMatcher("/w/**")
                .userDetailsService(customerDetailsService)
                .addFilterBefore(new MobileTokenAuthFilter(accessTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    @Bean
    public AuthenticationManager mobileAuthenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerDetailsService);
    }
}
