package com.app.projectlogistics.Controller.Authentication;

import com.app.projectlogistics.DataTransferObject.Account.RQTAccountDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Utilities.LoginCredentialDTO;
import com.app.projectlogistics.Service.AuthService;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.validationInterface.OnCreate;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/logistic/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signin")
    public String login(@RequestParam("accountUsername") String accountUsername,
                        @RequestParam("accountPassword") String accountPassword) throws AuthenticationException {
        authService.loginUser(accountUsername, accountPassword);
        return "redirect:/views/dashboard.html";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseMessageDTO signUp(@Validated(OnCreate.class) @RequestBody RQTAccountDTO rqtAccountDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return authService.signUpUser(rqtAccountDTO,userDetails);
    }

    @PostMapping("/signout")
    public String logout(@RequestBody LoginCredentialDTO loginCredentialDTO){
        authService.logoutUser(loginCredentialDTO);
        return "logisticsMenu";
    }

    @GetMapping("/role")
    @ResponseBody
    public ResponseMessageDTO fetchRole(@AuthenticationPrincipal CustomizedUserDetails userDetails){
        return authService.fetchRole(userDetails);
    }
}
