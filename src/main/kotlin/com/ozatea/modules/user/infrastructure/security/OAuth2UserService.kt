package com.ozatea.modules.user.infrastructure.security

import com.ozatea.core.enums.AuthProvider
import com.ozatea.modules.user.domain.User
import com.ozatea.modules.user.domain.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service("customOAuth2UserService")
class OAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val provider = userRequest.clientRegistration.registrationId.uppercase(Locale.getDefault())
        val email = oAuth2User.attributes["email"] as? String
            ?: throw IllegalArgumentException("Email not found in OAuth2 response")
        val name = oAuth2User.attributes["name"] as? String ?: "User"
        println("OAuth2UserService called for provider: $provider, email: $email")

        var user = userRepository.findByEmailAndProvider(email, AuthProvider.valueOf(provider)).orElse(null)

        if (user == null) {
            // 🔹 Generate random username for OAuth users
            val randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8)
            val randomPassword = UUID.randomUUID().toString()

            user = User(
                username = randomUsername,
                email = email,
                password = randomPassword, // no password for OAuth users
                provider = AuthProvider.valueOf(provider),
                name = name
            )

            userRepository.save(user)
        }

        return oAuth2User
    }
}
