package com.easyvaluation.authentication.domain.refreshToken;

import com.easyvaluation.security.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken findByUserAccount(UserAccount userAccount);

    @Modifying
    @Transactional(readOnly=false)
    int deleteByUserAccount(UserAccount userAccount);
}