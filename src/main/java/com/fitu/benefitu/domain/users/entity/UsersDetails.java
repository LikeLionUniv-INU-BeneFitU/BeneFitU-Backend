package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.users.dto.UsersSubmitInfoRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class UsersDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userId;

    private Float gpa;
    private Integer incomeBracket;
    private Boolean isBasicLiving;
    private Boolean isSecondLowest;

    public static UsersDetails createUsersDetails(UsersSubmitInfoRequest request, Users user) {
        UsersDetails details = new UsersDetails();
        details.gpa = request.detailInfo().gpa().floatValue(); // Double -> Float 변환
        details.incomeBracket = request.detailInfo().incomeBracket();
        details.isBasicLiving = request.detailInfo().isBasicLiving();
        details.isSecondLowest = request.detailInfo().isSecondLowest();
        details.userId = user;
        return details;

    }

    public void updateDetailInfo(Double gpa, Integer integer, Boolean basicLiving, Boolean secondLowest) {
    }
}
