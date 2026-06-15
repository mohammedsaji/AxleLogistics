package com.app.projectlogistics.Configuration;

import com.app.projectlogistics.SecurityFilters.ApiFilter;
import com.app.projectlogistics.SecurityFilters.JwtFilter;
import com.app.projectlogistics.SecurityFilters.RoleFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiFilter apiFilter;

    private final JwtFilter jwtFilter;

    private final RoleFilter roleFilter;

    public SecurityConfig(ApiFilter apiFilter,
                          JwtFilter jwtFilter,
                          RoleFilter roleFilter){

        this.apiFilter = apiFilter;
        this.jwtFilter = jwtFilter;
        this.roleFilter = roleFilter;

    }

    @Bean
    @Scope("singleton")
    @Profile("dev")
    @ConditionalOnMissingBean
    @Lazy
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope("singleton")
    @Lazy
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf->csrf.disable());

        httpSecurity.formLogin(form->form.disable());
        httpSecurity.httpBasic(basic->basic.disable());


        httpSecurity.authorizeHttpRequests(auth->
                auth.requestMatchers("/logistic/auth/signin","/logistic/auth/signout","/views/**","/styles/**","/js/**","/error","/favicon.ico").permitAll().anyRequest().authenticated());




        httpSecurity.addFilterBefore(apiFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(jwtFilter,ApiFilter.class);
        httpSecurity.addFilterAfter(roleFilter,JwtFilter.class);
        return httpSecurity.build();
    }
}
