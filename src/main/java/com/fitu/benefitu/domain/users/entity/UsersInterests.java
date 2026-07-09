package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.users.dto.InterestsDto;
import com.fitu.benefitu.domain.users.dto.UsersSubmitInfoRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UsersInterests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    private String category;

    public static UsersInterests createUsersInterests(UsersSubmitInfoRequest request, Users user) {
        UsersInterests interests = new UsersInterests();
        interests.user = user;

        InterestsDto interestsDto = request.detailInfo().Interests();
        String categoryString = "Corporate: " + (interestsDto != null ? interestsDto.corporate() : false) +
                ", Region: " + (interestsDto != null ? interestsDto.region() : false) +
                ", Requirements: " + (interestsDto != null ? interestsDto.requirements() : false) +
                ", State: " + (interestsDto != null ? interestsDto.state() : false);

        interests.category = categoryString;

        return interests;

    }
}
