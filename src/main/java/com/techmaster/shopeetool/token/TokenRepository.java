package com.techmaster.shopeetool.token;

import com.techmaster.shopeetool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByUser(User user);
}