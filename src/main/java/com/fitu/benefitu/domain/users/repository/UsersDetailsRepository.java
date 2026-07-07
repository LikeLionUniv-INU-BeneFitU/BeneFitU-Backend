package com.fitu.benefitu.domain.users.repository;

import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersDetailsRepository extends JpaRepository<UsersDetails, Long> {
    Optional<UsersDetails> findByUserId(Users user);
}