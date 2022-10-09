package com.easyvaluation.authentication.domain;

import com.easyvaluation.authentication.domain.refreshToken.RefreshToken;
import com.easyvaluation.authentication.domain.refreshToken.RefreshTokenService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class LoginService{

    private final UserAccountService userAccountService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public LoginService(UserAccountService userAccountService, TokenProvider tokenProvider, RefreshTokenService refreshTokenService){
        this.userAccountService = userAccountService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    // Random data generator
    SecureRandom secureRandom = new SecureRandom();

    public String login(UserAccount userAccount) throws EntityNotFoundException{
        System.out.println(userAccount.getLogin());
        System.out.println(userAccount.getPassword());
        boolean isExist = userAccountService.isExist(userAccount).getKey();
        System.out.println(isExist);
        if(isExist == true){
            try {
                userAccount = userAccountService.findByLogin(userAccount);
                System.out.println(userAccount.getId());
            } catch (EntityNotFoundException | NoSuchFieldException e) {
                throw new EntityNotFoundException("Thrown in finding entity by login");
            }
            return "{\"easyValuationToken\": \"" + tokenProvider.createToken(userAccount) + "\", " +
            "\"easyValuationRefreshToken\": \"" + refreshTokenService.createRefreshToken(userAccount).getToken() + "\"}";
        } else {
            throw new EntityNotFoundException("Thrown in isExist method");
        }
    }

    public String refreshLogin(UserAccount userAccount) throws EntityNotFoundException{

            try {
                userAccount = userAccountService.findByLogin(userAccount);
                return "{\"easyValuationToken\": \"" + tokenProvider.createToken(userAccount) + "\", " +
                        "\"easyValuationRefreshToken\": \"" + refreshTokenService.createRefreshToken(userAccount).getToken() + "\"}";
            } catch (EntityNotFoundException | NoSuchFieldException e) {
                throw new EntityNotFoundException("Thrown in finding entity by login in refreshLogin method");
            }

    }

    //Generating tokens for refresh request
    public String generateNewTokensAfterRefresh(String refreshTokenString) throws EntityNotFoundException {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenString);
        UserAccount userAccount = userAccountService.findById(refreshToken.getUserAccount().getId());

        String newTokens = refreshLogin(userAccount);

        return newTokens;
    }

    public String generateFingerprint(){
        //Generate a random string that will constitute the fingerprint for this user
        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);
        String userFingerprint = DatatypeConverter.printHexBinary(randomFgp);

        //Add the fingerprint in a hardened cookie - Add cookie manually because
        //SameSite attribute is not supported by javax.servlet.http.Cookie class
        String fingerprintCookie = "__Secure-Fgp=" + userFingerprint
                + "; SameSite=Strict; HttpOnly; Secure";
        return  fingerprintCookie;
    }

//    public RefreshToken generateRefreshToken(UserAccount userAccount){
//        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userAccount);
////        userAccountService.setRefreshToken(userAccount, newRefreshToken);
//        return newRefreshToken;
//    }
}

