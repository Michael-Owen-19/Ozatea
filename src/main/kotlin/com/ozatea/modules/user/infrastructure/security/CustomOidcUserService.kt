package com.ozatea.modules.user.infrastructure.security

import com.ozatea.core.constants.AuthProvider
import com.ozatea.modules.user.domain.User
import com.ozatea.modules.user.domain.UserRepository
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOidcUserService(
    private val userRepository: UserRepository
) : OidcUserService() {

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = super.loadUser(userRequest)
        val email = oidcUser.email
        val name = oidcUser.fullName ?: oidcUser.givenName
        val provider = userRequest.clientRegistration.registrationId.uppercase(Locale.getDefault())
        val enumProvider = AuthProvider.valueOf(provider)

        var user = userRepository.findByEmailAndProvider(email, enumProvider).orElse(null)

        if (user == null) {
            val randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8)
            val randomPassword = UUID.randomUUID().toString()

            user = User(
                username = randomUsername,
                email = email,
                password = randomPassword,
                provider = enumProvider,
                name = name
            )

            userRepository.save(user)
        }

        return oidcUser
    }
}
