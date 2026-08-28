package com.app.logistics.auth.authFilters;//package com.backend.ops.platform.auth.filters;

import com.backend.ops.platform.common.exception.APIException;
import com.backend.ops.platform.common.util.ApiCacheCluster;import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CustomAPI extends OncePerRequestFilter {
    private final ApiCacheCluster apiCacheCluster;

    public CustomAPI(ApiCacheCluster apiCacheCluster) {
        this.apiCacheCluster = apiCacheCluster;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (servletPath.equals("/api/auth/login")
                ||servletPath.equals("/actuator/health")
                || servletPath.startsWith("/swagger-ui/")
                || servletPath.startsWith("/v3/api-docs/")) {

            filterChain.doFilter(request, response);
            return;
        }

        String username = request.getHeader("Username");
        String apiKey = request.getHeader("X-API-KEY");

        Map<String, LocalDateTime> cachedApiKey = apiCacheCluster.getAPIKey(username);

        if (cachedApiKey != null && !cachedApiKey.isEmpty()) {
            if (cachedApiKey.containsKey(apiKey)) {
                LocalDateTime expirationTime = cachedApiKey.get(apiKey);
                if (LocalDateTime.now().isBefore(expirationTime)) {
                    filterChain.doFilter(request,response);
                } else {
                    throw new APIException("API key Expired.", HttpStatus.FORBIDDEN);
                }
            } else {
                throw new APIException("API Key provided could be invalid.", HttpStatus.FORBIDDEN);
            }
        } else {
            throw new APIException("API Key provided could be null.", HttpStatus.FORBIDDEN);
        }
    }
}
