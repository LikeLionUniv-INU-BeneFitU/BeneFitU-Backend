package com.fitu.benefitu.domain.users.mapper;

import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import com.fitu.benefitu.domain.users.dto.BaseInfoDto;
import com.fitu.benefitu.domain.users.dto.DetailInfoRequest;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUsers(BaseInfoDto dto, @MappingTarget Users users);

    UsersDetails toUserDetails(DetailInfoRequest dto, @MappingTarget UsersDetails details);

    default List<UsersInterests> toInterests(DetailInfoRequest.Interests dto, Users user) {
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
