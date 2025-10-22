package com.ozatea.modules.user.domain

import com.ozatea.core.constants.AuthProvider
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun findByEmailAndProvider(email: String, provider: AuthProvider): Optional<User>
}
