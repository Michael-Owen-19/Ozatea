package com.ozatea.modules.sku.domain

import com.ozatea.core.audit.AuditableEntity
import com.ozatea.modules.product.domain.Product
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "sku")
data class Sku(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false)
    val quantity: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(name = "is_active")
    val isActive: Boolean = true,

    @OneToMany(mappedBy = "sku", cascade = [CascadeType.ALL], orphanRemoval = true)
    val skuProperties: MutableList<SkuProperty> = mutableListOf(),

    ) : AuditableEntity()