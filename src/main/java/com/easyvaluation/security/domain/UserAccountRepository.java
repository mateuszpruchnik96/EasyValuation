package com.easyvaluation.security.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query(value = "select c from UserAccount c where c.login = :login and c.password = :password")
    UserAccount findByCriteria(@Param("login") String login, @Param("password") String password);

    @Query(value = "select c from UserAccount c where c.login = :login")
    UserAccount findByLogin(@Param("login") String login);

    @EntityGraph(attributePaths="userRoles")
    Optional<UserAccount> findUserAccountWithUserRolesById(Long userAccountId);

//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE UserAccount user SET user.refreshToken = :refreshToken WHERE user.login = :login")
//    void setRefreshToken(@Param("login") String login, @Param("refreshToken") RefreshToken refreshToken);
}
