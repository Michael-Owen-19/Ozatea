package com.ozatea.modules.property.domain

import com.ozatea.core.audit.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "property")
data class Property(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val slug: String,

    @Column(name = "is_active")
    val isActive: Boolean = true
) : AuditableEntity()