package com.app.logistics.auth.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.auth.authUtils.AuthMapper;
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

import java.util.*;

@Service
public class AuthService {

    private final EmployeeService employeeService;
    private final AuthRepo authRepo;
    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final APIKeyGenerator apiKeyGenerator;
    private final BearerTokenBuilder bearerTokenBuilder;
    private final HttpServletResponse httpServletResponse;
    private final ApiCacheCluster apiCacheCluster;

    private final Map<String, Auth> accountVOMap = new HashMap<>();

    public AuthService(EmployeeService employeeService,
                       AuthRepo authRepo,
                       AuthMapper authMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       APIKeyGenerator apiKeyGenerator,
                       BearerTokenBuilder bearerTokenBuilder,
                       HttpServletResponse httpServletResponse,
                       ApiCacheCluster apiCacheCluster) {
        this.employeeService = employeeService;
        this.authRepo = authRepo;
        this.authMapper = authMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.apiKeyGenerator = apiKeyGenerator;
        this.bearerTokenBuilder = bearerTokenBuilder;
        this.httpServletResponse = httpServletResponse;
        this.apiCacheCluster = apiCacheCluster;
    }

    public void userAccountCache(Auth auth) {
        if (auth == null || auth.getAccountUsername() == null) {
            throw new APIException("Cannot cache null or incomplete Auth data", HttpStatus.BAD_REQUEST);
        }
        accountVOMap.put(auth.getAccountUsername(), auth);
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

    public AuthResponse signUpUser(AuthRequest authRequest, AuthDetails authDetails) {
        Auth auth = authMapper.toVO(authRequest);
        auth.setAccountPassword(bCryptPasswordEncoder.encode(authRequest.getAccountPassword()));
        if (authDetails != null) {
            auth.setUpdatedBy(authDetails.getEmployeeId());
        }
        userAccountCache(auth);
        return authMapper.toDTO(auth);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Auth saveUser(String username) {
        Auth cachedAuth = accountVOMap.get(username);
        if (cachedAuth == null) {
            throw new APIException("No pending sign-up data found for user: " + username, HttpStatus.NOT_FOUND);
        }
        Auth savedAuth = authRepo.save(cachedAuth);
        accountVOMap.remove(username);
        return savedAuth;
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