package com.easyvaluation.authentication.domain.refreshToken;

import com.easyvaluation.authentication.domain.SecretKeyConfig;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import com.easyvaluation.security.domain.UserAccountService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private Long refreshTokenDurationMs =  Long.valueOf(2*60*60*1000);

    private RefreshTokenRepository refreshTokenRepository;

    private UserAccountService userAccountService;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserAccountService userAccountService){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userAccountService = userAccountService;
    }

    public RefreshToken findByToken(String token) {
        Optional<RefreshToken>refreshToken = refreshTokenRepository.findByToken(token);
        if(refreshToken != null){
            return refreshToken.stream().findFirst().orElse(null);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public RefreshToken createRefreshToken(UserAccount userAccount) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserAccount(userAccountService.findById(userAccount.getId()));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.deleteByUserAccount(userAccount);

        refreshTokenRepository.save(refreshToken);
        System.out.println(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserAccountId(Long userId) {
        return refreshTokenRepository.deleteByUserAccount(userAccountService.findById(userId));
    }
}