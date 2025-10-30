package com.ozatea.modules.media.presentation

import org.springframework.web.multipart.MultipartFile

data class BulkMediaRequest(
    val media: List<MediaItem>
)

data class MediaItem(
    val file: MultipartFile,
    val altText: String? = null
)