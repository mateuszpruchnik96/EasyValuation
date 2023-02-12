package com.easyvaluation.security.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(exclude = "userAccount")
@NoArgsConstructor
public class UserRole extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "userRoles")
    @JsonIgnore
    private Set<UserAccount> userAccounts;

    private Boolean active;

    public UserRole(String name){
        this.name = name;
    }


}
