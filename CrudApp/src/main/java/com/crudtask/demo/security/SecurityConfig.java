package com.crudtask.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.crudtask.demo.service.UserService;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService){

        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        
        httpSecurity.authorizeHttpRequests(configurer->configurer.anyRequest().authenticated())
                    .formLogin(form -> form.loginPage("/showLoginPage").loginProcessingUrl("/authenticateTheUser").permitAll())
                    .rememberMe(rememberMe->rememberMe.key("key").tokenValiditySeconds(300).rememberMeCookieName("remember-me-cookie"))
                    .logout(logout-> logout.permitAll());

        return httpSecurity.build();
    }


}
