package com.ozatea.modules.user.infrastructure.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor("supersecretkey_change_thissupersecretkey_change_this".toByteArray()) //todo use application.properties or yml
    private val validityInMs = 3600000 // 1 hour
    private val refreshValidityInMs = 604800000 // 7 days

    fun createAccessToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + validityInMs)

        val claims = Jwts.claims().setSubject(username)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createRefreshToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + refreshValidityInMs)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean = try {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)

        !claims.body.expiration.before(Date())
    } catch (e: Exception) {
        false
    }

    fun getUsername(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
}