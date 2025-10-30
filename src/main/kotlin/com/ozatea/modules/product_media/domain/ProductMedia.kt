package com.ozatea.modules.product_media.domain

import com.ozatea.core.audit.AuditableEntity
import com.ozatea.modules.media.domain.Media
import com.ozatea.modules.product.domain.Product
import jakarta.persistence.*

@Entity
@Table(name = "product_media")
data class ProductMedia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    val media: Media,

    @Column(name = "is_thumbnail")
    val isThumbnail: Boolean = false,

    @Column(name = "order_index")
    val orderIndex: Int = 0,

    @Column(name = "is_active")
    val isActive: Boolean = true
) : AuditableEntity()
