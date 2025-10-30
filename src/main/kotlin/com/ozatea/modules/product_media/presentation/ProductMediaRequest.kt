package com.ozatea.modules.product_media.presentation

import org.springframework.web.multipart.MultipartFile

data class ProductMediaRequest(
    val productId: Long,
    val media: List<ProductMediaItem>
)

data class ProductMediaItem(
    val file: MultipartFile,
    val altText: String? = null,
    val orderIndex: Int? = null,
    val isThumbnail: Boolean = false
)