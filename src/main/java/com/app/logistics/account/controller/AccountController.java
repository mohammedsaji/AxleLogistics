package com.app.logistics.account.controller;

import com.app.logistics.account.dto.AccountRequest;
import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.account.service.AccountService;
import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.dto.LoginRequest;
import com.app.logistics.common.validations.OnCreate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/logistic/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * No try/catch here: a failed login throws APIException, which the
     * global exception handler already turns into a JSON error response
     * for every other endpoint — catching it here just duplicates that,
     * and this method's own redirect-based catch never actually fired
     * because the handler intercepts it first.
     *
     * On success, loginUser() has already set the Authorization,
     * Api-Key, and username cookies via HttpServletResponse. This
     * response body just confirms success so the frontend knows to
     * redirect — the cookies themselves travel automatically.
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        accountService.loginUser(loginRequest.getUsername(), loginRequest.getPassword(), httpServletResponse);

        ApiResponse<String> apiResponse = new ApiResponse.Builder<>(true, "Signed in successfully.")
                .message("User signed in successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        accountService.logOutUser(loginRequest, httpServletResponse);
        ApiResponse<String> apiResponse = new ApiResponse.Builder<>(true, "User signed out.")
                .message("User logged out successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AccountResponse>> signUp(
            @Validated(OnCreate.class) @RequestBody AccountRequest accountRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        AccountResponse accountResponse = accountService.signUpUser(accountRequest, authDetails);

        ApiResponse<AccountResponse> apiResponse = new ApiResponse.Builder<>(true, accountResponse)
                .message("Account details collected successfully and will be created account once the ongoing process is completed.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/user-info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> fetchLoginUserMap(@AuthenticationPrincipal AuthDetails authDetails) {
        Map<String, Object> loginUserMap = accountService.fetchLoginUserMap(authDetails);

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse.Builder<>(true, loginUserMap)
                .message("Role fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(@RequestParam String emailId) {
        Boolean resetMailSent = accountService.forgotPassword(emailId);

        ApiResponse<Boolean> apiResponse = new ApiResponse
                .Builder<Boolean>(true, resetMailSent)
                .message("Check your email Inbox or Spam folder for the password reset.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam String emailId,
                                                             @RequestParam String password,
                                                             @RequestParam String confirmPassword) {
        String pwdUpdated = accountService.resetPassword(emailId, password, confirmPassword);

        ApiResponse<String> apiResponse = new ApiResponse
                .Builder<String>(true, pwdUpdated)
                .message("Password updated successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}