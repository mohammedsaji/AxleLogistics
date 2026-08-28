package com.app.logistics.auth.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.auth.dto.AuthRequest;
import com.app.logistics.auth.dto.AuthResponse;
import com.app.logistics.auth.dto.RoleResponse;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.dto.LoginRequest;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.auth.service.AuthService;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.common.validations.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/logistic/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public String login(@RequestParam("accountUsername") String accountUsername,
                        @RequestParam("accountPassword") String accountPassword,
                        RedirectAttributes redirectAttributes) {
        try {
            authService.loginUser(accountUsername, accountPassword);
            return "redirect:/views/dashboard.html";
        } catch (APIException | AuthenticationException ex) {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/views/login.html";
        }
    }

    @PostMapping("/signout")
    public String logout(@RequestBody LoginRequest loginRequest, RedirectAttributes redirectAttributes) {
        try {
            authService.logoutUser(loginRequest);
            return "logisticsMenu";
        } catch (APIException ex) {
            redirectAttributes.addFlashAttribute("error", "Could not sign out. Please try again.");
            return "logisticsMenu";
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(
            @Validated(OnCreate.class) @RequestBody AuthRequest authRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        AuthResponse authResponse = authService.signUpUser(authRequest, authDetails);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse
                .Builder<AuthResponse>(true, authResponse)
                .message("Account created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/role")
    public ResponseEntity<ApiResponse<RoleResponse>> fetchRole(@AuthenticationPrincipal AuthDetails authDetails) {
        RoleResponse roleResponse = authService.fetchRole(authDetails);

        ApiResponse<RoleResponse> apiResponse = new ApiResponse
                .Builder<RoleResponse>(true, roleResponse)
                .message("Role fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}