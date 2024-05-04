package com.learning.springsecurity.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String>{
    boolean existsByIdToken(String idToken);
}
