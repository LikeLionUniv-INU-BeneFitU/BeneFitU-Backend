package com.fitu.benefitu.domain.users.repository;


import com.fitu.benefitu.domain.users.entity.UsersInterests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersInterestsRepository extends JpaRepository<UsersInterests,Long> {
}
