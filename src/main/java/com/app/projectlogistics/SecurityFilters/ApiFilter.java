    package com.app.projectlogistics.SecurityFilters;

    import com.app.projectlogistics.UtilityFiles.ApiKeyCache;
    import jakarta.servlet.*;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.stereotype.Component;

    import java.io.IOException;
    import java.util.Map;

    @Component
    public class ApiFilter implements Filter {

        private final ApiKeyCache apiKeyCache;

        public ApiFilter(ApiKeyCache apiKeyCache){
            this.apiKeyCache = apiKeyCache;
        }


        public void throwErrorResponse(HttpServletResponse httpServletResponse) throws IOException {
            httpServletResponse.setStatus(401);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            String errorResponse = """
                      {
                    "status":401,
                    "error":"Un-authorized",
                    "message":"[REJECTED BY API_FILTER] Api Key mismatch or unauthorized access."
                  }""";
            httpServletResponse.getWriter().write(errorResponse);
            httpServletResponse.getWriter().flush();
        }

        public void doFilter(ServletRequest servletRequest,
                             ServletResponse servletResponse,
                             FilterChain filterChain)throws IOException, ServletException
        {


            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

            System.out.println("👉 TARGET URL HITTING FILTER: " + httpServletRequest.getRequestURI());

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

            String employeeDedicatedApiKey = "";
            String username = "";

            if(cookies!=null){
                for(Cookie cookie:cookies){
                    if("Api-Key".equals(cookie.getName())){
                        employeeDedicatedApiKey = cookie.getValue();
                    }else if(("username").equals(cookie.getName())){
                        username = cookie.getValue();
                    }
                }
            }

            Map<String,Long> empApiKey = apiKeyCache.getApiCache(username);
            if(empApiKey != null){
                Long apiKeyExpirationTime = empApiKey.get(employeeDedicatedApiKey);

                if(empApiKey.get(employeeDedicatedApiKey) != null && empApiKey.containsKey(employeeDedicatedApiKey)) {
                    if (System.currentTimeMillis()>apiKeyExpirationTime) {
                        throwErrorResponse(httpServletResponse);
                        return;
                    } else if (System.currentTimeMillis()<apiKeyExpirationTime) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            } else{
                throwErrorResponse(httpServletResponse);
                return;
            }
        }
    }
