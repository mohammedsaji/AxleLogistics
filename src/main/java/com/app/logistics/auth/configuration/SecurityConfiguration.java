package com.app.logistics.auth.configuration;

import com.app.logistics.auth.authFilters.CustomAPI;
import com.app.logistics.auth.authFilters.CustomBearerToken;
import com.app.logistics.auth.authFilters.CustomCORS;
import com.app.logistics.auth.authFilters.CustomCSRF;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final CustomCORS customCORS;
    private final CustomCSRF customCSRF;
    private final CustomAPI customAPI;
    private final CustomBearerToken customBearerToken;

    public SecurityConfiguration(CustomCORS customCORS, CustomCSRF customCSRF, CustomAPI customAPI, CustomBearerToken customBearerToken) {
        this.customCORS = customCORS;
        this.customCSRF = customCSRF;
        this.customAPI = customAPI;
        this.customBearerToken = customBearerToken;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(
                customCORS,
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                customCSRF,
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                customAPI,
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                customBearerToken,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
