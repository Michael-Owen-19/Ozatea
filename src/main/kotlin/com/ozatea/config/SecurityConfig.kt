package com.ozatea.config

import com.ozatea.core.security.JwtTokenFilter
import com.ozatea.modules.user.infrastructure.security.CustomOidcUserService
import com.ozatea.modules.user.infrastructure.security.JwtTokenProvider
import com.ozatea.modules.user.infrastructure.security.OAuth2SuccessHandler
import com.ozatea.modules.user.infrastructure.security.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
//            .sessionManagement {
//                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }
            .authorizeHttpRequests { auth ->
                auth
                    // Public endpoints (no authentication required)
                    .requestMatchers(
//                        "/products",
                        "/auth/**",
                        "/error",
                    ).permitAll()

                    // Protected endpoints (require authentication)
                    .requestMatchers(
                        "/api/v1/orders/**",
                        "/products"
                    ).authenticated()

                    // Everything else denied by default
                    .anyRequest().denyAll()
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

            // ✅ Use sessions for OAuth2 logins
//            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) }

            // ✅ Logout endpoint
            .logout { logout ->
                logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }

//            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
//            .authorizeHttpRequests {
//                it.requestMatchers("/auth/**", "/oauth2/**").permitAll()
//                    .anyRequest().authenticated()
//            }
//            .oauth2Login { oauth ->
//                oauth
//                    .userInfoEndpoint { it.userService(oAuth2UserService) }
//                    .successHandler(oAuth2SuccessHandler)
//            }
            .addFilterBefore(JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
