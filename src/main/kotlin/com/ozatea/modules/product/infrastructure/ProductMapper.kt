package com.ozatea.modules.product.infrastructure

import com.ozatea.modules.product.domain.Product
import com.ozatea.modules.product.presentation.ProductRequest
import com.ozatea.modules.product.presentation.ProductResponse

object ProductMapper {

    fun toEntity(request: ProductRequest): Product {
        val product = Product(
            name = request.name,
            description = request.description,
            basePrice = request.basePrice,
            imageUrl = request.imageUrl
        )
        return product
    }

    fun toResponse(product: Product): ProductResponse {
        return ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            basePrice = product.basePrice,
            imageUrl = product.imageUrl,
        )
    }
}