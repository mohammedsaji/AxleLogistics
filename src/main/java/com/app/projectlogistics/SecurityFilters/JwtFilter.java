package com.app.projectlogistics.SecurityFilters;

import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.UserDetailServices.UserDetailServiceImpl;
import com.app.projectlogistics.UtilityFiles.JwTokenUtilities;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final UserDetailServiceImpl userDetailService;

    private final JwTokenUtilities jwTokenUtilities;

    public JwtFilter(UserDetailServiceImpl userDetailService,
                     JwTokenUtilities jwTokenUtilities){
        this.userDetailService = userDetailService;
        this.jwTokenUtilities = jwTokenUtilities;
    }

    // Updated to accept a debugReason for better error tracking
    public void throwErrorResponse(HttpServletResponse httpServletResponse, String debugReason) throws IOException {
        System.out.println("❌ [JWT FILTER REJECTED]: " + debugReason);
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        String errorResponse = """
                    {
                  "status":401,
                  "error":"Un-authorized",
                  "message":"[REJECTED BY JWT_FILTER] Session expired or invalid JWT token."
                }""";
        httpServletResponse.getWriter().write(errorResponse);
        httpServletResponse.getWriter().flush();
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)throws IOException, ServletException{

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

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

        Cookie[] cookies = httpServletRequest.getCookies();

        String jwToken = "";
        String username = "";

        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("Authorization".equals(cookie.getName())){
                    jwToken = cookie.getValue();
                }else if(("username").equals(cookie.getName())){
                    username = cookie.getValue();
                }
            }
        }

        if(username == null || jwToken == null || username.trim().isEmpty() || jwToken.trim().isEmpty()){
            throwErrorResponse(httpServletResponse, "Missing or null cookie data.");
            return;
        }

        CustomizedUserDetails employeeDetails = (CustomizedUserDetails) userDetailService.loadUserByUsername(username);

        Boolean isJwTokenValid = jwTokenUtilities.verifyJWToken(username, jwToken);

        if(!isJwTokenValid){
            throwErrorResponse(httpServletResponse, "Invalid JWT token signature or expiration.");
            return;
        }

        UsernamePasswordAuthenticationToken
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                employeeDetails,
                employeeDetails.getPassword(),
                employeeDetails.getAuthorities());

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        securityContextHolder.setAuthentication(usernamePasswordAuthenticationToken);

        String roleFromToken = jwTokenUtilities.getRoleFromToken(jwToken);

        if(roleFromToken.equalsIgnoreCase("ADMIN")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }else{
            if (roleFromToken.equalsIgnoreCase("FEDERATE-MANAGER") ||
                    roleFromToken.equalsIgnoreCase("FEDERATE-DRIVER")) {
                if (httpServletRequest.getRequestURI().equals("/logistic/auth/signin") ||
                        httpServletRequest.getRequestURI().equals("/logistic/auth/signout") ||
                        httpServletRequest.getRequestURI().equals("/logistic/shipment/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/operator/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/driver/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/vehicle/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/shipment/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/status/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/customer/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/cargo/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/operator/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/driver/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/vehicle/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/status/update") ||
                        httpServletRequest.getRequestURI().startsWith("/views/") ||
                        httpServletRequest.getRequestURI().startsWith("/css/") ||
                        httpServletRequest.getRequestURI().startsWith("/js/") ||
                        httpServletRequest.getRequestURI().startsWith("/error") ||
                        httpServletRequest.getRequestURI().equals("/favicon.ico")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    throwErrorResponse(httpServletResponse, "Privilege mismatch: restricted endpoint access.");
                    return;
                }
            }

            if (!roleFromToken.equalsIgnoreCase("ADMIN") ||
                    !roleFromToken.equalsIgnoreCase("FEDERATE-MANAGER") ||
                    !roleFromToken.equalsIgnoreCase("FEDERATE-DRIVER")) {
                if (httpServletRequest.getRequestURI().equals("/logistic/auth/signin") ||
                        httpServletRequest.getRequestURI().equals("/logistic/auth/signout") ||
                        httpServletRequest.getRequestURI().equals("/logistic/employee/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/operator/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/manager/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/driver/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/vehicle/fetch") ||
                        httpServletRequest.getRequestURI().equals("/logistic/shipment/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/operator/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/driver/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/vehicle/fetchall") ||
                        httpServletRequest.getRequestURI().equals("/logistic/employee/update")
                ) {
                    throwErrorResponse(httpServletResponse,"Privilege mismatch: restricted endpoint access.");
                    return;
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}