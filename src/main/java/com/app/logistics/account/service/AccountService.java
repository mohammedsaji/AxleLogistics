package com.app.logistics.account.service;

import com.app.logistics.account.repo.AccountRepo;
import com.app.logistics.account.utils.AccountMapper;
import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.account.dto.AccountRequest;
import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.common.dto.LoginRequest;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.auth.authUtils.APIKeyGenerator;
import com.app.logistics.auth.authUtils.ApiCacheCluster;
import com.app.logistics.account.entity.Account;
import com.app.logistics.auth.authUtils.BearerTokenBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import com.app.logistics.common.utils.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final APIKeyGenerator apiKeyGenerator;
    private final BearerTokenBuilder bearerTokenBuilder;
    private final ApiCacheCluster apiCacheCluster;
    private final MailMessage mailMessage;
    private final JavaMailSender javaMailSender;

    private final Map<String, Account> accountVOMap = new HashMap<>();

    public AccountService(
            AccountRepo accountRepo,
            AccountMapper accountMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            APIKeyGenerator apiKeyGenerator,
            BearerTokenBuilder bearerTokenBuilder,
            ApiCacheCluster apiCacheCluster,
            MailMessage mailMessage,
            JavaMailSender javaMailSender) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.apiKeyGenerator = apiKeyGenerator;
        this.bearerTokenBuilder = bearerTokenBuilder;
        this.apiCacheCluster = apiCacheCluster;
        this.mailMessage = mailMessage;
        this.javaMailSender = javaMailSender;
    }

    public void userAccountCache(Account account) {
        if (account == null || account.getAccountUsername() == null) {
            throw new APIException("Cannot cache null or incomplete Auth data", HttpStatus.BAD_REQUEST);
        }
        accountVOMap.put(account.getAccountUsername(), account);
    }

    public void loginUser(String accountUsername, String accountPassword, HttpServletResponse httpServletResponse) {

        if (accountUsername == null || accountPassword == null) {
            throw new APIException("Username or password cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Account extractedAccount = accountRepo.findByAccountUsername(accountUsername);

        if (extractedAccount == null) {
            throw new APIException("User not found", HttpStatus.UNAUTHORIZED);
        }

        boolean passwordMatches = bCryptPasswordEncoder.matches(accountPassword, extractedAccount.getAccountPassword());

        if (!passwordMatches) {
            throw new APIException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        String apiKey = apiKeyGenerator.generateApiKey();
        apiCacheCluster.setAPIKey(extractedAccount.getAccountUsername(), apiKey);

        String jwToken = bearerTokenBuilder.builtBearerToken(extractedAccount.getAccountUsername(), extractedAccount.getAccountRole());

        Cookie jwTokenCookie = new Cookie("AUTH-TOKEN", jwToken);
        jwTokenCookie.setPath("/");

        Cookie apiKeyCookie = new Cookie("X-API-KEY", apiKey);
        apiKeyCookie.setPath("/");

        Cookie usernameCookie = new Cookie("username", extractedAccount.getAccountUsername());
        usernameCookie.setPath("/");

        String csrfToken = UUID.randomUUID().toString();
        Cookie csrfCookie = new Cookie("X-CSRF-TOKEN", csrfToken);
        csrfCookie.setPath("/");
        csrfCookie.setHttpOnly(false);

        httpServletResponse.addCookie(csrfCookie);
        httpServletResponse.addCookie(jwTokenCookie);
        httpServletResponse.addCookie(apiKeyCookie);
        httpServletResponse.addCookie(usernameCookie);
    }

    public void logOutUser(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        if (loginRequest == null || loginRequest.getUsername() == null) {
            throw new APIException("Username cannot be empty", HttpStatus.BAD_REQUEST);
        }
        apiCacheCluster.removeAPIKey(loginRequest.getUsername());

        Cookie jwTokenCookie = new Cookie("AUTH-TOKEN", null);
        jwTokenCookie.setPath("/");

        Cookie apiKeyCookie = new Cookie("X-API-KEY", null);
        apiKeyCookie.setPath("/");

        Cookie usernameCookie = new Cookie("username", null);
        usernameCookie.setPath("/");

        Cookie csrfCookie = new Cookie("X-CSRF-TOKEN", null);
        csrfCookie.setPath("/");
        csrfCookie.setHttpOnly(false);

        httpServletResponse.addCookie(csrfCookie);
        httpServletResponse.addCookie(jwTokenCookie);
        httpServletResponse.addCookie(apiKeyCookie);
        httpServletResponse.addCookie(usernameCookie);

        if(apiCacheCluster.getAPIKey(loginRequest.getUsername()) != null ){
            throw new APIException("Unable to log out, try after sometime.", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public AccountResponse signUpUser(AccountRequest accountRequest, AuthDetails authDetails) {
        Account account = accountMapper.toVO(accountRequest);
        account.setAccountPassword(bCryptPasswordEncoder.encode(accountRequest.getAccountPassword()));
        if (authDetails != null) {
            account.setUpdatedBy(authDetails.getAccount().getAccountId());
        }
        userAccountCache(account);
        return accountMapper.toDTO(account);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Account saveUser(String username) {
        Account cachedAccount = accountVOMap.get(username);
        if (cachedAccount == null) {
            throw new APIException("No pending sign-up data found for user: " + username, HttpStatus.NOT_FOUND);
        }
        Account savedAccount = accountRepo.save(cachedAccount);
        accountVOMap.remove(username);
        return savedAccount;
    }

    @Transactional(readOnly = true)
    public Map<String,Object> fetchLoginUserMap(AuthDetails authDetails) {

        Map<String,Object> loginUserMap = new HashMap<>();
        List<String> roleList = new ArrayList<>();
        Collection<? extends GrantedAuthority> grantedAuthorities = authDetails.getAuthorities();

        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            roleList.add(grantedAuthority.getAuthority());
        }
        loginUserMap.put("userId", authDetails.getAccount().getAccountId());
        loginUserMap.put("username", authDetails.getUsername());
        loginUserMap.put("RoleList", roleList);

        return loginUserMap;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void disableAccount(Integer accountId) {
        if (accountId == null) {
            throw new APIException("Account identifier parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        Account fetchedAccount = accountRepo.findById(accountId).orElseThrow( ()->{
            throw new APIException("Account ID " + accountId + " not found.", HttpStatus.NOT_FOUND);
        });

        fetchedAccount.setAccountStatus("IN-ACTIVE");
        accountRepo.save(fetchedAccount);
    }
    @Transactional(readOnly = true)
    public AccountResponse internalFetchService(Integer accountId){
        Account account = accountRepo.findByAccountId(accountId).orElseThrow( ()-> new APIException("Account ID " + accountId + " not found.", HttpStatus.NOT_FOUND));
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountRole(account.getAccountRole());
        accountResponse.setAccountStatus(account.getAccountStatus());
        accountResponse.setAccountEmail(account.getAccountEmail());
        return accountResponse;
    }

    @Transactional(readOnly = true)
    public boolean resetPassword(String signedUpUserGmailId){
        Account account = accountRepo.findByAccountEmail(signedUpUserGmailId).orElseThrow(()-> new APIException("Provided Email-Id doesn't registered against account.", HttpStatus.BAD_REQUEST));

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            messageHelper.setFrom("althafshajid@gmail.com");
            messageHelper.setTo(account.getAccountEmail());
            messageHelper.setSubject("[Passage Logistic Systems] Please reset your password");
            messageHelper.setText(mailMessage.getResetPasswordContent(),true);
            javaMailSender.send(mimeMessage);
        }catch (MailException mailException) {
            return false;
        }catch(MessagingException messagingException){
            throw new APIException("Unable to construct email message.", HttpStatus.BAD_REQUEST);
        }
        return true;
    }
}