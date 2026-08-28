package com.app.logistics.auth.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.auth.dto.AuthRequest;
import com.app.logistics.auth.dto.AuthResponse;
import com.app.logistics.common.dto.LoginRequest;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.auth.repo.AuthRepo;
import com.app.logistics.employee.service.EmployeeService;
import com.app.logistics.auth.authUtils.APIKeyGenerator;
import com.app.logistics.auth.authUtils.ApiCacheCluster;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.auth.authUtils.BearerTokenBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {

    private final EmployeeService employeeService;
    private final AuthRepo authRepo;
    private final AccountRepo accountRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final APIKeyGenerator apiKeyGenerator;
    private final BearerTokenBuilder bearerTokenBuilder;
    private final HttpServletResponse httpServletResponse;
    private final ApiCacheCluster apiCacheCluster;

    public AuthService(EmployeeService employeeService,
                       AuthRepo authRepo,
                       AccountRepo accountRepo,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       APIKeyGenerator apiKeyGenerator,
                       BearerTokenBuilder bearerTokenBuilder,
                       HttpServletResponse httpServletResponse,
                       ApiCacheCluster apiCacheCluster) {
        this.employeeService = employeeService;
        this.authRepo = authRepo;
        this.accountRepo = accountRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.apiKeyGenerator = apiKeyGenerator;
        this.bearerTokenBuilder = bearerTokenBuilder;
        this.httpServletResponse = httpServletResponse;
        this.apiCacheCluster = apiCacheCluster;
    }

    public void loginUser(String accountUsername, String accountPassword) {

        if (accountUsername == null || accountPassword == null) {
            throw new APIException("Username or password cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Auth extractedAuth = authRepo.findByAccountUsername(accountUsername);

        if (extractedAuth == null) {
            throw new APIException("User not found", HttpStatus.UNAUTHORIZED);
        }

        boolean passwordMatches = bCryptPasswordEncoder.matches(accountPassword, extractedAuth.getAccountPassword());

        if (!passwordMatches) {
            throw new APIException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        String apiKey = apiKeyGenerator.generateApiKey();
        apiCacheCluster.setAPIKey(extractedAuth.getAccountUsername(), apiKey);

        String jwToken = bearerTokenBuilder.builtBearerToken(extractedAuth.getAccountUsername(), extractedAuth.getAccountRole());

        Cookie jwTokenCookie = new Cookie("Authorization", jwToken);
        jwTokenCookie.setPath("/");

        Cookie apiKeyCookie = new Cookie("Api-Key", apiKey);
        apiKeyCookie.setPath("/");

        Cookie usernameCookie = new Cookie("username", extractedAuth.getAccountUsername());
        usernameCookie.setPath("/");

        httpServletResponse.addCookie(jwTokenCookie);
        httpServletResponse.addCookie(apiKeyCookie);
        httpServletResponse.addCookie(usernameCookie);
    }

    public void logoutUser(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getUsername() == null) {
            throw new APIException("Username cannot be empty", HttpStatus.BAD_REQUEST);
        }
        apiCacheCluster.removeAPIKey(loginRequest.getUsername());
    }

    public Auth dtoToVOConverter(String action, AuthRequest authRequest, CustomizedUserDetails userDetails) {

        Auth auth = new Auth();
        if (action.equalsIgnoreCase("UPDATE")) {
            auth.setAccountId(authRequest.getAccountId());
        }
        auth.setAccountUsername(authRequest.getAccountUsername());
        auth.setAccountPassword(bCryptPasswordEncoder.encode(authRequest.getAccountPassword()));
        auth.setAccountRole(authRequest.getAccountRole());
        auth.setAccountStatus(authRequest.getAccountStatus());
        auth.setAccountEmail(authRequest.getAccountEmail());

        if (action.equalsIgnoreCase("SAVE")) {
            auth.setCreatedAt(LocalDateTime.now());
        } else if (action.equalsIgnoreCase("UPDATE")) {
            auth.setCreatedAt(authRequest.getCreatedAt());
        }
        auth.setUpdatedAt(LocalDateTime.now());
        auth.setUpdatedBy(userDetails.getEmployeeId());

        return auth;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AuthResponse signUpUser(AuthRequest authRequest, AuthDetails authDetails) {
        Auth auth = dtoToVOConverter("SAVE", authRequest, authDetails);
        Auth savedAuth = authRepo.save(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccountUsername(savedAuth.getAccountUsername());
        authResponse.setAccountRole(savedAuth.getAccountRole());
        return authResponse;
    }

    @Transactional(readOnly = true)
    public List<String> fetchRole(AuthDetails authDetails) {
        List<String> roleList = new ArrayList<>();
        Collection<? extends GrantedAuthority> grantedAuthorities = authDetails.getAuthorities();

        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            roleList.add(grantedAuthority.getAuthority());
        }

        return roleList;
    }
}