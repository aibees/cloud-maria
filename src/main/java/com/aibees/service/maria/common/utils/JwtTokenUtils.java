package com.aibees.service.maria.common.utils;

import java.sql.Date;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenUtils {

    private final Long accessExpireTime = 3600L;

    public String createAccessToken() {
        // create claims
        Claims claims = Jwts.claims();

        // create zonedTime
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime validTime = now.plusSeconds(accessExpireTime);

        return Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(validTime.toInstant()))
            .compact();
    }
}
