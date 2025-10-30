package com.ozatea.modules.user.application

import com.ozatea.core.enums.AuthProvider
import com.ozatea.core.constants.ErrorMessage
import com.ozatea.modules.user.domain.RefreshToken
import com.ozatea.modules.user.domain.RefreshTokenRepository
import com.ozatea.modules.user.domain.User
import com.ozatea.modules.user.domain.UserRepository
import com.ozatea.modules.user.infrastructure.security.JwtTokenProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun register(username: String, email: String, password: String, name: String): User {
        if (userRepository.findByUsername(username).isPresent)
            throw RuntimeException(ErrorMessage.USERNAME_ALREADY_USED)

        if(userRepository.findByEmailAndProvider(email, AuthProvider.LOCAL).isPresent)
            throw RuntimeException(ErrorMessage.EMAIL_ALREADY_USED)

        val encodedPassword = passwordEncoder.encode(password)
        val user = User(username = username, email = email, password = encodedPassword, name = name)
        return userRepository.save(user)
    }

    @Transactional
    fun login(username: String, password: String): Map<String, String> {
        val user = userRepository.findByUsername(username)
            .orElseThrow { BadCredentialsException(ErrorMessage.INVALID_CREDENTIALS) }

        if (!passwordEncoder.matches(password, user.password))
            throw BadCredentialsException(ErrorMessage.INVALID_CREDENTIALS)

        val accessToken = jwtTokenProvider.createAccessToken(username, user.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(username, user.id)

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
            .orElseThrow { RuntimeException(ErrorMessage.INVALID_TOKEN) }

        if (token.expiryDate.isBefore(Instant.now()))
            throw RuntimeException(ErrorMessage.TOKEN_EXPIRED)

        val user = userRepository.findByUsername(token.username)
            .orElseThrow { BadCredentialsException(ErrorMessage.USER_NOT_FOUND) }

        val accessToken = jwtTokenProvider.createAccessToken(token.username, user.id)
        return mapOf("accessToken" to accessToken)
    }
}
