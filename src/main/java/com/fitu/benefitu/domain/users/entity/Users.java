package com.fitu.benefitu.domain.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Users {

    public static Users createUsers(String username, String password) {
        Users users = new Users();
        users.username = username;
        users.password = password;
        return users;
    }

    public void setUserIdANDNameAndPassword(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void setUsersHasDetails(boolean hasDetails) {
        this.hasDetails = hasDetails;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;    // 로그인 ID
    private String password;    // 암호화된 비밀번호
    private String name;        // 사용자 이름
    private String schoolName;  // 학교 이름
    private String department;  // 전공 이름
    private Integer grade;      // 학년
    private String residence;   // 거주 지역
    private LocalDate birthDate;// 생년월일
    private Boolean hasDetails; // 사용자 세부 정보 유무
}
