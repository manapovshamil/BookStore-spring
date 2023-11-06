package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.entities.BlacklistToken;
import com.example.MyBookShopApp.repositories.BlacklistTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtBlacklistService {
    private final BlacklistTokenRepository blacklistRepository;

    public JwtBlacklistService(BlacklistTokenRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    public void addTokenToBlacklist(String token, Date expiration) {
        BlacklistToken blacklistedToken = new BlacklistToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiration(expiration);
        blacklistRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        BlacklistToken blacklistToken = blacklistRepository.findByToken(token);
        return blacklistToken != null;
    }
}
