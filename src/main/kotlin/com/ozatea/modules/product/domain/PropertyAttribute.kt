package com.ozatea.modules.product.domain

import com.ozatea.core.audit.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "property_attribute")
data class PropertyAttribute(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    val property: Property,

    @Column(nullable = false)
    val value: String,

    @Column(name = "is_active")
    val isActive: Boolean = true
) : AuditableEntity()