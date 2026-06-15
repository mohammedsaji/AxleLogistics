package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Account.RQTAccountDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Utilities.LoginCredentialDTO;
import com.app.projectlogistics.Repository.AccountRepo;
import com.app.projectlogistics.Repository.UserDetailServicesRepo.UserDetailServiceImplRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.UtilityFiles.ApiKeyCache;
import com.app.projectlogistics.UtilityFiles.ApiKeyGenerator;
import com.app.projectlogistics.UtilityFiles.JwTokenUtilities;
import com.app.projectlogistics.ValueObject.AccountVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class AuthService {
    private final UserDetailServiceImplRepo userDetailServiceImplRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ApiKeyGenerator apiKeyGenerator;

    private final JwTokenUtilities jwTokenUtilities;

    private final HttpServletResponse httpServletResponse;

    private final ApiKeyCache apiKeyCache;

    private AccountRepo accountRepo;

    Map<String, AccountVO> accountVOMap = new HashMap<>();

    public AuthService(UserDetailServiceImplRepo userDetailServiceImplRepo,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       ApiKeyGenerator apiKeyGenerator,
                       JwTokenUtilities jwTokenUtilities,
                       HttpServletResponse httpServletResponse,
                       ApiKeyCache apiKeyCache,
                       AccountRepo accountRepo) {
        this.userDetailServiceImplRepo = userDetailServiceImplRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.apiKeyGenerator = apiKeyGenerator;
        this.jwTokenUtilities = jwTokenUtilities;
        this.httpServletResponse = httpServletResponse;
        this.apiKeyCache = apiKeyCache;
        this.accountRepo = accountRepo;
    }

    public void userAccountCache(AccountVO accountVO) {

        if (accountVO == null || accountVO.getAccountUsername() == null) {
            throw new IllegalArgumentException("Cannot cache null or incomplete AccountVO data");
        }
        accountVOMap.put(accountVO.getAccountUsername(), accountVO);
    }

    public void loginUser(String accountUsername, String accountPassword) throws AuthenticationException {

        if (accountUsername == null || accountPassword == null) {
            throw new AuthenticationException("Username or Password cannot be empty");
        }

        String apiKey;
        String jwToken;

        AccountVO extractedAccountVO =
                userDetailServiceImplRepo.findByAccountUsername(accountUsername);

        if (extractedAccountVO == null) {
            throw new AuthenticationException("User not found");
        }

        boolean passwordMatches = bCryptPasswordEncoder.matches(accountPassword,
                extractedAccountVO.getAccountPassword());
        System.out.println("username : " + accountUsername);
        System.out.println("password :" + accountPassword);
        if (passwordMatches) {
            System.out.println("matched..");
            apiKey = apiKeyGenerator.generateApiKey();

            apiKeyCache.addKeysInApiCache(extractedAccountVO.getAccountUsername(), Map.of(apiKey, System.currentTimeMillis() + (1000 * 60 * 30)));

            jwToken = jwTokenUtilities.generateJwToken(extractedAccountVO);

            Cookie apiKeyCookie = new Cookie("Api-Key", apiKey);
            apiKeyCookie.setPath("/");
            Cookie jwTokenCookie = new Cookie("Authorization", jwToken);
            jwTokenCookie.setPath("/");
            httpServletResponse.addCookie(jwTokenCookie);
            Cookie usernameCookie = new Cookie("username", extractedAccountVO.getAccountUsername());
            usernameCookie.setPath("/");
            httpServletResponse.addCookie(apiKeyCookie);
            httpServletResponse.addCookie(usernameCookie);
        } else {
            throw new AuthenticationException("UnAuthenticated user.");
        }
    }

    public void logoutUser(LoginCredentialDTO loginCredentialDTO) {

        if (loginCredentialDTO == null || loginCredentialDTO.getUsername() == null) {
            return;
        }

        String userName = loginCredentialDTO.getUsername();
        apiKeyCache.removeKeysInApiCache(userName);
    }

    public AccountVO dtoToVOConverter(String action, RQTAccountDTO rqtAccountDTO, CustomizedUserDetails userDetails) {

        AccountVO accountVO = new AccountVO();
        if (action.equalsIgnoreCase("UPDATE")) {
            accountVO.setAccountId(rqtAccountDTO.getAccountId());
        }
        accountVO.setAccountUsername(rqtAccountDTO.getAccountUsername());
        String bcryptPassword = bCryptPasswordEncoder.encode(rqtAccountDTO.getAccountPassword());
        accountVO.setAccountPassword(bcryptPassword);
        accountVO.setAccountRole(rqtAccountDTO.getAccountRole());
        accountVO.setAccountStatus(rqtAccountDTO.getAccountStatus());
        accountVO.setAccountEmail(rqtAccountDTO.getAccountEmail());
        if (action.equalsIgnoreCase("SAVE")) {
            accountVO.setCreatedAt(LocalDateTime.now());
        } else if (action.equalsIgnoreCase("UPDATE")) {
            accountVO.setCreatedAt(rqtAccountDTO.getCreatedAt());
        }
        accountVO.setUpdatedAt(LocalDateTime.now());
        accountVO.setUpdatedBy(userDetails.getEmployeeId());

        return accountVO;
    }

    public ResponseMessageDTO signUpUser(RQTAccountDTO rqtAccountDTO, CustomizedUserDetails userDetails) {
        userAccountCache(dtoToVOConverter("SAVE", rqtAccountDTO, userDetails));
        String role = rqtAccountDTO.getAccountRole();
        String username = rqtAccountDTO.getAccountUsername();
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setValue("Role", role);
        responseMessageDTO.setValue("Username", username);
        return responseMessageDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccountVO saveUser(String username) {
        AccountVO cachedAccountVO = this.accountVOMap.get(username);
        AccountVO savedAccountVO = accountRepo.save(cachedAccountVO);
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.clearValueMap();
        return savedAccountVO;

    }

    @Transactional(readOnly = true)
    public ResponseMessageDTO fetchRole(CustomizedUserDetails userDetails) {

        List<String> roleList = new ArrayList<>();
        Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities();

        for(GrantedAuthority grantedAuthority : grantedAuthorities){
            roleList.add(grantedAuthority.getAuthority());
        }

        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("Role",roleList.toArray());

        return responseMessageDTO;
    }
}
