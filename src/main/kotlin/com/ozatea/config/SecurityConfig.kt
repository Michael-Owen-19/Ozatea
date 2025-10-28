package com.ozatea.config

import com.ozatea.core.security.JwtTokenFilter
import com.ozatea.modules.user.infrastructure.security.CustomOidcUserService
import com.ozatea.modules.user.infrastructure.security.JwtTokenProvider
import com.ozatea.modules.user.infrastructure.security.OAuth2SuccessHandler
import com.ozatea.modules.user.infrastructure.security.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oAuth2UserService: OAuth2UserService,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val customOidcUserService: CustomOidcUserService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/category").permitAll()
                    .requestMatchers(
                        "/auth/**",
                        "/error",
                    ).permitAll()

                    .anyRequest().authenticated()
            }

            // ✅ OAuth2 login configuration
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { userInfo ->
                        userInfo
                            .oidcUserService(customOidcUserService) // for OpenID providers (like Google)
                            .userService(oAuth2UserService)  }
                    .successHandler(oAuth2SuccessHandler)
                    .failureUrl("/error")
            }

            // ✅ Logout endpoint
            .logout { logout ->
                logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }

            .addFilterBefore(JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
