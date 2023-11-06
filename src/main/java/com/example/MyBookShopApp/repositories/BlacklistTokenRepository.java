package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Integer> {
    BlacklistToken findByToken(String token);
}
