package com.ozatea.modules.user.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ozatea.core.constants.AuthProvider
import com.ozatea.modules.user.domain.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        if (authentication is OAuth2AuthenticationToken) {
            val principal = authentication.principal as OAuth2User
            val email = principal.attributes["email"] as String
            val provider = authentication.authorizedClientRegistrationId.uppercase()
            val providerEnum = AuthProvider.valueOf(provider)

            val user = userRepository.findByEmailAndProvider(email, provider = providerEnum).orElseThrow {
                RuntimeException("User not found after OAuth login")
            }

            val accessToken = jwtTokenProvider.createAccessToken(user.username)
            val refreshToken = jwtTokenProvider.createRefreshToken(user.username)

            val tokens = mapOf(
                "accessToken" to accessToken,
                "refreshToken" to refreshToken,
                "message" to "OAuth2 login successful"
            )

            response.contentType = "application/json"
            response.writer.write(ObjectMapper().writeValueAsString(tokens))
        } else {
            throw RuntimeException("Authentication is not of type OAuth2AuthenticationToken")
        }
    }
}
