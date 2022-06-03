package com.easyvaluation.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;


public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query(value = "select c from UserAccount c where c.login = :login and c.password = :password")
    UserAccount findByCriteria(@Param("login") String login, @Param("password") String password);

    @Query(value = "select c from UserAccount c where c.login = :login")
    UserAccount findByLogin(@Param("login") String login);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UserAccount user SET user.refreshToken = :refreshToken WHERE user.login = :login")
    void setRefreshToken(@Param("login") String login, @Param("refreshToken") String refreshToken);
}
