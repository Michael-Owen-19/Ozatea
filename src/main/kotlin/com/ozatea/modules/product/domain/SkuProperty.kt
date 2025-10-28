package com.ozatea.modules.product.domain

import com.ozatea.core.audit.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "sku_property")
data class SkuProperty(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    val sku: Sku,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_attribute_id")
    val propertyAttribute: PropertyAttribute
) : AuditableEntity()