package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import com.fitu.benefitu.domain.users.dto.DetailInfoRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class UsersInterests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    private BenefitCategory category;

    public UsersInterests(Users user, BenefitCategory benefitCategory) {
        this.users = user;
        this.category = benefitCategory;
    }

    public static List<UsersInterests> toInterests(DetailInfoRequest.Interests dto, Users user) {

        List<UsersInterests> interests = new ArrayList<>();
        if (Boolean.TRUE.equals(dto.getCorporate())) {
            interests.add(new UsersInterests(user, BenefitCategory.CORPORATE));
        }
        if (Boolean.TRUE.equals(dto.getRegion())) {
            interests.add(new UsersInterests(user, BenefitCategory.REGIONAL));
        }
        if (Boolean.TRUE.equals(dto.getRequirements())) {
            interests.add(new UsersInterests(user, BenefitCategory.CONDITIONAL));
        }
        if (Boolean.TRUE.equals(dto.getState())) {
            interests.add(new UsersInterests(user, BenefitCategory.NATIONAL));

        }
        return interests;
    }
}
