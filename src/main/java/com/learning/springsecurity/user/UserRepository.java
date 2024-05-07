package com.learning.springsecurity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByEmail(String email);
    List<User> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);
}
