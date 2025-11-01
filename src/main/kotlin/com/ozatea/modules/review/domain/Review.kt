package com.ozatea.modules.review.domain

import com.ozatea.core.audit.AuditableEntity
import com.ozatea.modules.sku.domain.Sku
import com.ozatea.modules.user.domain.User
import jakarta.persistence.*

@Entity
@Table(name = "review")
data class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val rate: Int,

    @Column(nullable = false)
    val text: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    val sku: Sku,

    @Column(name = "is_active")
    val isActive: Boolean = true
) : AuditableEntity()