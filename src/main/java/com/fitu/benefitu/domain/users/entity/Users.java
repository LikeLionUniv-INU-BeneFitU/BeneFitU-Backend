package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.users.dto.AuthSignupRequest;
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
    private Integer gradle;
    private String residence;
    private LocalDate birthDate;
    private Boolean hasDetails;
}
