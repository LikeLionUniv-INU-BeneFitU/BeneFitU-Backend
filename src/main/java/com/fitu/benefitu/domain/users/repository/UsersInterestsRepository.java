package com.fitu.benefitu.domain.users.repository;

import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersInterestsRepository extends JpaRepository<UsersInterests, Long> {
    List<UsersInterests> findByUsers(Users users);
}
