package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.users.dto.DetailInfoRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UsersDetails {
    public static UsersDetails createUserDetails(DetailInfoRequest detailInfoRequest, Users users) {
        UsersDetails usersDetails = new UsersDetails();
        usersDetails.users = users;
        usersDetails.gpa = detailInfoRequest.gpa();
        usersDetails.incomeBracket = detailInfoRequest.incomeBracket();
        usersDetails.isBasicLiving = detailInfoRequest.isBasicLiving();
        usersDetails.isSecondLowest = detailInfoRequest.isSecondLowest();
        return usersDetails;
    }

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
