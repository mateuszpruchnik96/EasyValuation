package com.easyvaluation.security.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class UserAccount extends BaseEntity {
    @Column(unique = true)
    private String login;

    private String firstName;
    private String lastName;

    private String password;

    @Column(unique = true)
    private String email;

    LocalDateTime registrationTime;
    LocalDateTime recentLoginTime;

    @OneToMany(mappedBy = "userAccount", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<UserRole> roles;

    public UserAccount(){
        this.registrationTime = LocalDateTime.now();
        this.roles = new ArrayList<>();
    }
}
