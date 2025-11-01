package com.ozatea.modules.product.presentation

import com.ozatea.modules.product.domain.Product
import com.ozatea.modules.product_media.presentation.ProductMediaResponse
import java.math.BigDecimal
import java.time.Instant

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val slug: String,
    val category: CategorySummary?,
    val isActive: Boolean,
    val mediaList: List<ProductMediaResponse> = emptyList(),
    val skuList: List<SkuResponse> = emptyList(),
    val createdAt: Instant?,
    val updatedAt: Instant?
) {
    fun toProduct() = Product(
        id = id,
        name = name,
        description = description,
        slug = slug,
    )
}

data class CategorySummary(
    val id: Int,
    val name: String,
    val slug: String
)

data class SkuResponse(
    val id: Int,
    val code: String,
    val price: BigDecimal,
    val quantity: Int,
    val isActive: Boolean,
    val properties: List<SkuPropertyResponse> = emptyList()
)

data class SkuPropertyResponse(
    val propertyAttributeId: Long,
    val propertyTitle: String,
    val propertyValue: String
)