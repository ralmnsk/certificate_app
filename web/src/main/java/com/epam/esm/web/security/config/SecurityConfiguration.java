package com.epam.esm.web.security.config;

import com.epam.esm.service.user.UserService;
import com.epam.esm.web.security.jwt.JwtConfigurer;
import com.epam.esm.web.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";
    private static final String TAGS = "/tags/**";
    private static final String CERTIFICATES = "/certificates/**";
    private static final String ORDERS = "/orders/**";
    private static final String USERS = "/users/**";

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    private static final String GUEST = "GUEST";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider,
                                 UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()


                .authorizeRequests()
                .antMatchers(LOGIN).permitAll()
                .antMatchers(REGISTER).permitAll()

                .antMatchers(HttpMethod.GET, TAGS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.POST, TAGS).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, TAGS).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PATCH, TAGS).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, TAGS).hasAnyRole(ADMIN)


                .antMatchers(HttpMethod.GET, CERTIFICATES).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.POST, CERTIFICATES).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, CERTIFICATES).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PATCH, CERTIFICATES).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, CERTIFICATES).hasAnyRole(ADMIN)


                .antMatchers(HttpMethod.GET, ORDERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.POST, ORDERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.PUT, ORDERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.PATCH, ORDERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.DELETE, ORDERS).hasAnyRole(USER, ADMIN)


                .antMatchers(HttpMethod.GET, USERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.GET, "/users").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.POST, USERS).hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, USERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.PATCH, USERS).hasAnyRole(USER, ADMIN)
                .antMatchers(HttpMethod.DELETE, USERS).hasAnyRole(ADMIN)


                .and()
//                .exceptionHandling().accessDeniedHandler(new DeniedHandler())
//                .and()
//                .oauth2Login()
//                .successHandler(new SuccessHandler(jwtTokenProvider, userService))
//                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
        ;
    }
}