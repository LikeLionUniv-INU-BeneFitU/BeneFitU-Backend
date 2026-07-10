package com.fitu.benefitu.domain.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class UsersDetails {

    public void setOwner(Users user) {
        this.users = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;            // 사용자 아이디

    private Float gpa;              // 학점
    private Integer incomeBracket;  // 소득 분위
    private Boolean isBasicLiving;  // 기초 생활 수급자 여부
    private Boolean isSecondLowest; // 차상위 계층 여부
}
