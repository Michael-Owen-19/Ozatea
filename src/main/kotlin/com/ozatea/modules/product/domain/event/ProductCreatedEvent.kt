package com.ozatea.modules.product.domain.event

import com.ozatea.modules.product.domain.Product

data class ProductCreatedEvent(
    val productId: Long,
    val name: String
) {
    companion object {
        fun from(product: Product) = ProductCreatedEvent(
            productId = product.id,
            name = product.name
        )
    }
}