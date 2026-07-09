package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.users.dto.AuthSignupRequest;
import com.fitu.benefitu.domain.users.dto.BaseInfoDto;
import com.fitu.benefitu.domain.users.dto.UsersSubmitInfoRequest;
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

    //객체생성구현해보기
    public static Users createUsers(AuthSignupRequest request){
        Users users = new Users();
        users.username = request.username();
        users.password = request.password();
        return users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String schoolName;
    private String department;
    private Integer grade;
    private String residence;
    private LocalDate birthDate;
    private Boolean hasDetails;

    public void updateBaseInfo(String schoolName, String department, Integer grade, String residence, String birthDate) {
        this.schoolName = schoolName;
        this.department = department;
        this.grade = grade;
        this.residence = residence;

        this.birthDate = java.time.LocalDate.parse(birthDate);
    }
}
