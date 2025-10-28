package com.ozatea.modules.product.domain

import com.ozatea.core.audit.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_media")
data class ProductMedia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(name = "media_type", nullable = false)
    val mediaType: String, // "image" or "video"

    @Column(nullable = false)
    val url: String,

    @Column(name = "is_thumbnail")
    val isThumbnail: Boolean = false,

    @Column(name = "is_active")
    val isActive: Boolean = true
) : AuditableEntity()