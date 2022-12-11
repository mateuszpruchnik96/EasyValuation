package com.easyvaluation.security.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.projects.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true)
    private String email;

    private LocalDateTime registrationTime;
    private LocalDateTime recentLoginTime;

    @OneToMany(mappedBy = "userAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Project> projects;

//    @Enumerated(EnumType.STRING)
//    UserType userType;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_role_id", referencedColumnName = "id"))
    Collection<UserRole> userRoles;

//    private String refreshToken;

    public UserAccount(){
        this.registrationTime = LocalDateTime.now();
//        UserRole adminRole = userRoleRepository.findByName("ROLE_ADMIN");
        this.userRoles = new ArrayList<>();
//        this.userType = UserType.USER;
    }

    public UserAccount(String login, String password){
        this.login = login;
        this.password = password;
        this.registrationTime = LocalDateTime.now();
        this.userRoles = new ArrayList<>();
//        this.userType = UserType.USER;
    }

    public UserAccount(String login, String password, UserRole userRole){
        this.login = login;
        this.password = password;
        this.registrationTime = LocalDateTime.now();
        this.userRoles = new ArrayList<>();
        this.userRoles.add(userRole);
    }

    public void setUserRoles(UserRole...userRoles){
        for(UserRole userRole : userRoles){
            this.userRoles.add(userRole);
        }
    }
}
