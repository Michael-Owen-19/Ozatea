package com.ozatea.core.security

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class SpringSecurityAuditorAware : AuditorAware<UUID> {
    override fun getCurrentAuditor(): Optional<UUID> {
        val ctx = SecurityContextHolder.getContext()
        val auth: Authentication? = ctx.authentication

        if (auth == null || !auth.isAuthenticated) {
            return Optional.empty()
        }

        return when (val principal = auth.principal) {
            is UserPrincipal -> Optional.of(principal.id)
            is UserDetails -> {
                // If your UserDetails doesn't include id, return empty
                Optional.empty()
            }
            is String -> {
                // In some cases principal is username String (for basic auth). If you can map username -> id here, do it.
                Optional.empty()
            }
            else -> Optional.empty()
        }
    }
}