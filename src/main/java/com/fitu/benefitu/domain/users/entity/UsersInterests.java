package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
