package com.learning.springsecurity.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    @EntityGraph(value = "User.roles")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
