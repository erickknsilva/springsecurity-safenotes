package tech.buildrun.safenotes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlockListService {

    public static final Logger logger = LoggerFactory.getLogger(TokenBlockListService.class);
    private final RedisTemplate<String, String > redisTemplate;


    public TokenBlockListService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void blockToken(String jti, Instant expiresAt){
        long ttl = Duration.between(Instant.now(), expiresAt).getSeconds();
        redisTemplate.opsForValue().set("blocklist:" + jti, "revoked", ttl, TimeUnit.SECONDS);
        logger.info("Enviando para blocklist: {}, revoked {}",jti, ttl);
    }

    public boolean isTokenBlocked(String jti){
        return Boolean.TRUE.equals(redisTemplate.hasKey("blocklist:" + jti));
    }

}
