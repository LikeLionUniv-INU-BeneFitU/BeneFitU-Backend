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
public class UsersInterests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userId;

    private String category;

    public static UsersInterests createUsersInterests(UsersSubmitInfoRequest request, Users user) {
        UsersInterests interests = new UsersInterests();
        interests.userId = user;

        // 💡 DTO의 InterestsDto 내용을 쉼표 등으로 구분된 하나의 문자열(String)로 변환하여 category에 저장
        // 예시: "corporate, region, state" 형태로 묶기
        String categoryString = "Corporate: " + request.detailInfo().Interests().corporate() +
                ", Region: " + request.detailInfo().Interests().region() +
                ", Requirements: " + request.detailInfo().Interests().requirements() +
                ", State: " + request.detailInfo().Interests().state();

        interests.category = categoryString;

        return interests;

    }
}
