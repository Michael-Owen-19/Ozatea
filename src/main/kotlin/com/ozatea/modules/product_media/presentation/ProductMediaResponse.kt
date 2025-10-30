package com.ozatea.modules.product_media.presentation

import com.ozatea.core.enums.MediaType
import com.ozatea.modules.product_media.domain.ProductMedia

data class ProductMediaResponse(
    val id: Long,
    val productId: Long,
    val mediaId: Long?,
    val filepath: String,
    val filename: String,
    val originalFilename: String,
    val altText: String?,
    val mimeType: String,
    val mediaType: MediaType,
    val isThumbnail: Boolean,
    val isActive: Boolean
) {
    companion object {
        fun from(productMedia: ProductMedia) = ProductMediaResponse(
            id = productMedia.id,
            productId = productMedia.product.id,
            mediaId = productMedia.media.id,
            filepath = productMedia.media.filepath,
            filename = productMedia.media.filename,
            originalFilename = productMedia.media.originalFilename,
            altText = productMedia.media.altText,
            mimeType = productMedia.media.mimeType,
            mediaType = productMedia.media.mediaType,
            isThumbnail = productMedia.isThumbnail,
            isActive = productMedia.isActive
        )
    }
}