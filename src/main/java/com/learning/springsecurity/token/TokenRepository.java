package com.learning.springsecurity.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>{

    Optional<Token> findByToken(String token);
}
