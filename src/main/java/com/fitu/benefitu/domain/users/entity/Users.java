package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.users.dto.BaseInfoDto;
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
        users.hasDetails = false;
        return users;
    }

    public void toSubmittedUsers(BaseInfoDto baseInfoDto) {
        this.name = baseInfoDto.name();
        this.schoolName= baseInfoDto.schoolName();
        this.department= baseInfoDto.department();
        this.grade = baseInfoDto.grade();
        this.residence = baseInfoDto.residence();
        this.birthDate = baseInfoDto.birthDate();
        this.hasDetails = true;
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
