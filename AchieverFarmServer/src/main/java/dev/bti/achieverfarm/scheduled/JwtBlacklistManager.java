package dev.bti.achieverfarm.scheduled;

import dev.bti.achieverfarm.common.Common;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlacklistManager {

    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    @Scheduled(fixedRate = 86_400_000)
    public void clearExpiredTokens() {
        tokenBlacklist.removeIf(Common.Jwt::isTokenExpired);
    }

    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}

