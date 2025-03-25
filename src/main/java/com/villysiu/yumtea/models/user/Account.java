package com.villysiu.yumtea.models.user;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.*;
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickname;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER )
    @JoinTable
    private Set<Role> roles = new HashSet<>();


    public Account(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}

