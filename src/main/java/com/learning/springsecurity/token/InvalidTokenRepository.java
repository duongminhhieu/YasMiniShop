package com.learning.springsecurity.token;

import com.learning.springsecurity.common.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String>{
    boolean existsByIdToken(String idToken);
}
