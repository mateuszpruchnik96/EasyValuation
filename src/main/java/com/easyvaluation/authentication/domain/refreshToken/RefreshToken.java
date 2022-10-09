package com.easyvaluation.authentication.domain.refreshToken;

import com.easyvaluation.security.domain.UserAccount;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.Instant;

import javax.persistence.*;

@Entity(name = "refreshtoken")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    @PrimaryKeyJoinColumn
//    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = false)
    @OneToOne
    @JoinColumn(name = "userAccount_id", referencedColumnName = "id")
    private UserAccount userAccount;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public UserAccount getUserAccount() {
        return userAccount;
    }
}