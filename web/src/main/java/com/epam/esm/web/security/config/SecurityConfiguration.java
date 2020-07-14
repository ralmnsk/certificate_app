package com.epam.esm.web.security.config;

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


    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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

//                .authorizeRequests()
//                .antMatchers("/**").permitAll()

                .authorizeRequests()
                .antMatchers(HttpMethod.GET, CERTIFICATES).permitAll()
                .antMatchers(LOGIN).permitAll()
                .antMatchers(REGISTER).permitAll()

                .antMatchers(HttpMethod.POST, ORDERS).hasRole(USER)
                .antMatchers(HttpMethod.PUT, ORDERS).hasRole(USER)
                .antMatchers(HttpMethod.GET, ORDERS).hasRole(USER)
                .antMatchers(HttpMethod.GET, TAGS).hasRole(USER)
                .antMatchers(HttpMethod.GET, USERS).hasRole(USER)

                .antMatchers(HttpMethod.PUT, USERS).hasRole(USER)
                .antMatchers(HttpMethod.DELETE, ORDERS).hasRole(USER)

                .anyRequest().hasRole(ADMIN)

                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}