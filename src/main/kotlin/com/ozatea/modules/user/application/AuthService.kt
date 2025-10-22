package com.ozatea.modules.user.application

import com.ozatea.modules.user.domain.RefreshToken
import com.ozatea.modules.user.domain.RefreshTokenRepository
import com.ozatea.modules.user.domain.User
import com.ozatea.modules.user.domain.UserRepository
import com.ozatea.modules.user.infrastructure.security.JwtTokenProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(username: String, email: String, password: String, name: String): User {
        if (userRepository.findByUsername(username).isPresent)
            throw RuntimeException("User already exists")

        val encodedPassword = passwordEncoder.encode(password)
        val user = User(username = username, email = email, password = encodedPassword, name = name)
        return userRepository.save(user)
    }

    fun login(username: String, password: String): Map<String, String> {
        val user = userRepository.findByUsername(username)
            .orElseThrow { BadCredentialsException("Username or password is incorrect") }

        if (!passwordEncoder.matches(password, user.password))
            throw BadCredentialsException("Username or password is incorrect")

        val accessToken = jwtTokenProvider.createAccessToken(username)
        val refreshToken = jwtTokenProvider.createRefreshToken(username)

        // Remove old tokens
        refreshTokenRepository.deleteByUsername(username)

        refreshTokenRepository.save(
            RefreshToken(
                token = refreshToken,
                username = username,
                expiryDate = Instant.now().plusSeconds(604800)
            )
        )

        return mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
    }

    fun refresh(refreshToken: String): Map<String, String> {
        val token = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow { RuntimeException("Invalid refresh token") }

        if (token.expiryDate.isBefore(Instant.now()))
            throw RuntimeException("Refresh token expired")

        val accessToken = jwtTokenProvider.createAccessToken(token.username)
        return mapOf("accessToken" to accessToken)
    }
}
