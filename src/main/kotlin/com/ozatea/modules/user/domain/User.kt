package com.ozatea.modules.user.domain

import com.ozatea.core.enums.AuthProvider
import com.ozatea.core.constants.RoleConstants
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email", "provider"])])
data class User(
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = false)
    var username: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var provider: AuthProvider = AuthProvider.LOCAL,

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    val role: String = RoleConstants.ROLE_USER,

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null

)
