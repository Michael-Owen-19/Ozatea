package com.ozatea.modules.product.domain

import com.ozatea.core.audit.AuditableEntity
import com.ozatea.modules.category.domain.Category
import com.ozatea.modules.product_media.domain.ProductMedia
import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    var slug: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category? = null,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val mediaList: MutableList<ProductMedia> = mutableListOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val skuList: MutableList<Sku> = mutableListOf()
) : AuditableEntity()
