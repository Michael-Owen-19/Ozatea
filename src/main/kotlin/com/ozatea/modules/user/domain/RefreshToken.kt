package com.ozatea.modules.user.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "refresh_token")
data class RefreshToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val expiryDate: Instant
)
