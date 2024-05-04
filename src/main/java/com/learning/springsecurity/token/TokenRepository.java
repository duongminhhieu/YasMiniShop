package com.learning.springsecurity.token;

import com.learning.springsecurity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>{

    List<Token> findByRevokedAndExpiredAndUser(boolean revoked, boolean expired, User user);
    Optional<Token> findByToken(String token);
}
