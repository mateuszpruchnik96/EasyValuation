package com.easyvaluation.security.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    UserAccountService userAccountService;

    @Mock
    UserAccountRepository userAccountRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Test
    void addUserAccountRolesShouldReturnAnEntityWithAddedRole(){
        //given

        UserRole userRole = new UserRole("ROLE_USER");
        UserRole userRoleToAdd = new UserRole("ROLE_ADMIN");
        UserAccount userAccount = new UserAccount("jan", "lokiloki", userRole);
        Set<UserAccount> userAccounts = new HashSet<UserAccount>();
        userAccounts.add(userAccount);
        userRoleToAdd.setUserAccounts(userAccounts);

        given(userRoleRepository.findByName("ROLE_ADMIN")).willReturn(userRoleToAdd);
        given(userAccountRepository.findUserAccountWithUserRolesById(1L)).willReturn(java.util.Optional.of(userAccount));

        //when
        UserAccount entity = userAccountService.addUserAccountRoles(1L,"ROLE_ADMIN");

        //then
        assertThat(entity.getUserRoles(), hasItem(userRoleToAdd));
    }

}