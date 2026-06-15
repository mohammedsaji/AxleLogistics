package com.app.projectlogistics.SecurityFilters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collection;

@Component
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        if(httpServletRequest.getRequestURI().equals("/logistic/auth/signin") ||
                httpServletRequest.getRequestURI().equals("/logistic/auth/signout")||
                httpServletRequest.getRequestURI().startsWith("/views/") ||
                httpServletRequest.getRequestURI().startsWith("/styles/")||
                httpServletRequest.getRequestURI().startsWith("/js/")||
                httpServletRequest.getRequestURI().startsWith("/error")||
                httpServletRequest.getRequestURI().equals("/favicon.ico")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> grantedAuthorityList = authentication.getAuthorities();

        boolean isAdmin = false;

        for(GrantedAuthority grantedAuthority: grantedAuthorityList){
            isAdmin = grantedAuthority.getAuthority().equalsIgnoreCase("ADMIN");
            if(isAdmin){
                break;
            }
        }

        if(isAdmin){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        throw new AccessDeniedException("Insufficient Privilege to perform this action.");
    }
}
