package com.ozatea.modules.user.infrastructure

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val expirationMs: Long = 3600000 // 1 hour

    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun extractEmail(token: String): String? =
        getClaims(token).subject

    fun isTokenValid(token: String, userEmail: String): Boolean {
        val email = extractEmail(token)
        return email == userEmail && !isExpired(token)
    }

    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

    private fun isExpired(token: String): Boolean =
        getClaims(token).expiration.before(Date())
}
