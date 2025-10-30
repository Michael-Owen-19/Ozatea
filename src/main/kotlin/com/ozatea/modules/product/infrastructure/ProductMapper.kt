package com.ozatea.modules.product.infrastructure

import com.ozatea.modules.category.domain.Category
import com.ozatea.modules.product.domain.Product
import com.ozatea.modules.product.presentation.*
import com.ozatea.modules.product_media.presentation.ProductMediaResponse

object ProductMapper {

    fun toEntity(request: ProductRequest, category: Category?): Product {
        val product = Product(
            name = request.name,
            description = request.description,
            slug = request.slug,
            category = category
        )
        return product
    }

    fun toResponse(product: Product): ProductResponse {
        return ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            slug = product.slug,
            category = product.category?.let {
                CategorySummary(
                    id = it.id,
                    name = it.name,
                    slug = it.slug,
                )
            },
            isActive = product.isActive,
            mediaList = product.mediaList.map { media ->
                ProductMediaResponse(
                    id = media.id,
                    productId = media.product.id,
                    mediaId = media.media.id,
                    filepath = media.media.filepath,
                    filename = media.media.filename,
                    originalFilename = media.media.originalFilename,
                    mimeType = media.media.mimeType,
                    mediaType = media.media.mediaType,
                    altText = media.media.altText,
                    isThumbnail = media.isThumbnail,
                    isActive = media.isActive
                )
            },
            skuList = product.skuList.map { sku ->
                SkuResponse(
                    id = sku.id,
                    code = sku.code,
                    price = sku.price,
                    quantity = sku.quantity,
                    isActive = sku.isActive,
                    properties = sku.skuProperties.map{
                        val attr = it.propertyAttribute
                        SkuPropertyResponse(
                            propertyAttributeId = attr.id,
                            propertyTitle = attr.property.name,
                            propertyValue = attr.value,
                        )
                    }
                )
            },
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}